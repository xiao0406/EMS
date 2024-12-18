package com.jeesite.modules.job.task.ems.timeshare;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
import com.jeesite.modules.ems.service.EmsElectricityTimeConfService;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeService;
import com.jeesite.modules.job.entity.CalculateJobParam;
import com.jeesite.modules.job.task.utils.Calculatehelper;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.service.entity.CompanyConfig;
import com.jeesite.modules.sys.service.impl.CompanyConfigServiceImpl;
import com.jeesite.modules.sys.service.support.adapt.GlobalCalculateAdapter;
import com.jeesite.modules.sys.utils.CorpUtils;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.*;

/**
 * 类说明
 * 设备日分时运行时长统计
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class TimeShareCalculateDayDeviceRunningTimeTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsElectricPowerConsumptionService powerConsumptionService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private EmsElectricityTimeConfService emsElectricityTimeConfService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;
    private static final String NOLOAD_KEY = "noLoad";
    private static final String STOP_KEY = "stop";
    private static final String RUNNING_KEY = "running";

    /**
     * 更新设备运行阈值或者尖峰平谷时间重新计算设备的运行时长和能耗
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateThresholdReCalculate() {
        execute(null);
    }

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######设备日分时运行时长统计【开始】######");
        log.info("######设备日分时运行时长统计【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User user = new User();
        user.setLoginCode("virtual_gd_job_admin");
        User jobAdmin = userService.getByLoginCode(user);
        CorpUtils.setCurrentCorpCode(jobAdmin.getCorpCode(), jobAdmin.getCorpName());

        CalculateJobParam calculateJobParam = Calculatehelper.parseJobParam(jobParam);
        String execDate = Calculatehelper.getExecDate_Def_OS(calculateJobParam.getAppointedTime());
        GlobalCalculateAdapter.OfFirst_Last ofDayFirstLast = calculateAdapter.getOfDayFirst_Last(DateUtils.parseDate(execDate));
        String busiKey = ofDayFirstLast.getBusiKey();

        XxlJobHelper.log("当前业务时间=》{}", busiKey);
        log.info("当前业务时间=》{}", busiKey);

        XxlJobHelper.log("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));
        log.info("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));

        //查询对应区域的设备数据
        Map<String, List<EmsMeter>> meterCompanyMap = emsMeterService.findMeterCompanyMap();
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        for (Map.Entry<String, List<EmsMeter>> mapEntry : meterCompanyMap.entrySet()) {
            String companyCode = mapEntry.getKey();
            List<EmsMeter> meterList = mapEntry.getValue();

            if (!Calculatehelper.executeCheck(companyCode, calculateJobParam)) {
                continue;
            }

            CompanyConfig cc = companyConfigMap.get(companyCode);
            if (Objects.isNull(cc)) {
                XxlJobHelper.log("当前处理公司=》{}，配置信息不存在，不进行数据处理", companyCode);
                log.info("当前处理公司=》{}，配置信息不存在，不进行数据处理", companyCode);
                continue;
            }

            //获取峰平谷配置
            EmsElectricityTimeConf timeConf = emsElectricityTimeConfService.getByCompanyCode(companyCode);
            //取值
            String cuspTimeRange = "", peakTimeRange = "", fairTimeRange = "", valleyTimeRange = "";
            if (ObjectUtils.isNotEmpty(timeConf)) {
                //尖
                cuspTimeRange = timeConf.getCuspTimeRange();
                //峰
                peakTimeRange = timeConf.getPeakTimeRange();
                //平
                fairTimeRange = timeConf.getFairTimeRange();
                //谷
                valleyTimeRange = timeConf.getValleyTimeRange();
            }

            //批处理开关
            String jobSwitch = cc.getJobSwitch();
            if (SysYesNoEnum._1.getCode().equals(jobSwitch)) {
                execute(busiKey, cuspTimeRange, peakTimeRange, fairTimeRange, valleyTimeRange, companyCode, meterList);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######设备日分时运行时长统计【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######设备日分时运行时长统计【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(String busiKey, String cuspTimeRange, String peakTimeRange, String fairTimeRange, String valleyTimeRange, String companyCode, List<EmsMeter> meterList) {
        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            //空载阈值
            Double noLoadThreshold = emsMeter.getNoLoadThreshold();
            //运行阈值
            Double operationTreshold = emsMeter.getOperationTreshold();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}），空载阈值=》{}，运行阈值=》{}", companyName, companyCode, meterName, meterCode, noLoadThreshold, operationTreshold);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}），空载阈值=》{}，运行阈值=》{}", companyName, companyCode, meterName, meterCode, noLoadThreshold, operationTreshold);

            //数据组装
            EmsTimeShareDeviceRuntime.EmsTimeShareDeviceRuntimeBuilder builder = EmsTimeShareDeviceRuntime.builder();
            builder.deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDate(DateUtils.parseDate(busiKey))        // 数据日期， 2023-03-24
                    .dataType(TemporalGranularityEnum.VD_Day.getCode())        // 数据类型：15分钟、小时、日、月、年
                    .companyCode(companyCode).companyName(companyName);

            Map<String, Double> cusp = timeCalculate(busiKey, meterCode, cuspTimeRange, noLoadThreshold, operationTreshold);
            builder.cuspNoLoadTime(NumberUtils.defNaN(cusp.get(NOLOAD_KEY))).cuspStopTime(NumberUtils.defNaN(cusp.get(STOP_KEY))).cuspRunningTime(NumberUtils.defNaN(cusp.get(RUNNING_KEY)));

            Map<String, Double> peak = timeCalculate(busiKey, meterCode, peakTimeRange, noLoadThreshold, operationTreshold);
            builder.peakNoLoadTime(NumberUtils.defNaN(peak.get(NOLOAD_KEY))).peakStopTime(NumberUtils.defNaN(peak.get(STOP_KEY))).peakRunningTime(NumberUtils.defNaN(peak.get(RUNNING_KEY)));

            Map<String, Double> fair = timeCalculate(busiKey, meterCode, fairTimeRange, noLoadThreshold, operationTreshold);
            builder.fairNoLoadTime(NumberUtils.defNaN(fair.get(NOLOAD_KEY))).fairStopTime(NumberUtils.defNaN(fair.get(STOP_KEY))).fairRunningTime(NumberUtils.defNaN(fair.get(RUNNING_KEY)));

            Map<String, Double> valley = timeCalculate(busiKey, meterCode, valleyTimeRange, noLoadThreshold, operationTreshold);
            builder.valleyNoLoadTime(NumberUtils.defNaN(valley.get(NOLOAD_KEY))).valleyStopTime(NumberUtils.defNaN(valley.get(STOP_KEY))).valleyRunningTime(NumberUtils.defNaN(valley.get(RUNNING_KEY)));

            builder.totalNoLoad(NumberUtils.addAll(cusp.get(NOLOAD_KEY), peak.get(NOLOAD_KEY), fair.get(NOLOAD_KEY), valley.get(NOLOAD_KEY))).totalStop(NumberUtils.addAll(cusp.get(STOP_KEY), peak.get(STOP_KEY), fair.get(STOP_KEY), valley.get(STOP_KEY))).totalRunning(NumberUtils.addAll(cusp.get(RUNNING_KEY), peak.get(RUNNING_KEY), fair.get(RUNNING_KEY), valley.get(RUNNING_KEY)));

            //保存入库
            EmsTimeShareDeviceRuntime build = builder.build();
            EmsTimeShareDeviceRuntime stockedRec = emsTimeShareDeviceRuntimeService.isStockedRec(build);
            if (Objects.nonNull(stockedRec)) {
                build.setId(stockedRec.getId());
                build.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            emsTimeShareDeviceRuntimeService.save(build);
        }
    }

    private Map<String, Double> timeCalculate(String busiKey, String meterCode, String timeRange, Double noLoadThreshold, Double operationTreshold) {
        HashMap<String, Double> calculate = MapUtils.newHashMap();
        Double stop = 0d;
        Double noLoad = 0d;
        Double running = 0d;
        //给默认值
        calculate.put(STOP_KEY, stop);
        calculate.put(NOLOAD_KEY, noLoad);
        calculate.put(RUNNING_KEY, running);
        List<TimeRange> timeRanges = JSONObject.parseArray(timeRange, TimeRange.class);
        if (CollectionUtils.isEmpty(timeRanges) || Objects.isNull(noLoadThreshold) || Objects.isNull(operationTreshold)) {
            //如果没配置，或者阈值为空
            return calculate;
        }

        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setDeviceId(meterCode);
        for (TimeRange range : timeRanges) {
            params.setQryStartTime(DateUtils.parseDate(busiKey + DefaultConstant.EMPTY_BLANK_STR + range.getStartTime()));
            String endTime = range.getEndTime();
            Date endTime_ = DateUtils.parseDate(busiKey + DefaultConstant.EMPTY_BLANK_STR + endTime);
            if (endTime.contains("23:59:59")) {
                endTime_ = DateUtils.calculateSecond(endTime_, 1);
            }
            params.setQryEndTime(endTime_);
            params.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
            params.getSqlMap().getOrder().setOrderBy("a.data_date_time");
            List<EmsElectricPowerConsumption> collect = powerConsumptionService.findList(params);
            if (CollectionUtils.isEmpty(collect)) {
                XxlJobHelper.log("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                log.info("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                continue;
            }

            Date lastTimestamp = collect.get(0).getDataDateTime();//上一个时间戳，默认为第一条记录的数据时间
            //计算当前时间段开机时间
            for (EmsElectricPowerConsumption data : collect) {
                Double positiveActiveEnergy = data.getPositiveActiveEnergy();
                Date timestamp = data.getDataDateTime();

                double minuteDistance = DateUtils.getMinuteDistanceOfTwoDate(lastTimestamp, timestamp);
                if (positiveActiveEnergy.compareTo(noLoadThreshold) <= 0) {
                    //小于等于空载阈值，为  未运行
                    stop = NumberUtils.add(stop, minuteDistance);
                } else if (positiveActiveEnergy.compareTo(noLoadThreshold) > 0 && positiveActiveEnergy.compareTo(operationTreshold) <= 0) {
                    //大于空载阈值，小于等于运行阈值，为  空载
                    noLoad = NumberUtils.add(noLoad, minuteDistance);
                } else if (positiveActiveEnergy.compareTo(operationTreshold) > 0) {
                    //大于运行阈值，为  运行
                    running = NumberUtils.add(running, minuteDistance);
                }
                lastTimestamp = timestamp;
            }
        }
        calculate.put(STOP_KEY, stop);
        calculate.put(NOLOAD_KEY, noLoad);
        calculate.put(RUNNING_KEY, running);

        return calculate;
    }
}

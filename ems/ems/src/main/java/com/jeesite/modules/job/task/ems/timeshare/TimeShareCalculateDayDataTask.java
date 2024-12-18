package com.jeesite.modules.job.task.ems.timeshare;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.service.EmsElectricityTimeConfService;
import com.jeesite.modules.ems.service.EmsMeterCollectedDataService;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.ems.service.EmsTimeSharePowerConsumptionService;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 类说明
 * 电表每日分时电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class TimeShareCalculateDayDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsMeterCollectedDataService collectedDataService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private EmsElectricityTimeConfService emsElectricityTimeConfService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######电表每日分时电耗计算定时任务【开始】######");
        log.info("######电表每日分时电耗计算定时任务【开始】######");
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
        XxlJobHelper.log("######电表每日分时电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######电表每日分时电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(String busiKey, String cuspTimeRange, String peakTimeRange, String fairTimeRange, String valleyTimeRange, String companyCode, List<EmsMeter> meterList) {
        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);

            Double pt = emsMeter.getPt();//电压倍率
            Double ct = emsMeter.getCt();//电流倍率
            Double qt = emsMeter.getQt();//综合倍率

            //数据组装
            EmsTimeSharePowerConsumption.EmsTimeSharePowerConsumptionBuilder builder = EmsTimeSharePowerConsumption.builder();
            builder
                    .deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDate(DateUtils.parseDate(busiKey))        // 数据日期， 2023-03-24
                    .dataType(TemporalGranularityEnum.VD_Day.getCode())        // 数据类型：15分钟、小时、日、月、年
                    .companyCode(companyCode)
                    .companyName(companyName);

            Double cusp = timeEnergy(busiKey, meterCode, cuspTimeRange, qt);
            Double peak = timeEnergy(busiKey, meterCode, peakTimeRange, qt);
            Double fair = timeEnergy(busiKey, meterCode, fairTimeRange, qt);
            Double valley = timeEnergy(busiKey, meterCode, valleyTimeRange, qt);
            builder
                    .totalEnergy(NumberUtils.addAll(cusp, peak, fair, valley))// 正向有功电能
                    .cuspTimeEnergy(cusp)// 反向有功电能
                    .peakTimeEnergy(peak)// 正向无功电能
                    .fairTimeEnergy(fair)// 反向无功电能
                    .valleyTimeEnergy(valley);// 反向无功电能
            //保存入库
            EmsTimeSharePowerConsumption build = builder.build();
            EmsTimeSharePowerConsumption stockedRec = emsTimeSharePowerConsumptionService.isStockedRec(build);
            if (Objects.nonNull(stockedRec)) {
                build.setId(stockedRec.getId());
                build.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            emsTimeSharePowerConsumptionService.save(build);
        }
    }

    private Double timeEnergy(String busiKey, String meterCode, String timeRange, Double qt) {
        Double timeEnergy = 0d;
        List<TimeRange> timeRanges = JSONObject.parseArray(timeRange, TimeRange.class);
        if (CollectionUtils.isEmpty(timeRanges)) {
            //如果没配置，
            return timeEnergy;
        }

        EmsMeterCollectedData params = new EmsMeterCollectedData();
        params.setDeviceId(meterCode);
        for (TimeRange range : timeRanges) {
            params.setDataDateTimeStart(DateUtils.parseDate(busiKey + DefaultConstant.EMPTY_BLANK_STR + range.getStartTime()));
            String endTime = range.getEndTime();
            Date endTime_ = DateUtils.parseDate(busiKey + DefaultConstant.EMPTY_BLANK_STR + endTime);
            if (endTime.contains("23:59:59")) {
                endTime_ = DateUtils.calculateSecond(endTime_, 1);
            }
            params.setDataDateTimeEnd(endTime_);
            //将查询结果中dataDateTime和对象映射，如果key重复，则取后面一条的数据
            List<EmsMeterCollectedData> collect = collectedDataService.calculateBaseDataList(params);
            if (CollectionUtils.isEmpty(collect)) {
                XxlJobHelper.log("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                log.info("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                continue;
            }
            EmsMeterCollectedData thisData = collect.get(collect.size() - 1);
            EmsMeterCollectedData lastData = collect.get(0);

            //当 前一条记录不存在 或 （数据异常 且 数值为 0），则赋默认值
            if (Objects.isNull(lastData) || Calculatehelper.isMissUnvalid(lastData)) {
                lastData = Calculatehelper.getLastValidData(collect);
                if (Objects.isNull(lastData)) {
                    lastData = thisData;
                }
            }

            timeEnergy = NumberUtils.add(timeEnergy, NumberUtils.mulDefNaN(NumberUtils.subDefNaN(thisData.getPositiveActiveEnergy(), lastData.getPositiveActiveEnergy()), qt));
        }
        return timeEnergy;
    }
}

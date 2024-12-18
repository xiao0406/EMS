package com.jeesite.modules.job.task.ems.timeshare;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsTimeShareDeviceRuntime;
import com.jeesite.modules.ems.entity.EmsTimeShareDeviceRuntimeStatistics;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeService;
import com.jeesite.modules.ems.service.EmsTimeShareDeviceRuntimeStatisticsService;
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
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.*;

/**
 * 类说明
 * 设备月分时运行时长统计
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class TimeShareCalculateMonthDeviceRunningTimeTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsTimeShareDeviceRuntimeStatisticsService emsTimeShareDeviceRuntimeStatisticsService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######设备月分时运行时长统计【开始】######");
        log.info("######设备月分时运行时长统计【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User user = new User();
        user.setLoginCode("virtual_gd_job_admin");
        User jobAdmin = userService.getByLoginCode(user);
        CorpUtils.setCurrentCorpCode(jobAdmin.getCorpCode(), jobAdmin.getCorpName());

        CalculateJobParam calculateJobParam = Calculatehelper.parseJobParam(jobParam);
        String execDate = Calculatehelper.getExecDate_Def_OS(calculateJobParam.getAppointedTime());

        XxlJobHelper.log("当前业务时间=》{}", execDate);
        log.info("当前业务时间=》{}", execDate);

        XxlJobHelper.log("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));
        log.info("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));

        GlobalCalculateAdapter.OfFirst_Last ofMonthFirstLast = calculateAdapter.getOfMonthFirst_Last(DateUtils.parseDate(execDate));
        String busiKey = ofMonthFirstLast.getBusiKey();
        XxlJobHelper.log("月份=》{}", busiKey);
        log.info("月份=》{}", busiKey);

        //查询对应区域的设备数据
        Map<String, List<EmsMeter>> meterCompanyMap = emsMeterService.findMeterCompanyMap();
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        Date ofDateFirst = ofMonthFirstLast.getOfDateFirst();
        Date ofDateLast = DateUtils.calculateDay(ofMonthFirstLast.getOfDateLast(), -1);

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

            //批处理开关
            String jobSwitch = cc.getJobSwitch();
            if (SysYesNoEnum._1.getCode().equals(jobSwitch)) {
                execute(busiKey, ofDateFirst, ofDateLast, companyCode, meterList);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######设备月分时运行时长统计【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######设备月分时运行时长统计【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(String busiKey, Date ofDateFirst, Date ofDateLast, String companyCode, List<EmsMeter> meterList) {
        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);

            EmsTimeShareDeviceRuntime where = new EmsTimeShareDeviceRuntime();
            where.setDeviceId(meterCode);
            where.setDataDateStart(ofDateFirst);
            where.setDataDateEnd(ofDateLast);
            where.setDataType(TemporalGranularityEnum.VD_Day.getCode());
            List<EmsTimeShareDeviceRuntime> etsdList = emsTimeShareDeviceRuntimeService.findList(where);

            ArrayList<Double> totalRunningList = new ArrayList<>();
            ArrayList<Double> totalNoLoadList = new ArrayList<>();
            ArrayList<Double> totalStopList = new ArrayList<>();
            ArrayList<Double> cuspRunningList = new ArrayList<>();
            ArrayList<Double> cuspNoLoadList = new ArrayList<>();
            ArrayList<Double> cuspStopList = new ArrayList<>();
            ArrayList<Double> peakRunningList = new ArrayList<>();
            ArrayList<Double> peakNoLoadList = new ArrayList<>();
            ArrayList<Double> peakStopList = new ArrayList<>();
            ArrayList<Double> fairRunningList = new ArrayList<>();
            ArrayList<Double> fairNoLoadList = new ArrayList<>();
            ArrayList<Double> fairStopList = new ArrayList<>();
            ArrayList<Double> valleyRunningList = new ArrayList<>();
            ArrayList<Double> valleyNoLoadList = new ArrayList<>();
            ArrayList<Double> valleyStopList = new ArrayList<>();
            etsdList.forEach(o -> {
                totalRunningList.add(o.getTotalRunning());
                totalNoLoadList.add(o.getTotalNoLoad());
                totalStopList.add(o.getTotalStop());
                cuspRunningList.add(o.getCuspRunningTime());
                cuspNoLoadList.add(o.getCuspNoLoadTime());
                cuspStopList.add(o.getCuspStopTime());
                peakRunningList.add(o.getPeakRunningTime());
                peakNoLoadList.add(o.getPeakNoLoadTime());
                peakStopList.add(o.getPeakStopTime());
                fairRunningList.add(o.getFairRunningTime());
                fairNoLoadList.add(o.getFairNoLoadTime());
                fairStopList.add(o.getFairStopTime());
                valleyRunningList.add(o.getValleyRunningTime());
                valleyNoLoadList.add(o.getValleyNoLoadTime());
                valleyStopList.add(o.getValleyStopTime());
            });
            Double totalRunning = NumberUtils.addAll(totalRunningList.toArray(new Double[0]));
            Double totalNoLoad = NumberUtils.addAll(totalNoLoadList.toArray(new Double[0]));
            Double totalStop = NumberUtils.addAll(totalStopList.toArray(new Double[0]));
            Double cuspRunning = NumberUtils.addAll(cuspRunningList.toArray(new Double[0]));
            Double cuspNoLoad = NumberUtils.addAll(cuspNoLoadList.toArray(new Double[0]));
            Double cuspStop = NumberUtils.addAll(cuspStopList.toArray(new Double[0]));
            Double peakRunning = NumberUtils.addAll(peakRunningList.toArray(new Double[0]));
            Double peakNoLoad = NumberUtils.addAll(peakNoLoadList.toArray(new Double[0]));
            Double peakStop = NumberUtils.addAll(peakStopList.toArray(new Double[0]));
            Double fairRunning = NumberUtils.addAll(fairRunningList.toArray(new Double[0]));
            Double fairNoLoad = NumberUtils.addAll(fairNoLoadList.toArray(new Double[0]));
            Double fairStop = NumberUtils.addAll(fairStopList.toArray(new Double[0]));
            Double valleyRunning = NumberUtils.addAll(valleyRunningList.toArray(new Double[0]));
            Double valleyNoLoad = NumberUtils.addAll(valleyNoLoadList.toArray(new Double[0]));
            Double valleyStop = NumberUtils.addAll(valleyStopList.toArray(new Double[0]));

            //数据组装
            EmsTimeShareDeviceRuntimeStatistics.EmsTimeShareDeviceRuntimeStatisticsBuilder builder = EmsTimeShareDeviceRuntimeStatistics.builder();
            builder.deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDateKey(busiKey)        // 数据日期， 2023-03-24
                    .dataType(TemporalGranularityEnum.VD_Month.getCode())        // 数据类型：15分钟、小时、日、月、年
                    .totalNoLoad(totalNoLoad)
                    .totalStop(totalStop)
                    .totalRunning(totalRunning)
                    .cuspNoLoadTime(cuspNoLoad)
                    .cuspStopTime(cuspStop)
                    .cuspRunningTime(cuspRunning)
                    .peakNoLoadTime(peakNoLoad)
                    .peakStopTime(peakStop)
                    .peakRunningTime(peakRunning)
                    .fairNoLoadTime(fairNoLoad)
                    .fairStopTime(fairStop)
                    .fairRunningTime(fairRunning)
                    .valleyNoLoadTime(valleyNoLoad)
                    .valleyStopTime(valleyStop)
                    .valleyRunningTime(valleyRunning)
                    .companyCode(companyCode)
                    .companyName(companyName);

            //保存入库
            EmsTimeShareDeviceRuntimeStatistics build = builder.build();
            EmsTimeShareDeviceRuntimeStatistics stockedRec = emsTimeShareDeviceRuntimeStatisticsService.isStockedRec(build);
            if (Objects.nonNull(stockedRec)) {
                build.setId(stockedRec.getId());
                build.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            emsTimeShareDeviceRuntimeStatisticsService.save(build);
        }
    }
}

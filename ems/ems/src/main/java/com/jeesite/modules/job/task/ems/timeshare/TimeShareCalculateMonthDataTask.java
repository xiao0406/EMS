package com.jeesite.modules.job.task.ems.timeshare;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumption;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.ems.service.EmsTimeSharePowerConsumptionService;
import com.jeesite.modules.ems.service.EmsTimeSharePowerConsumptionStatisticsService;
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
 * 电表每月分时电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class TimeShareCalculateMonthDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsTimeSharePowerConsumptionStatisticsService tmcStatisticsService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private EmsTimeSharePowerConsumptionService tmcService;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######电表每月分时电耗计算定时任务【开始】######");
        log.info("######电表每月分时电耗计算定时任务【开始】######");
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
        XxlJobHelper.log("######电表每月分时电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######电表每月分时电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(String busiKey, Date ofDateFirst, Date ofDateLast, String companyCode, List<EmsMeter> meterList) {
        //按设备去处理数据
        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);

            EmsTimeSharePowerConsumption where = new EmsTimeSharePowerConsumption();
            where.setDeviceId(meterCode);
            where.setDataDateStart(ofDateFirst);
            where.setDataDateEnd(ofDateLast);
            where.setDataType(TemporalGranularityEnum.VD_Day.getCode());
            List<EmsTimeSharePowerConsumption> tspcList = tmcService.findList(where);


            ArrayList<Double> cuspList = new ArrayList<>();
            ArrayList<Double> peakList = new ArrayList<>();
            ArrayList<Double> fairList = new ArrayList<>();
            ArrayList<Double> valleyList = new ArrayList<>();
            tspcList.forEach(o -> {
                cuspList.add(o.getCuspTimeEnergy());
                peakList.add(o.getPeakTimeEnergy());
                fairList.add(o.getFairTimeEnergy());
                valleyList.add(o.getValleyTimeEnergy());
            });
            Double cusp = NumberUtils.addAll(cuspList.toArray(new Double[0]));
            Double peak = NumberUtils.addAll(peakList.toArray(new Double[0]));
            Double fair = NumberUtils.addAll(fairList.toArray(new Double[0]));
            Double valley = NumberUtils.addAll(valleyList.toArray(new Double[0]));

            //数据组装
            EmsTimeSharePowerConsumptionStatistics.EmsTimeSharePowerConsumptionStatisticsBuilder builder = EmsTimeSharePowerConsumptionStatistics.builder();
            builder
                    .deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDateKey(busiKey)        // 数据日期， 2023-03-24
                    .dataType(TemporalGranularityEnum.VD_Month.getCode())        // 数据类型：15分钟、小时、日、月、年
                    .companyCode(companyCode)
                    .companyName(companyName)
                    .totalEnergy(NumberUtils.addAll(cusp, peak, fair, valley))// 正向有功电能
                    .cuspTimeEnergy(cusp)// 反向有功电能
                    .peakTimeEnergy(peak)// 正向无功电能
                    .fairTimeEnergy(fair)// 反向无功电能
                    .valleyTimeEnergy(valley);// 反向无功电能
            //保存入库
            EmsTimeSharePowerConsumptionStatistics build = builder.build();
            EmsTimeSharePowerConsumptionStatistics stockedRec = tmcStatisticsService.isStockedRec(build);
            if (Objects.nonNull(stockedRec)) {
                build.setId(stockedRec.getId());
                build.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            tmcStatisticsService.save(build);
        }
    }
}

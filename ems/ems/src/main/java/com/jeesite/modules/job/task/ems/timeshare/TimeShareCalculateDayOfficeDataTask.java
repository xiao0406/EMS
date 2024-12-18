package com.jeesite.modules.job.task.ems.timeshare;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsMeterOffice;
import com.jeesite.modules.ems.entity.EmsTimeShareOfficeConsumption;
import com.jeesite.modules.ems.entity.EmsTimeSharePowerConsumption;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.ems.service.EmsTimeShareOfficeConsumptionService;
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
 * 部门每日分时电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class TimeShareCalculateDayOfficeDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;
    @Resource
    private EmsTimeShareOfficeConsumptionService emsTimeShareOfficeConsumptionService;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######部门每日分时电耗计算定时任务【开始】######");
        log.info("######部门每日分时电耗计算定时任务【开始】######");
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

        Date dataDate = DateUtils.parseDate(busiKey);

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

            //批处理开关
            String jobSwitch = cc.getJobSwitch();
            if (SysYesNoEnum._1.getCode().equals(jobSwitch)) {
                execute(dataDate, companyCode, meterList);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######部门每日分时电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######部门每日分时电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(Date dataDate, String companyCode, List<EmsMeter> meterList) {
        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);

            //查询设备分时日电耗，正常情况下只有一条
            EmsTimeSharePowerConsumption where = new EmsTimeSharePowerConsumption();
            where.setDeviceId(meterCode);
            where.setDataDate(dataDate);
            where.setDataType(TemporalGranularityEnum.VD_Day.getCode());
            EmsTimeSharePowerConsumption dailyCons = emsTimeSharePowerConsumptionService.findMeterDailyConsumption(where);
            if (Objects.isNull(dailyCons)) {
                //无分时数据，后续计算不进行
                continue;
            }

            //获取设备部门配置
            List<EmsMeterOffice> meterOfficeList = emsMeter.getMeterOfficeList();
            if (CollectionUtils.isEmpty(meterOfficeList)) {
                //无设备部门配置，后续计算不进行
                continue;
            }
            for (EmsMeterOffice emsMeterOffice : meterOfficeList) {
                String officeCode = emsMeterOffice.getOfficeCode();
                String officeName = emsMeterOffice.getOfficeName();
                //部门使用比例
                Double powerRatio = emsMeterOffice.getPowerRatio();

                //数据组装
                EmsTimeShareOfficeConsumption.EmsTimeShareOfficeConsumptionBuilder builder = EmsTimeShareOfficeConsumption.builder();
                builder
                        .officeCode(officeCode)
                        .officeName(officeName)
                        .deviceId(meterCode)        // 设备ID
                        .deviceName(meterName)        // 设备名称
                        .dataDate(dataDate)        // 数据日期， 2023-03-24
                        .dataType(TemporalGranularityEnum.VD_Day.getCode())        // 数据类型：15分钟、小时、日、月、年
                        .companyCode(companyCode)
                        .companyName(companyName)
                        .totalEnergy(NumberUtils.mul(dailyCons.getTotalEnergy(), NumberUtils.div(powerRatio, 100)))
                        .cuspTimeEnergy(NumberUtils.mul(dailyCons.getCuspTimeEnergy(), NumberUtils.div(powerRatio, 100)))
                        .peakTimeEnergy(NumberUtils.mul(dailyCons.getPeakTimeEnergy(), NumberUtils.div(powerRatio, 100)))
                        .fairTimeEnergy(NumberUtils.mul(dailyCons.getFairTimeEnergy(), NumberUtils.div(powerRatio, 100)))
                        .valleyTimeEnergy(NumberUtils.mul(dailyCons.getValleyTimeEnergy(), NumberUtils.div(powerRatio, 100)));
                //保存入库
                EmsTimeShareOfficeConsumption build = builder.build();
                EmsTimeShareOfficeConsumption stockedRec = emsTimeShareOfficeConsumptionService.isStockedRec(build);
                if (Objects.nonNull(stockedRec)) {
                    build.setId(stockedRec.getId());
                    build.setIsNewRecord(stockedRec.getIsNewRecord());
                }
                emsTimeShareOfficeConsumptionService.save(build);
            }
        }
    }
}

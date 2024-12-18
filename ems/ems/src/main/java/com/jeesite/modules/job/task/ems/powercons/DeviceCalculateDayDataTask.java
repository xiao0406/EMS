package com.jeesite.modules.job.task.ems.powercons;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
import com.jeesite.modules.ems.service.EmsMeterCollectedDataService;
import com.jeesite.modules.ems.service.EmsMeterService;
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
import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 类说明
 * 电表每日电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class DeviceCalculateDayDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsMeterCollectedDataService collectedDataService;
    @Resource
    private EmsElectricPowerConsumptionService powerConsumptionService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) throws ParseException {
        XxlJobHelper.log("######电表每日电耗计算定时任务【开始】######");
        log.info("######电表每日电耗计算定时任务【开始】######");
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

        Date optDate = DateUtils.parseDate(busiKey);

        //查询设备数据
        Map<String, List<EmsMeter>> meterCompanyMap = emsMeterService.findMeterCompanyMap();
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        //取当天开始时间和下一天开始时间
        Date optDTime = DateUtils.getOfDayFirst(optDate);
        Date endOptDTime = DateUtils.calculateDay(optDTime, 1);

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
                execute(optDTime, endOptDTime, companyCode, meterList);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######电表每日电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######电表每日电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(Date optDTime, Date endOptDTime, String companyCode, List<EmsMeter> meterList) {
        //参数准备
        EmsMeterCollectedData params = new EmsMeterCollectedData();
        params.setDataDateTimeStart(optDTime);
        params.setDataDateTimeEnd(endOptDTime);

        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);

            Double pt = emsMeter.getPt();//电压倍率
            Double ct = emsMeter.getCt();//电流倍率
            Double qt = emsMeter.getQt();//综合倍率

            params.setDeviceId(meterCode);
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

            //计算数据，保存入库
            //数据组装
            EmsElectricPowerConsumption.EmsElectricPowerConsumptionBuilder builder = EmsElectricPowerConsumption.builder();
            builder
                    .deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDateTime(optDTime)        // 数据时间， 2023-03-24 22:00:12
                    .dataDate(optDTime)        // 数据日期， 2023-03-24
                    .dataTime(new Time(optDTime.getTime()))        // 数据时间， 22:00:12
                    .dataType(TemporalGranularityEnum.VD_Day.getCode())        // 数据类型：15分钟、小时、日、月、年
                    .companyCode(companyCode)
                    .companyName(companyName)
                    .positiveActiveEnergy(NumberUtils.mulDefNaN(NumberUtils.subDefNaN(thisData.getPositiveActiveEnergy(), lastData.getPositiveActiveEnergy()), qt))// 正向有功电能
                    .reverseActivePower(NumberUtils.mulDefNaN(NumberUtils.subDefNaN(thisData.getReverseActiveEnergy(), lastData.getReverseActiveEnergy()), qt))// 反向有功电能
                    .positiveReactiveEnergy(NumberUtils.mulDefNaN(NumberUtils.subDefNaN(thisData.getPositiveReactiveEnergy(), lastData.getPositiveReactiveEnergy()), qt))// 正向无功电能
                    .reverseReactivePower(NumberUtils.mulDefNaN(NumberUtils.subDefNaN(thisData.getReverseReactiveEnergy(), lastData.getReverseReactiveEnergy()), qt));// 反向无功电能

            Double aphaseV = NumberUtils.mulDefNaN(thisData.getAphaseV(), pt);
            Double bphaseV = NumberUtils.mulDefNaN(thisData.getBphaseV(), pt);
            Double cphaseV = NumberUtils.mulDefNaN(thisData.getCphaseV(), pt);
            builder
                    .aphaseVoltage(aphaseV)// A相电压
                    .bphaseVoltage(bphaseV)// B相电压
                    .cphaseVoltage(cphaseV)// C相电压
                    .voltageUnbalanceDegree(Calculatehelper.threePhaseUnbalance(aphaseV, bphaseV, cphaseV));// 三相电压不平衡度

            Double aphaseA = NumberUtils.mulDefNaN(thisData.getAphaseA(), ct);
            Double bphaseA = NumberUtils.mulDefNaN(thisData.getBphaseA(), ct);
            Double cphaseA = NumberUtils.mulDefNaN(thisData.getCphaseA(), ct);
            builder
                    .aphaseCurrent(aphaseA)// A相电流
                    .bphaseCurrent(bphaseA)// B相电流
                    .cphaseCurrent(cphaseA)// C相电流
                    .currentUnbalanceDegree(Calculatehelper.threePhaseUnbalance(aphaseA, bphaseA, cphaseA));// 三相电流不平衡度


            Double totalAp = NumberUtils.mulDefNaN(thisData.getTotalAp(), qt);
            Double aphaseAp = NumberUtils.mulDefNaN(thisData.getAphaseAp(), qt);
            Double bphaseAp = NumberUtils.mulDefNaN(thisData.getBphaseAp(), qt);
            Double cphaseAp = NumberUtils.mulDefNaN(thisData.getCphaseAp(), qt);
            builder
                    .totalActivePower(totalAp)// 总有功功率
                    .aphaseActivePower(aphaseAp)// A相有功功率
                    .bphaseActivePower(bphaseAp)// B相有功功率
                    .cphaseActivePower(cphaseAp);// C相有功功率

            Double totalRp = NumberUtils.mulDefNaN(thisData.getTotalRp(), qt);
            Double aphaseRp = NumberUtils.mulDefNaN(thisData.getAphaseRp(), qt);
            Double bphaseRp = NumberUtils.mulDefNaN(thisData.getBphaseRp(), qt);
            Double cphaseRp = NumberUtils.mulDefNaN(thisData.getCphaseRp(), qt);
            builder
                    .totalReactivePower(totalRp)// 总无功功率
                    .aphaseReactivePower(aphaseRp)// A相无功功率
                    .bphaseReactivePower(bphaseRp)// B相无功功率
                    .cphaseReactivePower(cphaseRp)// C相无功功率
                    .totalPowerFactor(NumberUtils.defNaN(thisData.getTotalPf()))// 总功率因数
                    .aphasePowerFactor(NumberUtils.defNaN(thisData.getAphasePf()))// A相功率因数
                    .bphasePowerFactor(NumberUtils.defNaN(thisData.getBphasePf()))// B相功率因数
                    .cphasePowerFactor(NumberUtils.defNaN(thisData.getCphasePf()))// C相功率因数
                    .totalApparentPower(Calculatehelper.apparentPower(totalAp, totalRp))// 总视在功率
                    .aphaseApparentPower(Calculatehelper.apparentPower(aphaseAp, aphaseRp))// A相视在功率
                    .bphaseApparentPower(Calculatehelper.apparentPower(bphaseAp, bphaseRp))// B相视在功率
                    .cphaseApparentPower(Calculatehelper.apparentPower(cphaseAp, cphaseRp))// C相视在功率
                    .mqttTs(thisData.getMqttTs());

            EmsElectricPowerConsumption eepc = builder.build();
            EmsElectricPowerConsumption stockedRec = powerConsumptionService.isStockedRec(eepc);
            if (Objects.nonNull(stockedRec)) {
                eepc.setId(stockedRec.getId());
                eepc.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            powerConsumptionService.save(eepc);
        }
    }
}

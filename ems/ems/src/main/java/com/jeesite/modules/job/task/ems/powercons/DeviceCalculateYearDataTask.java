package com.jeesite.modules.job.task.ems.powercons;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumptionStatistics;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionStatisticsService;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 类说明
 * 电表每年电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class DeviceCalculateYearDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsElectricPowerConsumptionStatisticsService powerConsumptionStatisticsService;
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
        XxlJobHelper.log("######电表每年电耗计算定时任务【开始】######");
        log.info("######电表每年电耗计算定时任务【开始】######");
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

        GlobalCalculateAdapter.OfFirst_Last ofYearFirstLast = calculateAdapter.getOfYearFirst_Last(DateUtils.parseDate(execDate));
        String busiKey = ofYearFirstLast.getBusiKey();
        XxlJobHelper.log("年份=》{}", busiKey);
        log.info("年份=》{}", busiKey);

        //查询设备数据
        Map<String, List<EmsMeter>> meterCompanyMap = emsMeterService.findMeterCompanyMap();
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        //当前将要计算时刻  上一个时刻
        Date timeStart = ofYearFirstLast.getOfDateFirst();
        Date timeEnd = DateUtils.calculateMonth(ofYearFirstLast.getOfDateLast(), -1);

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
                execute(busiKey, timeStart, timeEnd, companyCode, meterList);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######电表每年电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######电表每年电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(String busiKey, Date timeStart, Date timeEnd, String companyCode, List<EmsMeter> meterList) {
        EmsElectricPowerConsumptionStatistics params = new EmsElectricPowerConsumptionStatistics();
        params.setQryStartTime(DateUtils.formatDate(timeStart, "yyyy-MM"));
        params.setQryEndTime(DateUtils.formatDate(timeEnd, "yyyy-MM"));

        for (EmsMeter emsMeter : meterList) {
            String meterCode = emsMeter.getMeterCode();
            String meterName = emsMeter.getMeterName();
            String companyName = emsMeter.getCompanyName();

            XxlJobHelper.log("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);
            log.info("当前公司=》{}（{}），当前电表=》{}（{}）", companyName, companyCode, meterName, meterCode);

            params.setDeviceId(meterCode);
            params.setDataType(TemporalGranularityEnum.VD_Month.getCode());
            params.getSqlMap().getOrder().setOrderBy("a.data_date_key");
            //将查询结果中以dataDateTime排序，后面按顺序取值
            List<EmsElectricPowerConsumptionStatistics> collect = powerConsumptionStatisticsService.findList(params);
            EmsElectricPowerConsumptionStatistics lastData = null;
            if (!CollectionUtils.isEmpty(collect)) {
                //当前时刻记录
                lastData = collect.get(collect.size() - 1);
            }
            if (Objects.isNull(lastData)) {
                XxlJobHelper.log("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                log.info("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                continue;
            }

            List<Double> positiveActiveEnergy = ListUtils.newLinkedList();
            List<Double> reverseActivePower = ListUtils.newLinkedList();
            List<Double> positiveReactiveEnergy = ListUtils.newLinkedList();
            List<Double> reverseReactivePower = ListUtils.newLinkedList();
            collect.forEach(o -> {
                positiveActiveEnergy.add(o.getPositiveActiveEnergy());
                reverseActivePower.add(o.getReverseActivePower());
                positiveReactiveEnergy.add(o.getPositiveReactiveEnergy());
                reverseReactivePower.add(o.getReverseReactivePower());
            });

            //计算数据，保存入库
            //数据组装
            EmsElectricPowerConsumptionStatistics.EmsElectricPowerConsumptionStatisticsBuilder builder = EmsElectricPowerConsumptionStatistics.builder();
            builder
                    .deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDateKey(busiKey)        // 数据时间， 2023-03-24 22:00:12
                    .dataType(TemporalGranularityEnum.VD_Year.getCode())        // 数据类型：15分钟、小时、日、月、年
                    .companyCode(companyCode)
                    .companyName(companyName)
                    .positiveActiveEnergy(NumberUtils.addAll(positiveActiveEnergy.toArray(new Double[0])))// 正向有功电能
                    .reverseActivePower(NumberUtils.addAll(reverseActivePower.toArray(new Double[0])))// 反向有功电能
                    .positiveReactiveEnergy(NumberUtils.addAll(positiveReactiveEnergy.toArray(new Double[0])))// 正向无功电能
                    .reverseReactivePower(NumberUtils.addAll(reverseReactivePower.toArray(new Double[0])))// 反向无功电能
                    .aphaseVoltage(lastData.getAphaseVoltage())// A相电压
                    .bphaseVoltage(lastData.getBphaseVoltage())// B相电压
                    .cphaseVoltage(lastData.getCphaseVoltage())// C相电压
                    .voltageUnbalanceDegree(lastData.getVoltageUnbalanceDegree())// 三相电压不平衡度
                    .aphaseCurrent(lastData.getAphaseCurrent())// A相电流
                    .bphaseCurrent(lastData.getBphaseCurrent())// B相电流
                    .cphaseCurrent(lastData.getCphaseCurrent())// C相电流
                    .currentUnbalanceDegree(lastData.getCurrentUnbalanceDegree())// 三相电流不平衡度
                    .totalActivePower(lastData.getTotalActivePower())// 总有功功率
                    .aphaseActivePower(lastData.getAphaseActivePower())// A相有功功率
                    .bphaseActivePower(lastData.getBphaseActivePower())// B相有功功率
                    .cphaseActivePower(lastData.getCphaseActivePower())// C相有功功率
                    .totalReactivePower(lastData.getTotalReactivePower())// 总无功功率
                    .aphaseReactivePower(lastData.getAphaseReactivePower())// A相无功功率
                    .bphaseReactivePower(lastData.getBphaseReactivePower())// B相无功功率
                    .cphaseReactivePower(lastData.getCphaseReactivePower())// C相无功功率
                    .totalPowerFactor(lastData.getTotalPowerFactor())// 总功率因数
                    .aphasePowerFactor(lastData.getAphasePowerFactor())// A相功率因数
                    .bphasePowerFactor(lastData.getBphasePowerFactor())// B相功率因数
                    .cphasePowerFactor(lastData.getCphasePowerFactor())// C相功率因数
                    .totalApparentPower(lastData.getTotalApparentPower())// 总视在功率
                    .aphaseApparentPower(lastData.getAphaseApparentPower())// A相视在功率
                    .bphaseApparentPower(lastData.getBphaseApparentPower())// B相视在功率
                    .cphaseApparentPower(lastData.getCphaseApparentPower())// C相视在功率
                    .mqttTs(lastData.getMqttTs());

            EmsElectricPowerConsumptionStatistics build = builder.build();
            EmsElectricPowerConsumptionStatistics stockedRec = powerConsumptionStatisticsService.isStockedRec(build);
            if (Objects.nonNull(stockedRec)) {
                build.setId(stockedRec.getId());
                build.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            powerConsumptionStatisticsService.save(build);
        }
    }
}

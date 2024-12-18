package com.jeesite.modules.job.task.ems.powercons;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.constant.enums.TimeUnitEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.constant.RedisConstant;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
import com.jeesite.modules.ems.service.EmsMeterCollectedDataService;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.etl.clock.Clock;
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
import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类说明
 * 电表15分钟电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class DeviceCalculateQuarterDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsMeterCollectedDataService collectedDataService;
    @Resource
    private EmsElectricPowerConsumptionService powerConsumptionService;
    @Resource
    private EmsMeterService emsMeterService;
    @Resource
    private RedisService redisService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;
    private static final Integer CLOCK_STEP = 15;

    public String getKEY_etlSchedule(String appointedTime) {
        /**
         * 此处逻辑和execute方法内获取 busiKey 部分逻辑保持一致
         * 此方法的目的是提供 KEY_etlSchedule 方便全量数据生成时清除缓存
         */
        String execDate = Calculatehelper.getExecDate(appointedTime);
        GlobalCalculateAdapter.OfFirst_Last ofDayFirstLast = calculateAdapter.getOfDayFirst_Last(DateUtils.parseDate(execDate));
        String busiKey = ofDayFirstLast.getBusiKey();

        String KEY_etlSchedule = RedisConstant._ETL_SCHEDULE_QUARTER_ + busiKey;
        return KEY_etlSchedule;
    }

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Long execute(String jobParam) throws ParseException {
        XxlJobHelper.log("######电表15分钟电耗计算定时任务【开始】######");
        log.info("######电表15分钟电耗计算定时任务【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User user = new User();
        user.setLoginCode("virtual_gd_job_admin");
        User jobAdmin = userService.getByLoginCode(user);
        CorpUtils.setCurrentCorpCode(jobAdmin.getCorpCode(), jobAdmin.getCorpName());

        CalculateJobParam calculateJobParam = Calculatehelper.parseJobParam(jobParam);
        String execDate = Calculatehelper.getExecDate(calculateJobParam.getAppointedTime());
        GlobalCalculateAdapter.OfFirst_Last ofDayFirstLast = calculateAdapter.getOfDayFirst_Last(DateUtils.parseDate(execDate));
        String busiKey = ofDayFirstLast.getBusiKey();

        XxlJobHelper.log("当前业务时间=》{}", busiKey);
        log.info("当前业务时间=》{}", busiKey);

        XxlJobHelper.log("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));
        log.info("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));

        String KEY_etlSchedule = RedisConstant._ETL_SCHEDULE_QUARTER_ + busiKey;
        refreshCacheIfNotExist(busiKey, KEY_etlSchedule);

        //如果key存在，则表示今天已经计算过，则查看当前执行到哪个时间节点
        Long bitpos = redisService.bitpos(KEY_etlSchedule, 1);
        XxlJobHelper.log("当前任务执行坐标=》{}", bitpos);
        log.info("当前任务执行坐标=》{}", bitpos);
        if (Double.compare(-1d, bitpos) == 0) {
            //ps:-1表示已经没有需要处理的时间点了，当天批次的任务已经结束
            return Calculatehelper.ABORT_BIT_POS;
        }

        Date optDTime = Calculatehelper.getPlTime(busiKey, bitpos);
        XxlJobHelper.log("当前任务执行时刻=》{}", DateUtils.formatDate(optDTime, "yyyy-MM-dd HH:mm:ss"));
        log.info("当前任务执行时刻=》{}", DateUtils.formatDate(optDTime, "yyyy-MM-dd HH:mm:ss"));
        if (optDTime.getTime() > System.currentTimeMillis()) {
            XxlJobHelper.log("时间还没到，定时任务处理终止(做时间对比的时候要加上偏移量)");
            log.info("时间还没到，定时任务处理终止(做时间对比的时候要加上偏移量)");
            return Calculatehelper.ABORT_BIT_POS;
        }

        //查询设备数据
        Map<String, List<EmsMeter>> meterCompanyMap = emsMeterService.findMeterCompanyMap();
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        //计算查询时间得起始时间和结束时间
        Date lastOptDTime = DateUtils.calculateMinute(optDTime, -1 * CLOCK_STEP);

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
                execute(optDTime, lastOptDTime, companyCode, meterList);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        XxlJobHelper.log("该时刻处理完成后，缓存=》{}，偏移量=》{}设置为false", KEY_etlSchedule, bitpos);
        log.info("该时刻处理完成后，缓存=》{}，偏移量=》{}设置为false", KEY_etlSchedule, bitpos);
        redisService.setbit(KEY_etlSchedule, bitpos, 0);

        stopWatch.stop();
        XxlJobHelper.log("######电表15分钟电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######电表15分钟电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());

        return bitpos;
    }

    private void execute(Date optDTime, Date lastOptDTime, String companyCode, List<EmsMeter> meterList) {
        //参数准备
        EmsMeterCollectedData params = new EmsMeterCollectedData();
        params.setDataDateTimeStart(lastOptDTime);
        params.setDataDateTimeEnd(optDTime);

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
//                params.setCompanyCode(companyCode);
            //将查询结果中dataDateTime和对象映射，如果key重复，则取后面一条的数据
            Map<Date, EmsMeterCollectedData> collect = collectedDataService.calculateBaseDataList(params).stream().collect(Collectors.toMap(EmsMeterCollectedData::getDataDateTime, EmsMeterCollectedData -> EmsMeterCollectedData, (key1, key2) -> key2));
            //当前时刻记录
            EmsMeterCollectedData thisData = collect.get(optDTime);
            //上一时刻记录
            EmsMeterCollectedData lastData = collect.get(lastOptDTime);
            if (Objects.isNull(thisData)) {
                XxlJobHelper.log("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                log.info("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                continue;
            }

            //当 前一条记录不存在 或 （数据异常 且 数值为 0），则赋默认值
            if (Objects.isNull(lastData)
                    || Calculatehelper.isMissUnvalid(lastData)) {
                lastData = thisData;
            }

            //计算数据，保存入库
            //数据组装
            EmsElectricPowerConsumption.EmsElectricPowerConsumptionBuilder builder = EmsElectricPowerConsumption.builder();
            builder
                    .deviceId(meterCode)        // 设备ID
                    .deviceName(meterName)        // 设备名称
                    .dataDateTime(optDTime)        // 数据时间， 2023-03-24 22:00:12
                    .dataDate(DateUtils.parseDate(DateUtils.formatDate(optDTime)))        // 数据日期， 2023-03-24
                    .dataTime(new Time(optDTime.getTime()))        // 数据时间， 22:00:12
                    .dataType(TemporalGranularityEnum.VD_Quarter.getCode())        // 数据类型：15分钟、小时、日、月、年
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

    /**
     * 如果缓存不存在，则刷新缓存
     *
     * @param busiKey
     * @param KEY_etlSchedule
     */
    private void refreshCacheIfNotExist(String busiKey, String KEY_etlSchedule) {
        XxlJobHelper.log("判断当前redis是否存在时刻表{}的缓存", KEY_etlSchedule);
        log.info("判断当前redis是否存在时刻表{}的缓存", KEY_etlSchedule);
        boolean isExist = redisService.hasKey(KEY_etlSchedule);
        if (!isExist) {
            XxlJobHelper.log("{}时刻表不存在，重新生成", KEY_etlSchedule);
            log.info("{}时刻表不存在，重新生成", KEY_etlSchedule);
            List<String> pendulum = new Clock().pendulum(TimeUnitEnum.YMD_05, CLOCK_STEP);
            //此处一定要保证有序
            LinkedHashMap<Long, Integer> pendulumMap = new LinkedHashMap<>();
            for (String pl : pendulum) {
                String plTime = busiKey + DefaultConstant.EMPTY_BLANK_STR + pl;
                long time = Calculatehelper.bitMapOffset(busiKey, plTime);
                //默认都是false，代表还没有生成数据
                pendulumMap.put(time, 1);
            }
//            //从数据库查询看当前计算执行的节点
//            List<Date> stockPendulum = powerConsumptionService.getStockPendulum(busiKey, TemporalGranularityEnum.VD_Quarter);
//            if (!CollectionUtils.isEmpty(stockPendulum)) {
//                //如果有，则重新生成redis缓存
//                stockPendulum.forEach(o -> pendulumMap.put(Long.parseLong(DateUtils.formatDate(o, PENDULUM_FORMAT)), 0));
//            }//如果没有记录，则表示当天数据还未生成
            XxlJobHelper.log("把时刻表{}放入redis", KEY_etlSchedule);
            log.info("把时刻表{}放入redis", KEY_etlSchedule);
            pendulumMap.entrySet().forEach(o -> {
                redisService.setbit(KEY_etlSchedule, o.getKey(), o.getValue());
            });
            //时刻表设置缓存时间
            redisService.expire(KEY_etlSchedule, Calculatehelper.KEY_ETLSCHEDULE_OUT_TIME);
        }
    }

}

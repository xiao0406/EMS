package com.jeesite.modules.job.task.ems.powercons;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.constant.enums.TimeUnitEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.constant.RedisConstant;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumption;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.service.EmsAreaService;
import com.jeesite.modules.ems.service.EmsElectricPowerAreaConsumptionService;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 类说明
 * 区域15分钟电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class AreaCalculateQuarterDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsAreaService emsAreaService;
    @Resource
    private EmsElectricPowerConsumptionService powerConsumptionService;
    @Resource
    private EmsElectricPowerAreaConsumptionService emsElectricPowerAreaConsumptionService;
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

        String KEY_etlSchedule = RedisConstant._ETL_AREA_SCHEDULE_QUARTER_ + busiKey;
        return KEY_etlSchedule;
    }

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Long execute(String jobParam) throws ParseException {
        XxlJobHelper.log("######区域15分钟电耗计算定时任务【开始】######");
        log.info("######区域15分钟电耗计算定时任务【开始】######");
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

        String KEY_etlSchedule = RedisConstant._ETL_AREA_SCHEDULE_QUARTER_ + busiKey;
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

        //查询所有区域编码
        Map<String, List<EmsArea>> areaCompanyMap = emsAreaService.findAreaCompanyMap(new EmsArea());
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        for (Map.Entry<String, List<EmsArea>> mapEntry : areaCompanyMap.entrySet()) {
            String companyCode = mapEntry.getKey();
            List<EmsArea> emsAreas = mapEntry.getValue();

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
                execute(optDTime, companyCode, emsAreas);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        XxlJobHelper.log("该时刻处理完成后，缓存=》{}，偏移量=》{}设置为false", KEY_etlSchedule, bitpos);
        log.info("该时刻处理完成后，缓存=》{}，偏移量=》{}设置为false", KEY_etlSchedule, bitpos);
        redisService.setbit(KEY_etlSchedule, bitpos, 0);

        stopWatch.stop();
        XxlJobHelper.log("######区域15分钟电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######区域15分钟电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());

        return bitpos;
    }

    private void execute(Date optDTime, String companyCode, List<EmsArea> emsAreas) {
        for (EmsArea emsArea : emsAreas) {
            //按区域生成数据
            String areaCode = emsArea.getAreaCode();
            String areaName = emsArea.getAreaName();
            String companyName = emsArea.getCompanyName();
            List<String> areaMarkList = Calculatehelper.getMeterCalculateCodeList(emsArea);

            XxlJobHelper.log("当前公司=》{}，当前区域=》{}（{}），计量电表=》{}", companyCode, areaName, areaCode, areaMarkList);
            log.info("当前公司=》{}，当前区域=》{}（{}），计量电表=》{}", companyCode, areaName, areaCode, areaMarkList);

            //组装统计条件
            EmsElectricPowerConsumption where = new EmsElectricPowerConsumption();
            where.setAreaCode(areaCode);
            where.setDataDateTime(optDTime);
            where.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
            where.setAreaMarkList(areaMarkList);
            EmsElectricPowerAreaConsumption areaConsumption = powerConsumptionService.getAreaStageCumulativeConsumption(where);
            if (Objects.isNull(areaConsumption)) {
                XxlJobHelper.log("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                log.info("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                continue;
            }
            //补充属性
            areaConsumption.setAreaCode(areaCode);
            areaConsumption.setAreaName(areaName);
            areaConsumption.setCompanyCode(companyCode);
            areaConsumption.setCompanyName(companyName);
            //查询存量数据
            EmsElectricPowerAreaConsumption stockedRec = emsElectricPowerAreaConsumptionService.isStockedRec(areaConsumption);
            if (Objects.nonNull(stockedRec)) {
                areaConsumption.setId(stockedRec.getId());
                areaConsumption.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            emsElectricPowerAreaConsumptionService.save(areaConsumption);

            List<EmsArea> childList = emsArea.getChildList();
            if (!CollectionUtils.isEmpty(childList)) {
                execute(optDTime, companyCode, childList);
            }
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
//            List<Date> stockPendulum = emsElectricPowerAreaConsumptionService.getStockPendulum(busiKey, TemporalGranularityEnum.VD_Quarter);
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

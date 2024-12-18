package com.jeesite.modules.job.task.etl.dataclear;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.job.entity.CalculateJobParam;
import com.jeesite.modules.job.task.ems.powercons.*;
import com.jeesite.modules.job.task.ems.timeshare.*;
import com.jeesite.modules.job.task.utils.Calculatehelper;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * 类说明
 * 电表数据采集全量数据生成任务
 * 参数格式  {"companyCodes":[],"appointedTime":"2023-09-07"}
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class FullDoseCalculateTask {
    @Resource
    private DataCleansingTask dataCleansingTask;
    @Resource
    private DeviceCalculateQuarterDataTask deviceCalculateQuarterDataTask;
    @Resource
    private DeviceCalculateHourDataTask deviceCalculateHourDataTask;
    @Resource
    private DeviceCalculateDayDataTask deviceCalculateDayDataTask;
    @Resource
    private DeviceCalculateMonthDataTask deviceCalculateMonthDataTask;
    @Resource
    private DeviceCalculateYearDataTask deviceCalculateYearDataTask;
    @Resource
    private TimeShareCalculateDayDataTask timeShareCalculateDayDataTask;
    @Resource
    private TimeShareCalculateMonthDataTask timeShareCalculateMonthDataTask;
    @Resource
    private TimeShareCalculateYearDataTask timeShareCalculateYearDataTask;
    @Resource
    private TimeShareCalculateDayDeviceRunningTimeTask timeShareCalculateDayDeviceRunningTimeTask;
    @Resource
    private TimeShareCalculateMonthDeviceRunningTimeTask timeShareCalculateMonthDeviceRunningTimeTask;
    @Resource
    private TimeShareCalculateDayOfficeDataTask timeShareCalculateDayOfficeDataTask;
    @Resource
    private TimeShareCalculateMonthOfficeDataTask timeShareCalculateMonthOfficeDataTask;
    @Resource
    private AreaCalculateQuarterDataTask areaCalculateQuarterDataTask;
    @Resource
    private AreaCalculateHourDataTask areaCalculateHourDataTask;
    @Resource
    private AreaCalculateDayDataTask areaCalculateDayDataTask;
    @Resource
    private AreaCalculateMonthDataTask areaCalculateMonthDataTask;
    @Resource
    private AreaCalculateYearDataTask areaCalculateYearDataTask;
    @Resource
    private TimeShareCalculateYearDeviceRunningTimeTask timeShareCalculateYearDeviceRunningTimeTask;
    @Resource
    private TimeShareCalculateAreaDayDataTask timeShareCalculateAreaDayDataTask;
    @Resource
    private TimeShareCalculateAreaMonthDataTask timeShareCalculateAreaMonthDataTask;
    @Resource
    private RedisService redisService;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(readOnly = false)
    public void execute(String jobParam) throws ParseException {
        XxlJobHelper.log("######电表数据采集全量数据生成任务【开始】######");
        log.info("######电表数据采集全量数据生成任务【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if (StringUtils.isEmpty(jobParam)) {
            XxlJobHelper.log("执行参数为空，终止任务");
            log.info("执行参数为空，终止任务");
            return;
        }

        CalculateJobParam calculateJobParam = Calculatehelper.parseJobParam(jobParam);
        String appointedTime = calculateJobParam.getAppointedTime();
        List<String> companyCodes = calculateJobParam.getCompanyCodes();
        XxlJobHelper.log("当前业务时间=》{}，当前指定公司=》{}", appointedTime, JSONObject.toJSONString(companyCodes));
        log.info("当前业务时间=》{}，当前指定公司=》{}", appointedTime, JSONObject.toJSONString(companyCodes));

        if(StringUtils.isEmpty(appointedTime)){
            XxlJobHelper.log("执行时间参数为空，终止任务");
            log.info("执行时间参数为空，终止任务");
            return;
        }

        int limit = 2;//当前业务时间往后冗余生成的天数（包含当前）

        //删除时刻表的key
        String tmpAppointedTime2 = appointedTime;
        for (int i = 0; i < limit; i++) {
            tmpAppointedTime2 = DateUtils.formatDate(DateUtils.calculateDay(DateUtils.parseDate(tmpAppointedTime2), i));
            //清除缓存
            redisService.del(
                    dataCleansingTask.getKEY_etlSchedule(tmpAppointedTime2),
                    deviceCalculateQuarterDataTask.getKEY_etlSchedule(tmpAppointedTime2),
                    deviceCalculateHourDataTask.getKEY_etlSchedule(tmpAppointedTime2),
                    areaCalculateQuarterDataTask.getKEY_etlSchedule(tmpAppointedTime2),
                    areaCalculateHourDataTask.getKEY_etlSchedule(tmpAppointedTime2)
            );
        }

        /**
         * 注意：以下定时任务执行顺序不可随意修改，部分任务执行需要依赖其他任务的执行结果
         * 注意：以下定时任务执行顺序不可随意修改，部分任务执行需要依赖其他任务的执行结果
         * 注意：以下定时任务执行顺序不可随意修改，部分任务执行需要依赖其他任务的执行结果
         */
        Long execute;

        /**************************数据清洗任务**********************/
        String tmpAppointedTime0 = appointedTime;
        for (int i = 0; i < limit; i++) {
            //每次跑全量数据，往后跑一天，否则：在下一天数据缺失的情况下，能耗数据会少15分钟的数据
            try {
                tmpAppointedTime0 = DateUtils.formatDate(DateUtils.calculateDay(DateUtils.parseDate(tmpAppointedTime0), i));
                do {
                    //电表数据清洗处理定时任务，依赖 原始采集数据
                    execute = dataCleansingTask.execute(JSONObject.toJSONString(new CalculateJobParam(companyCodes, tmpAppointedTime0)));
                } while (
                        Double.compare(-1d, execute) != 0
                );
            } catch (Exception ex) {
                log.error("电表数据采集全量数据生成任务【异常】=》", ex.getMessage(), ex);
                throw ex;
            }
        }

        /**************************电表电耗计算任务**********************/
        String tmpAppointedTime1 = appointedTime;
        for (int i = 0; i < limit; i++) {
            try {
                tmpAppointedTime1 = DateUtils.formatDate(DateUtils.calculateDay(DateUtils.parseDate(tmpAppointedTime1), i));
                do {
                    //电表15分钟电耗计算定时任务，EmsElectricPowerConsumption 依赖 EmsMeterCollectedData  VD_Quarter
                    execute = deviceCalculateQuarterDataTask.execute(JSONObject.toJSONString(new CalculateJobParam(companyCodes, tmpAppointedTime1)));
                } while (
                        Double.compare(-1d, execute) != 0
                );
            } catch (Exception ex) {
                log.error("电表数据采集全量数据生成任务【异常】=》", ex.getMessage(), ex);
                throw ex;
            }
        }
        do {
            //电表1小时电耗计算定时任务，EmsElectricPowerConsumption 依赖 EmsMeterCollectedData  VD_Quarter
            execute = deviceCalculateHourDataTask.execute(jobParam, true);
        } while (
                Double.compare(-1d, execute) != 0
        );
        //电表每日电耗计算定时任务，EmsElectricPowerConsumption 依赖 EmsMeterCollectedData  VD_Quarter
        deviceCalculateDayDataTask.execute(jobParam);
        //电表每月电耗计算定时任务，EmsElectricPowerConsumptionStatistics 依赖 EmsElectricPowerConsumption  VD_Day
        deviceCalculateMonthDataTask.execute(jobParam);
        //电表每年电耗计算定时任务，EmsElectricPowerConsumptionStatistics 依赖 EmsElectricPowerConsumptionStatistics  VD_Month
        deviceCalculateYearDataTask.execute(jobParam);

        /**************************区域电耗计算任务**********************/
        do {
            //区域15分钟电耗计算定时任务，EmsElectricPowerAreaConsumption 依赖 EmsElectricPowerConsumption  VD_Quarter
            execute = areaCalculateQuarterDataTask.execute(jobParam);
        } while (
                Double.compare(-1d, execute) != 0
        );
        do {
            //区域1小时电耗计算定时任务，EmsElectricPowerAreaConsumption 依赖 EmsElectricPowerConsumption  VD_Hour
            execute = areaCalculateHourDataTask.execute(jobParam, true);
        } while (
                Double.compare(-1d, execute) != 0
        );
        //区域每日电耗计算定时任务，EmsElectricPowerAreaConsumption 依赖 EmsElectricPowerConsumption  VD_Day
        areaCalculateDayDataTask.execute(jobParam);
        //区域每月电耗计算定时任务，EmsElectricPowerAreaConsumptionStatistics 依赖 EmsElectricPowerConsumptionStatistics  VD_Month
        areaCalculateMonthDataTask.execute(jobParam);
        //区域每年电耗计算定时任务，EmsElectricPowerAreaConsumptionStatistics 依赖 EmsElectricPowerConsumptionStatistics  VD_Year
        areaCalculateYearDataTask.execute(jobParam);

        /**************************电表分时电耗计算任务**********************/
        //电表每日分时电耗计算定时任务，EmsTimeSharePowerConsumption 依赖 EmsMeterCollectedData  VD_Quarter
        timeShareCalculateDayDataTask.execute(jobParam);
        //电表每月分时电耗计算定时任务，EmsTimeSharePowerConsumptionStatistics 依赖 EmsTimeSharePowerConsumption  VD_Day
        timeShareCalculateMonthDataTask.execute(jobParam);
        //电表年分时电耗计算定时任务，EmsTimeSharePowerConsumptionStatistics 依赖 EmsTimeSharePowerConsumptionStatistics  VD_Month
        timeShareCalculateYearDataTask.execute(jobParam);

        /**************************区域分时电耗计算任务**********************/
        //区域每日分时电耗计算定时任务，EmsTimeShareAreaPowerConsumption 依赖 EmsTimeSharePowerConsumption  VD_Day
        timeShareCalculateAreaDayDataTask.execute(jobParam);
        //区域每月分时电耗计算定时任务，EmsTimeShareAreaPowerConsumptionStatistics 依赖 EmsTimeSharePowerConsumptionStatistics  VD_Month
        timeShareCalculateAreaMonthDataTask.execute(jobParam);

        /**************************设备分时运行时长统计任务**********************/
        //设备日分时运行时长统计，EmsTimeShareDeviceRuntime 依赖 EmsElectricPowerConsumption  VD_Quarter
        timeShareCalculateDayDeviceRunningTimeTask.execute(jobParam);
        //设备月分时运行时长统计，EmsTimeShareDeviceRuntimeStatistics 依赖 EmsTimeShareDeviceRuntime  VD_Day
        timeShareCalculateMonthDeviceRunningTimeTask.execute(jobParam);
        //设备年分时运行时长统计，EmsTimeShareDeviceRuntimeStatistics 依赖 EmsTimeShareDeviceRuntimeStatistics  VD_Month
        timeShareCalculateYearDeviceRunningTimeTask.execute(jobParam);

        /**************************部门分时电耗计算任务**********************/
        //部门每日分时电耗计算定时任务，EmsTimeShareOfficeConsumption 依赖 EmsTimeSharePowerConsumption  VD_Day
        timeShareCalculateDayOfficeDataTask.execute(jobParam);
        //部门每月分时电耗计算定时任务，EmsTimeShareOfficeConsumptionStatistics 依赖 EmsTimeShareOfficeConsumption  VD_Day
        timeShareCalculateMonthOfficeDataTask.execute(jobParam);

        stopWatch.stop();
        XxlJobHelper.log("######电表数据采集全量数据生成任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######电表数据采集全量数据生成任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }
}

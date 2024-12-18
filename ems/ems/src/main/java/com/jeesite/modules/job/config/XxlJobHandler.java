package com.jeesite.modules.job.config;

import com.jeesite.modules.ems.service.EmsDataSyncService;
import com.jeesite.modules.job.task.ems.event.DeviceCurrentPowerSyncTask;
import com.jeesite.modules.job.task.ems.event.EventEquipmentOffLineTask;
import com.jeesite.modules.job.task.ems.powercons.*;
import com.jeesite.modules.job.task.ems.timeshare.*;
import com.jeesite.modules.job.task.etl.dataclear.DataCleansingTask;
import com.jeesite.modules.job.task.etl.dataclear.FullDoseCalculateTask;
import com.jeesite.modules.sys.utils.CorpUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * XxlJob开发示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、任务开发：在Spring Bean实例中，开发Job方法；
 * 2、注解配置：为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobHelper.log" 打印执行日志；
 * 4、任务结果：默认任务结果为 "成功" 状态，不需要主动设置；如有诉求，比如设置任务结果为失败，可以通过 "XxlJobHelper.handleFail/handleSuccess" 自主设置任务结果；
 *
 * @author xuxueli 2019-12-11 21:52:51
 */
@Component
public class XxlJobHandler {

    private static Logger logger = LoggerFactory.getLogger(XxlJobHandler.class);

    @Resource
    private FullDoseCalculateTask fullDoseCalculateTask;

    /**
     * 电表数据采集全量数据生成任务
     */
    @XxlJob("fullDoseCalculateTask")
    public void fullDoseCalculateTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表数据采集全量数据生成任务");
        String jobParam = XxlJobHelper.getJobParam();
        fullDoseCalculateTask.execute(jobParam);
    }

    @Resource
    private DataCleansingTask dataCleansingTask;

    /**
     * 电表数据清洗处理定时任务
     */
    @XxlJob("dataCleansingTask")
    public void dataCleansingTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表数据清洗处理定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        dataCleansingTask.execute(jobParam);
    }

    @Resource
    private DeviceCalculateQuarterDataTask deviceCalculateQuarterDataTask;

    /**
     * 电表15分钟电耗计算定时任务
     */
    @XxlJob("deviceCalculateQuarterDataTask")
    public void deviceCalculateQuarterDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表15分钟电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        deviceCalculateQuarterDataTask.execute(jobParam);
    }

    @Resource
    private DeviceCalculateHourDataTask deviceCalculateHourDataTask;

    /**
     * 电表1小时电耗计算定时任务
     */
    @XxlJob("deviceCalculateHourDataTask")
    public void deviceCalculateHourDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表1小时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        deviceCalculateHourDataTask.execute(jobParam, false);
    }

    @Resource
    private DeviceCalculateDayDataTask deviceCalculateDayDataTask;

    /**
     * 电表每日电耗计算定时任务
     */
    @XxlJob("deviceCalculateDayDataTask")
    public void deviceCalculateDayDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表每日电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        deviceCalculateDayDataTask.execute(jobParam);
    }

    @Resource
    private DeviceCalculateMonthDataTask deviceCalculateMonthDataTask;

    /**
     * 电表每日电耗计算定时任务
     */
    @XxlJob("deviceCalculateMonthDataTask")
    public void deviceCalculateMonthDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表每日电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        deviceCalculateMonthDataTask.execute(jobParam);
    }

    @Resource
    private DeviceCalculateYearDataTask deviceCalculateYearDataTask;

    /**
     * 电表每日电耗计算定时任务
     */
    @XxlJob("deviceCalculateYearDataTask")
    public void deviceCalculateYearDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表每日电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        deviceCalculateYearDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateDayDataTask timeShareCalculateDayDataTask;

    /**
     * 电表每日分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateDayDataTask")
    public void timeShareCalculateDayDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表每日分时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateDayDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateMonthDataTask timeShareCalculateMonthDataTask;

    /**
     * 电表每月分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateMonthDataTask")
    public void timeShareCalculateMonthDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表每月分时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateMonthDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateYearDataTask timeShareCalculateYearDataTask;

    /**
     * 电表年分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateYearDataTask")
    public void timeShareCalculateYearDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 电表年分时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateYearDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateDayDeviceRunningTimeTask timeShareCalculateDayDeviceRunningTimeTask;

    /**
     * 设备日分时运行时长统计
     */
    @XxlJob("timeShareCalculateDayDeviceRunningTimeTask")
    public void timeShareCalculateDayDeviceRunningTimeTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 设备日分时运行时长统计.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateDayDeviceRunningTimeTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateMonthDeviceRunningTimeTask timeShareCalculateMonthDeviceRunningTimeTask;

    /**
     * 设备月分时运行时长统计
     */
    @XxlJob("timeShareCalculateMonthDeviceRunningTimeTask")
    public void timeShareCalculateMonthDeviceRunningTimeTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 设备月分时运行时长统计.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateMonthDeviceRunningTimeTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateDayOfficeDataTask timeShareCalculateDayOfficeDataTask;

    /**
     * 部门每日分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateDayOfficeDataTask")
    public void timeShareCalculateDayOfficeDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 部门每日分时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateDayOfficeDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateMonthOfficeDataTask timeShareCalculateMonthOfficeDataTask;

    /**
     * 部门每月分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateMonthOfficeDataTask")
    public void timeShareCalculateMonthOfficeDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 部门每月分时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateMonthOfficeDataTask.execute(jobParam);
    }

    @Resource
    private AreaCalculateQuarterDataTask areaCalculateQuarterDataTask;

    /**
     * 区域15分钟电耗计算定时任务
     */
    @XxlJob("areaCalculateQuarterDataTask")
    public void areaCalculateQuarterDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域15分钟电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        areaCalculateQuarterDataTask.execute(jobParam);
    }

    @Resource
    private AreaCalculateHourDataTask areaCalculateHourDataTask;

    /**
     * 区域1小时电耗计算定时任务
     */
    @XxlJob("areaCalculateHourDataTask")
    public void areaCalculateHourDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域1小时电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        areaCalculateHourDataTask.execute(jobParam, false);
    }

    @Resource
    private AreaCalculateDayDataTask areaCalculateDayDataTask;

    /**
     * 区域每日电耗计算定时任务
     */
    @XxlJob("areaCalculateDayDataTask")
    public void areaCalculateDayDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域每日电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        areaCalculateDayDataTask.execute(jobParam);
    }

    @Resource
    private AreaCalculateMonthDataTask areaCalculateMonthDataTask;

    /**
     * 区域每月电耗计算定时任务
     */
    @XxlJob("areaCalculateMonthDataTask")
    public void areaCalculateMonthDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域每月电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        areaCalculateMonthDataTask.execute(jobParam);
    }

    @Resource
    private AreaCalculateYearDataTask areaCalculateYearDataTask;

    /**
     * 区域每年电耗计算定时任务
     */
    @XxlJob("areaCalculateYearDataTask")
    public void areaCalculateYearDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域每年电耗计算定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        areaCalculateYearDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateYearDeviceRunningTimeTask timeShareCalculateYearDeviceRunningTimeTask;

    /**
     * 设备每年电耗计算定时任务年统计测试
     */
    @XxlJob("timeShareCalculateYearDeviceRunningTimeTask")
    public void timeShareCalculateYearDeviceRunningTimeTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 设备每年电耗计算定时任务年统计定时任务.");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateYearDeviceRunningTimeTask.execute(jobParam);
    }

    @Resource
    private DeviceCurrentPowerSyncTask deviceCurrentPowerSyncTask;

    /**
     * 设备实时有功 无功 电流 电压 统计定时任务
     */
    @XxlJob("deviceCurrentPowerSyncTask")
    public void deviceCurrentPowerSyncTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 设备实时有功 无功 电流 电压 统计定时任务");
        String jobParam = XxlJobHelper.getJobParam();
        deviceCurrentPowerSyncTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateAreaDayDataTask timeShareCalculateAreaDayDataTask;

    /**
     * 区域每日分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateAreaDayDataTask")
    public void timeShareCalculateAreaDayDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域每日分时电耗计算定时任务");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateAreaDayDataTask.execute(jobParam);
    }

    @Resource
    private TimeShareCalculateAreaMonthDataTask timeShareCalculateAreaMonthDataTask;

    /**
     * 区域每月分时电耗计算定时任务
     */
    @XxlJob("timeShareCalculateAreaMonthDataTask")
    public void timeShareCalculateAreaMonthDataTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 区域每月分时电耗计算定时任务");
        String jobParam = XxlJobHelper.getJobParam();
        timeShareCalculateAreaMonthDataTask.execute(jobParam);
    }


    @Resource
    private EventEquipmentOffLineTask eventEquipmentOffLineTask;
    /**
     * 设备终端离线告警事件生成定时任务
     */
    @XxlJob("eventEquipmentOffLineTask")
    public void eventEquipmentOffLineTask() throws Exception {
        XxlJobHelper.log("XXL-JOB, 设备终端离线告警事件生成定时任务");
        String jobParam = XxlJobHelper.getJobParam();
        eventEquipmentOffLineTask.execute(jobParam);
    }

    @Resource
    private EmsDataSyncService emsDataSyncService;

    @XxlJob("resultDataSyncTask")
    public void resultDataSyncTask() throws Exception {
        CorpUtils.setCurrentCorpCode("SXJT", "山西建投");
        XxlJobHelper.log("能源数据统计结果同步开始");
        emsDataSyncService.syncTotal();
        XxlJobHelper.log("总用电曲趋势统计同步成功！");
        emsDataSyncService.syncTodayElectricity();
        XxlJobHelper.log("用电量统计同步成功！");
        emsDataSyncService.synCconsumptionRanking();
        XxlJobHelper.log("用电量排行统计同步成功！");
        emsDataSyncService.syncTimeShareConsumptionTrend();
        XxlJobHelper.log("尖峰平谷用电趋势统计同步成功！");
        emsDataSyncService.equipmentOnlineStatistics();
        XxlJobHelper.log("设备在线情况统计同步成功！");
    }

}

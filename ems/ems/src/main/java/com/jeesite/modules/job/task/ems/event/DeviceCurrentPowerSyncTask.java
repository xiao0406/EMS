package com.jeesite.modules.job.task.ems.event;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.cecec.api.redis.RedisKeyUtil;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionService;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类说明
 * 设备实时有功 无功 电流 电压 统计定时任务
 *
 * @author 吴鹏
 * @date 2023/7/21
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class DeviceCurrentPowerSyncTask {
    @Resource
    private EmsMeterService emsMeterService;

    @Resource
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;
    @Resource
    private RedisService redisService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######设备实时有功 无功 电流 电压 统计定时任务【开始】######");
        log.info("######设备实时有功 无功 电流 电压 统计定时任务【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // step 1:获取所有的设备ID
        List<String> devices = emsMeterService.findAllMeterCode();
        // step 2:获取设备的最新值
        if(CollectionUtils.isEmpty(devices)){
            XxlJobHelper.log("设备信息为空，终止任务");
            log.info("设备信息为空，终止任务");
            return;
        }
        for(String device : devices){
            EmsElectricPowerConsumption param = new EmsElectricPowerConsumption();
            param.setDeviceId(device);
            param.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
            param.setDataDate(DateUtils.parseDate(DateUtils.formatDate(new Date())));
            EmsElectricPowerConsumption emsElectricPowerConsumption = emsElectricPowerConsumptionService.getLastRecord(param);
            if(!ObjectUtil.isEmpty(emsElectricPowerConsumption)){
                redisService.set(RedisKeyUtil.deviceCurrentPower(device), JSON.toJSONString(emsElectricPowerConsumption),5 * 60);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######设备实时有功 无功 电流 电压 统计定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######设备实时有功 无功 电流 电压 统计定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }
}

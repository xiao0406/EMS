package com.jeesite.modules.job.task.ems.event;

import com.cecec.api.redis.RedisKeyUtil;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.constant.DefaultValues;
import com.jeesite.modules.ems.entity.EmsAlarmEvent;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsTerminal;
import com.jeesite.modules.ems.entity.EmsTerminalMeter;
import com.jeesite.modules.ems.entity.enums.EmsEventTypeEnum;
import com.jeesite.modules.ems.service.*;
import com.jeesite.modules.job.task.utils.Calculatehelper;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类说明
 * 设备终端离线告警事件生成定时任务
 *
 * @author 吴鹏
 * @date 2023/8/09
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class EventEquipmentOffLineTask {
    private static final String DEVICE_STRING = "终端：";


    @Resource
    private EmsTerminalService emsTerminalService;

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @Resource
    private EmsAlarmEventService emsAlarmEventService;

    @Resource
    private EmsMeterService emsMeterService;

    @Resource
    private EmsTerminalMeterService emsTerminalMeterService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) {
        XxlJobHelper.log("######设备终端离线告警事件生成定时任务【开始】######");
        log.info("######设备终端离线告警事件生成定时任务【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User user = new User();
        user.setLoginCode("virtual_gd_job_admin");
        User jobAdmin = userService.getByLoginCode(user);
        CorpUtils.setCurrentCorpCode(jobAdmin.getCorpCode(), jobAdmin.getCorpName());
        String execDate = Calculatehelper.getExecDate(jobParam);
        XxlJobHelper.log("当前业务时间=》{}", execDate);
        log.info("当前业务时间=》{}", execDate);

        List<EmsTerminalMeter> terminalMeters = emsTerminalMeterService.findList(new EmsTerminalMeter());
        Map<String, String> terminalMeterMap = terminalMeters.stream().collect(Collectors.toMap(EmsTerminalMeter::getTerminalCode, EmsTerminalMeter::getMeterCode, (key1, key2) -> key2));
        // step 1:终端离线
        // 缓存查询终端，如果离线则生成告警记录 dmp:terminalCode:861241058527613:status
        List<EmsTerminal> terminalsList = emsTerminalService.findAllTerminals(new EmsTerminal());
        Map<String,String> companyMap;
        Date date = new Date();
        if(!CollectionUtils.isEmpty(terminalsList)){
            companyMap = getCompanyInfo(terminalsList);
            List<String> terminalKeys = RedisKeyUtil.terminalStatus(terminalsList.stream().map(EmsTerminal::getTerminalCode).collect(Collectors.toList()));
            //从redis中查询是否存在缓存，若存在则，在线，若不存在，则不在线
            for (String terminalKey : terminalKeys) {
                if (!redisService.hasKey(terminalKey)) {
                    String deviceId = terminalKey.split(":")[2];
                    log.info(DEVICE_STRING + deviceId + " 离线");
                    // 判断15分钟内有无报警记录，如果没有才生成
                    List<EmsAlarmEvent> events = emsAlarmEventService.findListByMin(deviceId, DateUtils.calculateMinute(new Date(), -15));
                    if (CollectionUtils.isEmpty(events)) {
                        // 查询设备信息
                        EmsTerminal terminal = new EmsTerminal();
                        terminal.setTerminalCode(deviceId);
                        List<EmsTerminal> terminals = emsTerminalService.findAllTerminals(terminal);
                        if (CollectionUtils.isEmpty(terminals)) {
                            continue;
                        }
                        terminal = terminals.get(0);
                        EmsAlarmEvent event = new EmsAlarmEvent();
                        event.setDataDateTime(date);
                        event.setDataDate(DateUtils.parseDate(DateUtils.formatDate(date, "yyyy-MM-dd")));
                        event.setDataTime(DateUtils.formatDate(date, "hh:mm:ss"));
                        event.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
                        event.setEventType(EmsEventTypeEnum.ET_TERMINAL_OFF_LINE.getCode());
                        event.setEventTypeMsg(EmsEventTypeEnum.ET_TERMINAL_OFF_LINE.getMsg());
                        event.setEventLevel(DefaultValues.STRING_TWO);
                        // 终端ID转换为设备ID
                        event.setDeviceId(terminalMeterMap.get(terminal.getTerminalCode()));
                        event.setDeviceName(terminal.getTerminalName());
                        event.setEventContent("设备:" + terminal.getTerminalName() + ",已离线,离线时间:" + DateUtils.formatDateTime(date));
                        event.setCompanyCode(companyMap.get(deviceId).split(",")[0]);
                        event.setCompanyName(companyMap.get(deviceId).split(",")[1]);
                        event.setIsNewRecord(true);
                        emsAlarmEventService.save(event);
                    }
                }
            }
        }


        // step 2:数据丢失
        // 找出在ems_meter_collected_data电表采集数据表中15分钟内状态为1数据丢失的数据 且在ems_alarm_event事件表中15分钟未生成的数据
        List<String> loseList = emsAlarmEventService.getloseDataBy15Min(DateUtils.calculateMinute(date, -15));
        if (!CollectionUtils.isEmpty(loseList)) {
            List<EmsMeter> meters = emsMeterService.getMertInfoByCodeList(loseList);
            Map<String,String> meterCompanyMap = getMeterCompanyInfo(meters);
            loseList.forEach(en -> {
                EmsAlarmEvent event = new EmsAlarmEvent();
                event.setDataDateTime(date);
                event.setDataDate(DateUtils.parseDate(DateUtils.formatDate(date, "yyyy-MM-dd")));
                event.setDataTime(DateUtils.formatDate(date, "hh:mm:ss"));
                event.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
                event.setEventType(EmsEventTypeEnum.ET_LOSE_DATA.getCode());
                event.setEventTypeMsg(EmsEventTypeEnum.ET_LOSE_DATA.getMsg());
                event.setEventLevel(DefaultValues.STRING_TWO);
                event.setDeviceId(en);
                event.setDeviceName(meterCompanyMap.get(en).split(",")[0]);
                event.setEventContent("设备:" + event.getDeviceName() + ",数据异常,发生时间:" + DateUtils.formatDateTime(date));
                event.setCompanyCode(meterCompanyMap.get(en).split(",")[1]);
                event.setCompanyName(meterCompanyMap.get(en).split(",")[2]);
                event.setIsNewRecord(true);
                emsAlarmEventService.save(event);
            });
        }
        stopWatch.stop();
        XxlJobHelper.log("######设备终端离线告警事件生成定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######设备终端离线告警事件生成定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }


    /**
     * 组装电表名称，公司信息map
     * @param meters
     * @return
     */
    private Map<String, String> getMeterCompanyInfo(List<EmsMeter> meters) {
        Map<String, String> map = new HashMap<>();
        for(EmsMeter meter : meters){
            map.put(meter.getMeterCode(),meter.getMeterName()+","+meter.getCompanyCode() +","+meter.getCompanyName());
        }
        return map;
    }

    /**
     * 组装公司信息map
     * @param terminalsList
     * @return
     */
    private Map<String, String> getCompanyInfo(List<EmsTerminal> terminalsList) {
        Map<String, String> map = new HashMap<>();
        for(EmsTerminal terminal : terminalsList){
            map.put(terminal.getTerminalCode(),terminal.getCompanyCode() +","+terminal.getCompanyName());
        }
        return map;
    }

}

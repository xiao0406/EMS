package com.jeesite.modules.dcs.base.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.dcs.base.entity.DeviceAlarm;
import com.jeesite.modules.dcs.base.service.DeviceAlarmService;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;

import java.util.List;

/**
 * 设备报警表Controller
 * @author ds
 * @version 2023-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/dcs/DeviceAlarm")
@Api(value = "设备报警表接口", tags = "设备报警表")
public class DeviceAlarmController extends BaseController {

    @Resource
    private DeviceAlarmService DeviceAlarmService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public DeviceAlarm get(String id, boolean isNewRecord) {
        return DeviceAlarmService.get(id, isNewRecord);
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    @ApiOperation(value = "查询列表数据", notes = "查询列表数据")
    public Page<DeviceAlarm> listData(DeviceAlarm deviceAlarm, HttpServletRequest request, HttpServletResponse response) {
        deviceAlarm.setPage(new Page<>(request, response));
        return DeviceAlarmService.findPage(deviceAlarm);
    }

    /**
     * 查询最新报警数据
     */
    @RequestMapping(value = "findDeviceAlarm")
    @ResponseBody
    @ApiOperation(value = "查询最新报警数据", notes = "查询最新报警数据")
    public List<DeviceAlarm> findDeviceAlarm(String deviceCode){
        return DeviceAlarmService.findDeviceAlarm(deviceCode);
    }

}
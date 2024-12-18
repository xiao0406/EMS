package com.cscec.dmp.http.imir.yh.controller;

import com.cecec.api.redis.RedisKeyUtil;
import com.cscec.common.imir.dto.state.DeviceStatus;
import com.cscec.common.imir.dto.state.WeldDeviceStatus;
import com.cscec.common.imir.vo.ComponentWeldingData;
import com.cscec.common.imir.vo.ComponentWeldingParam;
import com.cscec.common.imir.vo.state.ComponentWeldingStatus;
import com.cscec.dmp.http.imir.yh.service.YhCwProdService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("dmp/componentWelding")
public class ComponentWeldingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final YhCwProdService yhCwProdService;

    public ComponentWeldingController(RedisTemplate<String, Object> redisTemplate, YhCwProdService yhCwProdService) {
        this.redisTemplate = redisTemplate;
        this.yhCwProdService = yhCwProdService;
    }

    @RequestMapping("/deviceStatus")
    private ResponseEntity<String> deviceStatus(@RequestBody ComponentWeldingStatus status) {
        DeviceStatus<WeldDeviceStatus> ds = new DeviceStatus<>(status.getDeviceCode(), status.getRunStatus(), classChange(status));
        redisTemplate.opsForValue().set(RedisKeyUtil.deviceStatus(ds.getDeviceCode()), ds,10, TimeUnit.SECONDS);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * 数据类型转换
     */
    public WeldDeviceStatus classChange(ComponentWeldingStatus s){
        WeldDeviceStatus wds = new WeldDeviceStatus();
        wds.setRunMode(s.getRunMode());
        wds.setElectrical(BigDecimal.valueOf(s.getElectrical()));
        wds.setVoltage(BigDecimal.valueOf(s.getVoltage()));
        return wds;
    }

    @RequestMapping("/weldParam")
    private ResponseEntity<String> weldParam(@RequestBody ComponentWeldingParam param) {
        redisTemplate.opsForValue().set(RedisKeyUtil.weldParam(param.getDeviceCode()), param);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("/deviceOperate")
    private ResponseEntity<String> deviceOperate(@RequestBody ComponentWeldingData data) {
        yhCwProdService.saveDate(data);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

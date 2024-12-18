package com.cscec.dmp.http.imir.sx.controller;

import com.cecec.api.redis.RedisKeyUtil;
import com.cscec.common.imir.dto.state.DeviceStatus;
import com.cscec.dmp.http.imir.sx.service.SxGcProdService;
import com.cscec.common.imir.vo.GrooveCuttingData;
import com.cscec.common.imir.vo.state.GrooveCuttingStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/cutting/unit")
public class GrooveCuttingController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SxGcProdService sxGcProdService;

    /**
     * 设备状态
     */
    @RequestMapping("/deviceStatus")
    private ResponseEntity<String> deviceStatus(@RequestBody GrooveCuttingStatus status) {
        DeviceStatus<Object> ds = new DeviceStatus<>(status.getDeviceNumber(), status.getUnitStatus());
        redisTemplate.opsForValue().set(RedisKeyUtil.deviceStatus(ds.getDeviceCode()), ds,5, TimeUnit.SECONDS);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * 生产数据
     */
    @RequestMapping("/saveData")
    private ResponseEntity<String> saveData(@RequestBody GrooveCuttingData data) {
        sxGcProdService.saveDate(data);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

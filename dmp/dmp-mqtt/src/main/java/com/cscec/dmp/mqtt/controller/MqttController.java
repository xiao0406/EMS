package com.cscec.dmp.mqtt.controller;

import com.cscec.dmp.mqtt.service.MqttProtocolDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 类说明
 *
 * @author 李鹏
 * @date 2023/4/6
 */
@Slf4j
@Controller
@RequestMapping(value = "/mqtt")
public class MqttController {
    @Resource
    private MqttProtocolDataService mqttProtocolDataService;

    @PostMapping("/save/{companyAlias}/{terminalCode}")
    public ResponseEntity<String> save(@PathVariable("companyAlias") String companyAlias,
                                     @PathVariable("terminalCode") String terminalCode,
                                     @RequestBody String json){
        if(!StringUtils.hasText(terminalCode) || !StringUtils.hasText(companyAlias) ){
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        String topic = "CSCEC-DMP/" + companyAlias + "/" + terminalCode;
        mqttProtocolDataService.handleMessage(topic, terminalCode, json);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


//    @Autowired
//    private MqttGatewayComponent mqttGatewayComponent;
//
//    @RequestMapping("/publish")
//    @ResponseBody
//    private ResponseEntity<String> publish(String topic, String payload) {
//        String data = "我是springboot发送的数据";
//        mqttGatewayComponent.sendToMqtt("test", data);
//        return new ResponseEntity<>("OK", HttpStatus.OK);
//    }
//
//    /**
//     * 动态增加主题
//     *
//     * @param
//     * @param
//     */
//    @ResponseBody
//    @RequestMapping("/sendToTopic")
//    private ResponseEntity<String> sendToTopic() {
//        String topic = "sharjeck/ai/test/out";
//        String data = "这是出的主题";
//        mqttGatewayComponent.sendToMqtt(data, topic);
//        return new ResponseEntity<>("OK", HttpStatus.OK);
//    }

}

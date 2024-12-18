package com.cscec.dmp.http.log.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cscec.dmp.http.log.entity.HttpProtocolData;
import com.cscec.dmp.http.log.mapper.HttpProtocolDataMapper;
import com.cscec.dmp.http.log.service.HttpProtocolDataService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@DS("dmp")
@Service
public class HttpProtocolDataServiceImpl extends ServiceImpl<HttpProtocolDataMapper, HttpProtocolData>
        implements HttpProtocolDataService {

    @Override
    public void saveLog(Object payload) {
        HttpProtocolData.HttpProtocolDataBuilder dataBuilder = HttpProtocolData.builder()
                .id(IdUtil.getSnowflake().nextIdStr())
                .body(JSON.toJSONString(payload))
                .ts(LocalDateTime.now());
        baseMapper.insert(dataBuilder.build());
    }
}


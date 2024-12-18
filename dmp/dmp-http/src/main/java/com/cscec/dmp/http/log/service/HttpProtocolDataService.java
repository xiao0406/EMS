package com.cscec.dmp.http.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cscec.dmp.http.log.entity.HttpProtocolData;


/**
 * @author duans
 * @description 针对表【dmp_http_data】的数据库操作Service
 * @createDate 2023-06-21 16:02:47
 */

public interface HttpProtocolDataService extends IService<HttpProtocolData> {

    void saveLog(Object payload);
}


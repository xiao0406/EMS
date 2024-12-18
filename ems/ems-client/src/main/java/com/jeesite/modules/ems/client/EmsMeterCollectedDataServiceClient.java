package com.jeesite.modules.ems.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.ems.api.EmsMeterCollectedDataServiceApi;

/**
 * 电表采集数据API
 * @author 李鹏
 * @version 2023-06-08
 */
@FeignClient(name="${service.ems.name}", path="${service.ems.path}")
@ConditionalOnNotCurrentApplication(name="${service.ems.name}")
public interface EmsMeterCollectedDataServiceClient extends EmsMeterCollectedDataServiceApi {
	
}
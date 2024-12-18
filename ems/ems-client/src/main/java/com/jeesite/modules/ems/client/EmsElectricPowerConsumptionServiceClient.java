package com.jeesite.modules.ems.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.ems.api.EmsElectricPowerConsumptionServiceApi;

/**
 * 电耗表API
 * @author 李鹏
 * @version 2023-05-25
 */
@FeignClient(name="${service.ems.name}", path="${service.ems.path}")
@ConditionalOnNotCurrentApplication(name="${service.ems.name}")
public interface EmsElectricPowerConsumptionServiceClient extends EmsElectricPowerConsumptionServiceApi {
	
}
package com.jeesite.modules.ems.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.ems.api.EmsTimeShareConsumptionTrendSummaryServiceApi;

/**
 * 尖峰平谷用电趋势汇总API
 * @author 范富华
 * @version 2024-05-17
 */
@FeignClient(name="${service.ems.name}", path="${service.ems.path}")
@ConditionalOnNotCurrentApplication(name="${service.ems.name}")
public interface EmsTimeShareConsumptionTrendSummaryServiceClient extends EmsTimeShareConsumptionTrendSummaryServiceApi {
	
}
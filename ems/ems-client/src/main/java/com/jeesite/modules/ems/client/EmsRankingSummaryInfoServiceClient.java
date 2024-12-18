package com.jeesite.modules.ems.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.ems.api.EmsRankingSummaryInfoServiceApi;

/**
 * 耗电量排行关联表API
 * @author 范富华
 * @version 2024-05-15
 */
@FeignClient(name="${service.ems.name}", path="${service.ems.path}")
@ConditionalOnNotCurrentApplication(name="${service.ems.name}")
public interface EmsRankingSummaryInfoServiceClient extends EmsRankingSummaryInfoServiceApi {
	
}
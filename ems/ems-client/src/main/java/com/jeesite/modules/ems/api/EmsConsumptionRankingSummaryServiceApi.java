package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsConsumptionRankingSummary;

/**
 * 用电量排行API
 * @author 范富华
 * @version 2024-05-14
 */
@RequestMapping(value = "/inner/api/ems/emsConsumptionRankingSummary")
public interface EmsConsumptionRankingSummaryServiceApi extends CrudServiceRest<EmsConsumptionRankingSummary> {
	
}
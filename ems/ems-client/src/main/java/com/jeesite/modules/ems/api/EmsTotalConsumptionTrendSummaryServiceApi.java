package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsTotalConsumptionTrendSummary;

/**
 * 总用电量汇总API
 * @author 范富华
 * @version 2024-05-13
 */
@RequestMapping(value = "/inner/api/ems/emsTotalConsumptionTrendSummary")
public interface EmsTotalConsumptionTrendSummaryServiceApi extends CrudServiceRest<EmsTotalConsumptionTrendSummary> {
	
}
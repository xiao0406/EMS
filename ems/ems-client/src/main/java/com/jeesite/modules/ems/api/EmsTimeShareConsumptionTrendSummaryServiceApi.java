package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsTimeShareConsumptionTrendSummary;

/**
 * 尖峰平谷用电趋势汇总API
 * @author 范富华
 * @version 2024-05-17
 */
@RequestMapping(value = "/inner/api/ems/emsTimeShareConsumptionTrendSummary")
public interface EmsTimeShareConsumptionTrendSummaryServiceApi extends CrudServiceRest<EmsTimeShareConsumptionTrendSummary> {
	
}
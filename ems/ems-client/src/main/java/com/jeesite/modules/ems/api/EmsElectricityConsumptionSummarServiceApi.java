package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsElectricityConsumptionSummar;

/**
 * 用电量汇总API
 * @author 范富华
 * @version 2024-05-13
 */
@RequestMapping(value = "/inner/api/ems/emsElectricityConsumptionSummar")
public interface EmsElectricityConsumptionSummarServiceApi extends CrudServiceRest<EmsElectricityConsumptionSummar> {
	
}
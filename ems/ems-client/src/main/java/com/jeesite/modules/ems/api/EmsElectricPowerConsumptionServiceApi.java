package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;

/**
 * 电耗表API
 * @author 李鹏
 * @version 2023-05-25
 */
@RequestMapping(value = "/inner/api/ems/emsElectricPowerConsumption")
public interface EmsElectricPowerConsumptionServiceApi extends CrudServiceRest<EmsElectricPowerConsumption> {
	
}
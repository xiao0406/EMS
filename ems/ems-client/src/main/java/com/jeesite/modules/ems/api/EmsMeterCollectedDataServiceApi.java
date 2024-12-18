package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;

/**
 * 电表采集数据API
 * @author 李鹏
 * @version 2023-06-08
 */
@RequestMapping(value = "/inner/api/ems/emsMeterCollectedData")
public interface EmsMeterCollectedDataServiceApi extends CrudServiceRest<EmsMeterCollectedData> {
	
}
package com.jeesite.modules.ems.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsRankingSummaryInfo;

/**
 * 耗电量排行关联表API
 * @author 范富华
 * @version 2024-05-15
 */
@RequestMapping(value = "/inner/api/ems/emsRankingSummaryInfo")
public interface EmsRankingSummaryInfoServiceApi extends CrudServiceRest<EmsRankingSummaryInfo> {
	
}
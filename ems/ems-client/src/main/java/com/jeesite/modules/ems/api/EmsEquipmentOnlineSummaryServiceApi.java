package com.jeesite.modules.ems.api;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.ems.entity.EmsEquipmentOnlineSummary;
import com.jeesite.modules.ems.vo.TimeSharePowerConsumptionVo;
import com.jeesite.modules.ems.vo.TimeSharePowerQueryVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 在线设备统计API
 * @author 范富华
 * @version 2024-05-29
 */
@RequestMapping(value = "/inner/api/ems/emsEquipmentOnlineSummary")
public interface EmsEquipmentOnlineSummaryServiceApi extends CrudServiceRest<EmsEquipmentOnlineSummary> {

    @PostMapping("findTimeShare")
    List<TimeSharePowerConsumptionVo> findTimeShare(@RequestBody TimeSharePowerQueryVo timeSharePowerQueryVo);

}
package com.jeesite.modules.dcs.sx.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.dcs.sx.dto.RunRecordDTO;
import com.jeesite.modules.dcs.sx.dto.WorkEfficiencyDTO;
import com.jeesite.modules.dcs.sx.dto.YieldDTO;
import com.jeesite.modules.dcs.sx.entity.SxGcProd;

import java.time.LocalDate;
import java.util.List;

/**
 * 坡口切割单元生产数据记录表DAO接口
 * @author ds
 * @version 2023-06-29
 */
@MyBatisDao
public interface SxGcProdDao extends CrudDao<SxGcProd> {

    List<RunRecordDTO> deviceRunRecord(String deviceCode, LocalDate startDate, LocalDate endDate);

    List<SxGcProd> deviceRunTime(String deviceCode, LocalDate startDate, LocalDate endDate);

    SxGcProd deviceUtilization(String deviceCode, LocalDate localDate);

    List<WorkEfficiencyDTO> deviceWorkEfficiency(String deviceCode, LocalDate startDate, LocalDate endDate);

    List<SxGcProd> deviceCuttingMeters(String deviceCode, LocalDate startDate, LocalDate endDate);

    List<YieldDTO> deviceYield(String deviceCode, LocalDate startDate, LocalDate endDate);
}
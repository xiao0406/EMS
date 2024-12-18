package com.jeesite.modules.dcs.yh.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.dcs.yh.dto.DeviceCapacityDTO;
import com.jeesite.modules.dcs.yh.entity.YhCwProd;

import java.time.LocalDate;
import java.util.List;

/**
 * 部件焊接单元生产数据记录表DAO接口
 * @author ds
 * @version 2023-06-25
 */
@MyBatisDao
public interface YhCwProdDao extends CrudDao<YhCwProd> {

    void updateData(YhCwProd data);

    List<YhCwProd> deviceRunTime(String deviceCode, LocalDate startDate, LocalDate endDate);

    List<YhCwProd> deviceWire(String deviceCode, LocalDate startDate, LocalDate endDate);

    List<DeviceCapacityDTO> deviceCapacity(String deviceCode, LocalDate startDate, LocalDate endDate);

    YhCwProd deviceUtilization(String deviceCode, LocalDate localDate);
}
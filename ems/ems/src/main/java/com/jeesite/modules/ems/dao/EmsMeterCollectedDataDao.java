package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumption;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.ems.entity.MeterPendulumDisplayEntity;

import java.util.Date;
import java.util.List;

/**
 * 电表采集数据DAO接口
 * @author 李鹏
 * @version 2023-06-08
 */
@MyBatisDao
public interface EmsMeterCollectedDataDao extends CrudDao<EmsMeterCollectedData> {
    /**
     * 获取已经数据清洗完成的时刻表
     * @param params
     * @return
     */
    List<Date> getStockPendulum(EmsMeterCollectedData params);

    /**
     * 是否存在存量数据，若存在则返回
     * @param eepc
     * @return
     */
    EmsMeterCollectedData isStockedRec(EmsMeterCollectedData eepc);

    /**
     * 查询统计基础数据查询（此处的sql一定不要修改，若是需要，复制一份重命名后修改）
     * @param params
     * @return
     */
    List<EmsMeterCollectedData> calculateBaseDataList(EmsMeterCollectedData params);

    /**
     * 查询时点电表示数
     * @param params
     */
    List<MeterPendulumDisplayEntity> meterPendulumDisplay(EmsMeterCollectedData params);

    /**
     * 查询时设备左后一条数据
     */
    EmsMeterCollectedData getDeviceLastData(String deviceId);

//    /**
//     * 查询时点电表示数
//     * @param startTimeQuery
//     */
//    List<MeterPendulumDisplayEntity> meterPendulumDisplay(EmsMeterCollectedData timeQuery);

}
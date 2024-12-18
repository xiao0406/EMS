package com.jeesite.modules.ems.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsMeterOffice;
import com.jeesite.modules.sys.entity.Office;

import java.util.List;

/**
 * 电表设备表DAO接口
 * @author 李鹏
 * @version 2023-06-06
 */
@MyBatisDao
public interface EmsMeterDao extends CrudDao<EmsMeter> {

    EmsMeter findByMeterCode(EmsMeter emsMeter);

    void deleteBatch(EmsMeter emsMeter);

    List<EmsMeterOffice> meterOfficeList(EmsMeterOffice emsMeterOffice);

    /**
     * 根据登录信息查询所属公司车间数据
     * @param companyCode
     * @return
     */
    List<Office> getCompanyWorkshop(String companyCode);

    void saveThreshold(EmsMeter emsMeter);

    List<EmsMeter> findAllList(EmsMeter emsMeter);

    List<String> findCodes(EmsMeter emsMeter);

    List<String> findAllMeterCode();

    List<EmsMeter> getMertInfoByCodeList(List<String> loseList);
}
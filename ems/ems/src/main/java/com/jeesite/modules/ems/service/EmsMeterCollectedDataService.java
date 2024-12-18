package com.jeesite.modules.ems.service;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.api.EmsMeterCollectedDataServiceApi;
import com.jeesite.modules.ems.dao.EmsMeterCollectedDataDao;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.ems.entity.MeterPendulumDisplayEntity;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 电表采集数据Service
 *
 * @author 李鹏
 * @version 2023-06-08
 */
@Service
@RestController
@Transactional(readOnly = true)
public class EmsMeterCollectedDataService extends CrudService<EmsMeterCollectedDataDao, EmsMeterCollectedData>
        implements EmsMeterCollectedDataServiceApi {

    /**
     * 获取单条数据
     *
     * @param emsMeterCollectedData
     * @return
     */
    @Override
    public EmsMeterCollectedData get(EmsMeterCollectedData emsMeterCollectedData) {
        return super.get(emsMeterCollectedData);
    }

    /**
     * 查询分页数据
     *
     * @param emsMeterCollectedData 查询条件
     * @return
     */
    @Override
    public Page<EmsMeterCollectedData> findPage(EmsMeterCollectedData emsMeterCollectedData) {
        return super.findPage(emsMeterCollectedData);
    }

    /**
     * 查询列表数据
     *
     * @param emsMeterCollectedData
     * @return
     */
    @Override
    public List<EmsMeterCollectedData> findList(EmsMeterCollectedData emsMeterCollectedData) {
        return super.findList(emsMeterCollectedData);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsMeterCollectedData
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void save(EmsMeterCollectedData emsMeterCollectedData) {
        super.save(emsMeterCollectedData);
    }

    /**
     * 更新状态
     *
     * @param emsMeterCollectedData
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void updateStatus(EmsMeterCollectedData emsMeterCollectedData) {
        super.updateStatus(emsMeterCollectedData);
    }

    /**
     * 删除数据
     *
     * @param emsMeterCollectedData
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void delete(EmsMeterCollectedData emsMeterCollectedData) {
        super.delete(emsMeterCollectedData);
    }

    public List<Date> getStockPendulum(String execDTime, TemporalGranularityEnum temporalGranularityEnum) {
        EmsMeterCollectedData params = new EmsMeterCollectedData();
        params.setDataDate(DateUtils.parseDate(execDTime));
        params.setDataType(temporalGranularityEnum.getCode());
        return this.dao.getStockPendulum(params);
    }

    /**
     * 查询当前记录是否是存量数据 设备ID + 数据时间 + 数据类型 唯一确定一条记录
     *
     * @param eepc
     * @return
     */
    public EmsMeterCollectedData isStockedRec(EmsMeterCollectedData eepc) {
        return this.dao.isStockedRec(eepc);
    }

    public List<EmsMeterCollectedData> calculateBaseDataList(EmsMeterCollectedData params) {
        return this.dao.calculateBaseDataList(params);
    }

    public List<MeterPendulumDisplayEntity> meterPendulumDisplay(EmsMeterCollectedData params) {
        return this.dao.meterPendulumDisplay(params);
    }

    public EmsMeterCollectedData getDeviceLastData(String deviceId){
        return this.dao.getDeviceLastData(deviceId);
    }
}
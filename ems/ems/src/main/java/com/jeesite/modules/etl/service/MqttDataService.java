package com.jeesite.modules.etl.service;

import com.alibaba.fastjson.JSON;
import com.cecec.api.base.MeterPayloadEntity;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.ems.service.EmsMeterCollectedDataService;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.etl.dao.MqttDataDao;
import com.jeesite.modules.etl.entity.MqttData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 电耗表Service
 *
 * @author 李鹏
 * @version 2023-05-25
 */
@Service
public class MqttDataService extends CrudService<MqttDataDao, MqttData> {

    @Resource
    private EmsMeterCollectedDataService collectedDataService;

    /**
     * 获取单条数据
     *
     * @param mqttData
     * @return
     */
    @Override
    public MqttData get(MqttData mqttData) {
        return super.get(mqttData);
    }

    /**
     * 查询分页数据
     *
     * @param mqttData 查询条件
     * @return
     */
    @Override
    public Page<MqttData> findPage(MqttData mqttData) {
        return super.findPage(mqttData);
    }

    /**
     * 查询列表数据
     *
     * @param mqttData
     * @return
     */
    @Override
    public List<MqttData> findList(MqttData mqttData) {
        return super.findList(mqttData);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param mqttData
     */
    @Override
    @Transactional(readOnly = false)
    public void save(MqttData mqttData) {
        super.save(mqttData);
    }

    /**
     * 更新状态
     *
     * @param mqttData
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(MqttData mqttData) {
        super.updateStatus(mqttData);
    }

    /**
     * 删除数据
     *
     * @param mqttData
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(MqttData mqttData) {
        super.delete(mqttData);
    }

    /**
     * 数据清洗处理类
     * 此处新启用事务处理
     *
     * @param optDTime
     * @param offset
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void dataCleansingProcess(String companyCode, List<EmsMeter> meterList, Date optDTime, Integer offset) {
        //查询所有设备信息
        Map<String, EmsMeter> meterMap = meterList.stream().collect(Collectors.toMap(EmsMeter::getMeterCode, EmsMeter -> EmsMeter, (key1, key2) -> key2));
        List<String> meterCodeList = new ArrayList<>(meterMap.keySet());

        //查询待处理的时间节点的所有数据（因为每个厂的电表数量不超过100个，所以全量查询）
        Date tsEnd = DateUtils.calculateSecond(optDTime, offset);
        Map<String, List<MqttData>> dataMapping = getMqttData(optDTime, tsEnd, meterCodeList);
        ArrayList<String> dataKeyList = new ArrayList<>(dataMapping.keySet());
        //电表ID的list 和 查询出来的id做差集,删除后meterCodeList为没查到数据的电表集合
        meterCodeList.removeAll(dataKeyList);

        //对查询
        for (Map.Entry<String, List<MqttData>> dataEntry : dataMapping.entrySet()) {
            String meterCode = dataEntry.getKey();
            List<MqttData> dataP = dataEntry.getValue();
            //第一条为有效数据
            MqttData mqttData = dataP.get(0);
            electricDataSave(mqttData, optDTime, meterCode, meterMap.get(meterCode), companyCode, true);
        }
        //未查询到数据的设备，插入默认数据，并标记记录异常
        unvalidDataCompensate(meterCodeList, meterMap, optDTime, companyCode);
    }

    /**
     * 未查询到的数据插入一条默认数据，标记为异常
     *
     * @param meterCodeList
     * @param meterMap
     * @param optDTime
     */
    private void unvalidDataCompensate(List<String> meterCodeList, Map<String, EmsMeter> meterMap, Date optDTime, String companyCode) {
        //时间往前推1小时
        Date tsStart = DateUtils.calculateHour(optDTime, -1);
        meterCodeList.forEach(meterCode -> {
            /**
             * 查询该设备 n 小时以内的最近的一条数据，，
             */
            MqttData latestData = getLatestMqttData(meterCode, tsStart, optDTime);
            if (Objects.nonNull(latestData)) {
                // 如果有，则作为最新的数据插入
                electricDataSave(latestData, optDTime, meterCode, meterMap.get(meterCode), companyCode, false);
            } else {
                // 如果没有，则查询该设备的最后一条数据填充
                cleaningUpReplace(optDTime, meterCode, meterMap.get(meterCode), companyCode);
            }
        });
    }

    private void cleaningUpReplace(Date optDTime, String meterCode, EmsMeter emsMeter, String companyCode) {
        EmsMeterCollectedData lastData = collectedDataService.getDeviceLastData(meterCode);
        EmsMeterCollectedData replaceCollectedData;
        if(lastData != null){
            replaceCollectedData = lastData;
            replaceCollectedData.setId(IdGen.nextId());
            replaceCollectedData.setIsNewRecord(true);
            replaceCollectedData.setDataDateTime(optDTime);
            replaceCollectedData.setDataDate(DateUtils.parseDate(DateUtils.formatDate(optDTime)));
            replaceCollectedData.setDataTime(new Time(optDTime.getTime()));
            replaceCollectedData.setNotValid(SysYesNoEnum._1.getCode());
        }else {
            // 如果生成的数据也不存在,说明这是当前设备的第一条数据
            replaceCollectedData = saveZeroCollectedData(optDTime, meterCode, emsMeter, companyCode);
        }
        powerConsumptionSave(replaceCollectedData);
    }

    private EmsMeterCollectedData saveZeroCollectedData(Date optDTime, String meterCode, EmsMeter emsMeter, String companyCode) {
        EmsMeterCollectedData.EmsMeterCollectedDataBuilder builder = EmsMeterCollectedData.builder()
                .deviceId(meterCode)        // 设备ID
                .deviceName(emsMeter.getMeterName())        // 设备名称
                .dataDateTime(optDTime)        // 数据时间， 2023-03-24 22:00:12
                .dataDate(DateUtils.parseDate(DateUtils.formatDate(optDTime)))        // 数据日期， 2023-03-24
                .dataTime(new Time(optDTime.getTime()))        // 数据时间， 22:00:12
                .dataType(TemporalGranularityEnum.VD_Quarter.getCode())        // 数据类型：15分钟、小时、日、月、年
                .companyCode(companyCode)
                .companyName(emsMeter.getCompanyName())
                .notValid(SysYesNoEnum._0.getCode())
                .positiveActiveEnergy(0d)        // 正向有功电能
                .reverseActiveEnergy(0d)        // 反向有功电能
                .positiveReactiveEnergy(0d)        // 正向无功电能
                .reverseReactiveEnergy(0d)        // 反向无功电能
                .aphaseV(0d)        // A相电压
                .bphaseV(0d)        // B相电压
                .cphaseV(0d)        // C相电压
                .aphaseA(0d)        // A相电流
                .bphaseA(0d)        // B相电流
                .cphaseA(0d)        // C相电流
                .totalAp(0d)        // 总有功功率
                .aphaseAp(0d)        // A相有功功率
                .bphaseAp(0d)        // B相有功功率
                .cphaseAp(0d)        // C相有功功率
                .totalRp(0d)        // 总无功功率
                .aphaseRp(0d)        // A相无功功率
                .bphaseRp(0d)        // B相无功功率
                .cphaseRp(0d)        // C相无功功率
                .totalPf(0d)        // 总功率因数
                .aphasePf(0d)        // A相功率因数
                .bphasePf(0d)        // B相功率因数
                .cphasePf(0d)        // C相功率因数
                .notValid(SysYesNoEnum._1.getCode());//无效数据
        return builder.build();
    }

    /**
     * 数组组装和入库
     *
     * @param mqttData
     * @param optDTime
     * @param meterCode
     * @param emsMeter
     * @param companyCode
     * @param isValid
     */
    private void electricDataSave(MqttData mqttData, Date optDTime, String meterCode, EmsMeter emsMeter, String companyCode, boolean isValid) {
        //数据组装
        EmsMeterCollectedData.EmsMeterCollectedDataBuilder builder = EmsMeterCollectedData.builder()
                .deviceId(meterCode)        // 设备ID
                .deviceName(emsMeter.getMeterName())        // 设备名称
                .dataDateTime(optDTime)        // 数据时间， 2023-03-24 22:00:12
                .dataDate(DateUtils.parseDate(DateUtils.formatDate(optDTime)))        // 数据日期， 2023-03-24
                .dataTime(new Time(optDTime.getTime()))        // 数据时间， 22:00:12
                .dataType(TemporalGranularityEnum.VD_Quarter.getCode())        // 数据类型：15分钟、小时、日、月、年
                .companyCode(companyCode)
                .companyName(emsMeter.getCompanyName())
                .notValid(SysYesNoEnum._0.getCode());
        //数据解析处理
        MeterPayloadEntity payload = JSON.parseObject(mqttData.getPayload(), MeterPayloadEntity.class);
        builder
                .positiveActiveEnergy(payload.getEp())        // 正向有功电能
                .reverseActiveEnergy(payload.getErp())        // 反向有功电能
                .positiveReactiveEnergy(payload.getEqi())        // 正向无功电能
                .reverseReactiveEnergy(payload.getErqi())        // 反向无功电能
                .aphaseV(payload.getUa())        // A相电压
                .bphaseV(payload.getUb())        // B相电压
                .cphaseV(payload.getUc())        // C相电压
                .aphaseA(payload.getIa())        // A相电流
                .bphaseA(payload.getIb())        // B相电流
                .cphaseA(payload.getIc())        // C相电流
                .totalAp(payload.getP())        // 总有功功率
                .aphaseAp(payload.getPa())        // A相有功功率
                .bphaseAp(payload.getPb())        // B相有功功率
                .cphaseAp(payload.getPc())        // C相有功功率
                .totalRp(payload.getQ())        // 总无功功率
                .aphaseRp(payload.getQa())        // A相无功功率
                .bphaseRp(payload.getQb())        // B相无功功率
                .cphaseRp(payload.getQc())        // C相无功功率
                .totalPf(payload.getPf())        // 总功率因数
                .aphasePf(payload.getPfa())        // A相功率因数
                .bphasePf(payload.getPfb())        // B相功率因数
                .cphasePf(payload.getPfc())        // C相功率因数
                .mqttTs(mqttData.getTs());
        //是否有效数据
        if (!isValid) {
            builder.notValid(SysYesNoEnum._1.getCode());
        }
        powerConsumptionSave(builder.build());
    }

    private MqttData getLatestMqttData(String meterCode, Date tsStart, Date tsEnd) {
        MqttData params = new MqttData();
        params.setDeviceCode(meterCode);
        params.setTsStart(tsStart);
        params.setTsEnd(tsEnd);
        return this.dao.getLatestMqttData(params);
    }

    /**
     * 先查询记录是否存在，若存在则更新
     *
     * @param emcd
     */
    private void powerConsumptionSave(EmsMeterCollectedData emcd) {
        EmsMeterCollectedData stockedRec = collectedDataService.isStockedRec(emcd);
        if (Objects.nonNull(stockedRec)) {
            emcd.setId(stockedRec.getId());
            emcd.setIsNewRecord(stockedRec.getIsNewRecord());
        }
        collectedDataService.save(emcd);
    }

    /**
     * 查询设备上报的原始数据
     *
     * @param tsStart
     * @param tsEnd
     * @return
     */
    private Map<String, List<MqttData>> getMqttData(Date tsStart, Date tsEnd, List<String> deviceCodeList) {
        MqttData params = new MqttData();
        params.setTsStart(tsStart);
        params.setTsEnd(tsEnd);
        params.setDeviceCodeList(deviceCodeList);
        params.getSqlMap().getOrder().setOrderBy("a.ts");
        List<MqttData> mqttDataList = this.findList(params);

        //把数据以 设备ID -》 list 做分组
        return mqttDataList.stream().collect(Collectors.groupingBy(MqttData::getDeviceCode, LinkedHashMap::new, Collectors.toList()));
    }
}
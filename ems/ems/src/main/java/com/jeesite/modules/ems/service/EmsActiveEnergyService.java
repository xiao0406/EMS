package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.ems.dao.EmsAreaMeterDao;
import com.jeesite.modules.ems.dao.EmsTerminalDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 有功电量Service
 *
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly = true)
public class EmsActiveEnergyService extends CrudService<EmsAreaMeterDao, EmsAreaMeter> {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;
    @Resource
    private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;
    @Resource
    private EmsElectricPowerAreaConsumptionService emsElectricPowerAreaConsumptionService;
    @Resource
    private EmsElectricPowerAreaConsumptionStatisticsService emsElectricPowerAreaConsumptionStatisticsService;


    public ActiveEnergyStatisticsEntity activeEnergyStatistics(EnergyManageParam energyManageParam) throws ExecutionException, InterruptedException, TimeoutException {
        User loginUser = UserUtils.getUser();
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        ActiveEnergyStatisticsEntity rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = activeEnergyAreaStatistics(loginUser, energyManageParam);
                break;
            case GROUP_Device:
                rlt = activeEnergyDeviceStatistics(loginUser, energyManageParam);
                break;
        }
        return rlt;
    }

    private ActiveEnergyStatisticsEntity activeEnergyDeviceStatistics(User loginUser, EnergyManageParam energyManageParam) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = loginUser.getCorpCode();

        //正向有功总电量
        String activeSql = "";
        //最大值
        String activeMaxSql = "";
        //最小值
        String activeMinSql = "";
        //反向有功总电量
        String reverseSql = "";
        //最大值
        String reverseMaxSql = "";
        //最小值
        String reverseMinSql = "";

        //组装查询参数
        ArrayList<Object> params = new ArrayList<>();
        params.add(corpCode);
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
            case VD_Day:
                activeSql = "SELECT sum(eepc.positive_active_energy ) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMaxSql = "SELECT MAX(eepc.positive_active_energy ) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMinSql = "SELECT MIN(eepc.positive_active_energy ) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                reverseSql = "SELECT sum(eepc.reverse_active_power ) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                reverseMaxSql = "SELECT MAX(eepc.reverse_active_power ) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                reverseMinSql = "SELECT MIN(eepc.reverse_active_power ) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";

                params.add(DateUtils.parseDate(energyManageParam.getQryStartTime()));
                params.add(DateUtils.parseDate(energyManageParam.getQryEndTime()));
                break;
            case VD_Month:
            case VD_Year:
                activeSql = "SELECT sum(eepc.positive_active_energy ) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMaxSql = "SELECT MAX(eepc.positive_active_energy ) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMinSql = "SELECT MIN(eepc.positive_active_energy ) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                reverseSql = "SELECT sum(eepc.reverse_active_power ) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                reverseMaxSql = "SELECT MAX(eepc.reverse_active_power ) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                reverseMinSql = "SELECT MIN(eepc.reverse_active_power ) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";

                params.add(energyManageParam.getQryStartTime());
                params.add(energyManageParam.getQryEndTime());
                break;
        }
        params.add(temporalGranularity.getCode());
        params.add(energyManageParam.getMeterCode());

        Object[] args = params.toArray();

        //查询结果处理
        ResultSetExtractor rse = new ResultSetExtractor<Double>() {
            @Override
            public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getDouble("value");
                }
                return 0d;
            }
        };
        Map<String, Future<Double>> threads = new LinkedHashMap<>();
        threads.put("active", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, activeSql, args, rse)));
        threads.put("activeMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, activeMaxSql, args, rse)));
        threads.put("activeMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, activeMinSql, args, rse)));
        threads.put("reverse", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reverseSql, args, rse)));
        threads.put("reverseMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reverseMaxSql, args, rse)));
        threads.put("reverseMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reverseMinSql, args, rse)));

        //封装返回结果
        return ActiveEnergyStatisticsEntity.builder()
                .positiveActiveEnergy(threads.get("active").get(20, TimeUnit.SECONDS))
                .positiveActiveEnergyMax(threads.get("activeMax").get(20, TimeUnit.SECONDS))
                .positiveActiveEnergyMin(threads.get("activeMin").get(20, TimeUnit.SECONDS))
                .reverseActivePower(threads.get("reverse").get(20, TimeUnit.SECONDS))
                .reverseActivePowerMax(threads.get("reverseMax").get(20, TimeUnit.SECONDS))
                .reverseActivePowerMin(threads.get("reverseMin").get(20, TimeUnit.SECONDS))
                .build();
    }

    private ActiveEnergyStatisticsEntity activeEnergyAreaStatistics(User loginUser, EnergyManageParam energyManageParam) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = loginUser.getCorpCode();
        String areaCode = energyManageParam.getAreaCode();

        //正向有功总电量
        String activeSql = "";
        //最大值
        String activeMaxSql = "";
        //最小值
        String activeMinSql = "";
        //反向有功总电量
        String reverseSql = "";
        //最大值
        String reverseMaxSql = "";
        //最小值
        String reverseMinSql = "";

        //组装查询参数
        ArrayList<Object> params = new ArrayList<>();
        params.add(corpCode);
        params.add(areaCode);
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
            case VD_Day:
                activeSql = "select sum( eepc.positive_active_energy ) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                activeMaxSql = "select MAX( eepc.positive_active_energy ) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                activeMinSql = "select MIN( eepc.positive_active_energy ) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                reverseSql = "select sum( eepc.reverse_active_power ) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                reverseMaxSql = "select MAX( eepc.reverse_active_power ) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                reverseMinSql = "select MIN( eepc.reverse_active_power ) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";

                params.add(DateUtils.parseDate(energyManageParam.getQryStartTime()));
                params.add(DateUtils.parseDate(energyManageParam.getQryEndTime()));
                break;
            case VD_Month:
            case VD_Year:
                activeSql = "select sum(eepc.positive_active_energy ) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                activeMaxSql = "select MAX(eepc.positive_active_energy ) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                activeMinSql = "select MIN(eepc.positive_active_energy ) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                reverseSql = "select sum(eepc.reverse_active_power ) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                reverseMaxSql = "select MAX(eepc.reverse_active_power ) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                reverseMinSql = "select MIN(eepc.reverse_active_power ) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";

                params.add(energyManageParam.getQryStartTime());
                params.add(energyManageParam.getQryEndTime());
                break;
        }
        params.add(temporalGranularity.getCode());

        Object[] args = params.toArray();

        //查询结果处理
        ResultSetExtractor rse = new ResultSetExtractor<Double>() {
            @Override
            public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getDouble("value");
                }
                return 0d;
            }
        };
        Map<String, Future<Double>> threads = new LinkedHashMap<>();
        threads.put("active", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, activeSql, args, rse)));
        threads.put("activeMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, activeMaxSql, args, rse)));
        threads.put("activeMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, activeMinSql, args, rse)));
        threads.put("reverse", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reverseSql, args, rse)));
        threads.put("reverseMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reverseMaxSql, args, rse)));
        threads.put("reverseMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reverseMinSql, args, rse)));

        //封装返回结果
        return ActiveEnergyStatisticsEntity.builder()
                .positiveActiveEnergy(threads.get("active").get(20, TimeUnit.SECONDS))
                .positiveActiveEnergyMax(threads.get("activeMax").get(20, TimeUnit.SECONDS))
                .positiveActiveEnergyMin(threads.get("activeMin").get(20, TimeUnit.SECONDS))
                .reverseActivePower(threads.get("reverse").get(20, TimeUnit.SECONDS))
                .reverseActivePowerMax(threads.get("reverseMax").get(20, TimeUnit.SECONDS))
                .reverseActivePowerMin(threads.get("reverseMin").get(20, TimeUnit.SECONDS))
                .build();
    }

    public EChart activeEnergyEChart(EnergyManageParam energyManageParam) {
        //获取前端传来的公司code
        String companyCode = energyManageParam.getCompanyCode();

        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        String areaCode = energyManageParam.getAreaCode();
        String meterCode = energyManageParam.getMeterCode();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setDataType(temporalGranularity.getCode());
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));

        switch (energyGroup) {
            case GROUP_Area:
                List<String> meterMarkList = EmsUserHelper.getMeterMarkList(companyCode,areaCode);
                params.setAreaCode(areaCode);
                params.setAreaMarkList(meterMarkList);
                break;
            case GROUP_Device:
                params.setDeviceId(meterCode);
                break;
        }

        List<String> xAxes = ListUtils.newArrayList();
        List<Double> active = new ArrayList<>();
        List<Double> revActive = new ArrayList<>();
        List<EmsElectricPowerConsumption> consumptionList = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                consumptionList = emsElectricPowerConsumptionService.activeEnergyEChart(params);
                consumptionList.stream().forEach(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd HH:mm");
                    xAxes.add(label);
                    active.add(o.getPositiveActiveEnergy());
                    revActive.add(o.getReverseActivePower());
                });
                break;
            case VD_Day:
                consumptionList = emsElectricPowerConsumptionService.activeEnergyEChart(params);
                consumptionList = consumptionList.stream().sorted(Comparator.comparing(EmsElectricPowerConsumption::getDataDateTime)).collect(Collectors.toList());
                consumptionList.stream().forEach(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd");
                    xAxes.add(label);
                    active.add(o.getPositiveActiveEnergy());
                    revActive.add(o.getReverseActivePower());
                });
                break;
            case VD_Month:
            case VD_Year:
                EmsElectricPowerConsumptionStatistics spams = new EmsElectricPowerConsumptionStatistics();
                spams.setDataType(temporalGranularity.getCode());
                spams.setQryStartTime(qryStartTime);
                spams.setQryEndTime(qryEndTime);
                switch (energyGroup) {
                    case GROUP_Area:
                        List<String> meterMarkList = EmsUserHelper.getMeterMarkList(companyCode,areaCode);
                        spams.setAreaCode(areaCode);
                        spams.setAreaMarkList(meterMarkList);
                        break;
                    case GROUP_Device:
                        spams.setDeviceId(meterCode);
                        break;
                }
                List<EmsElectricPowerConsumptionStatistics> statisticsList = emsElectricPowerConsumptionStatisticsService.activeEnergyEChart(spams);
                statisticsList = statisticsList.stream().sorted(Comparator.comparing(EmsElectricPowerConsumptionStatistics::getDataDateKey)).collect(Collectors.toList());
                statisticsList.stream().forEach(o -> {
                    String label = o.getDataDateKey();
                    xAxes.add(label);
                    active.add(o.getPositiveActiveEnergy());
                    revActive.add(o.getReverseActivePower());
                });
                break;
        }

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("正向有功电能", active),
                new EChartItem("反向有功电能", revActive)));
        return EChart.builder().body(body).build();
    }

    public Page<ActiveEnergyEntity> activeEnergyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        Page<ActiveEnergyEntity> rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = areaActiveEnergyHisPageList(energyManageParam, request, response);
                break;
            case GROUP_Device:
                rlt = deviceActiveEnergyHisPageList(energyManageParam, request, response);
                break;
        }
        return rlt;
    }

    private Page<ActiveEnergyEntity> deviceActiveEnergyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));
        params.setDataType(temporalGranularity.getCode());
        params.setDeviceId(energyManageParam.getMeterCode());
        params.setPage(new Page<EmsElectricPowerConsumption>(request, response));
        params.getSqlMap().getOrder().setOrderBy("a.data_date_time desc");

        Page<ActiveEnergyEntity> rlt = new Page<>(request, response);
        Page<EmsElectricPowerConsumption> areaConsPage = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsPage = emsElectricPowerConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getDeviceName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList()));
                break;
            case VD_Day:
                areaConsPage = emsElectricPowerConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getDeviceName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList()));
                break;
            case VD_Month:
            case VD_Year:
                EmsElectricPowerConsumptionStatistics sparams = new EmsElectricPowerConsumptionStatistics();
                sparams.setQryStartTime(qryStartTime);
                sparams.setQryEndTime(qryEndTime);
                sparams.setDataType(temporalGranularity.getCode());
                sparams.setDeviceId(energyManageParam.getMeterCode());
                sparams.setPage(new Page<EmsElectricPowerConsumptionStatistics>(request, response));
                sparams.getSqlMap().getOrder().setOrderBy("a.data_date_key desc");
                Page<EmsElectricPowerConsumptionStatistics> statisticsPage = emsElectricPowerConsumptionStatisticsService.findPage(sparams);
                rlt.setPageNo(statisticsPage.getPageNo());
                rlt.setCount(statisticsPage.getCount());
                rlt.setList(statisticsPage.getList().stream().map(o -> {
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(o.getDataDateKey());
                    activeEnergyEntity.setTypeInfo(o.getDeviceName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList()));
                break;
        }
        return rlt;
    }

    private Page<ActiveEnergyEntity> areaActiveEnergyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        String areaCode = energyManageParam.getAreaCode();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerAreaConsumption params = new EmsElectricPowerAreaConsumption();
        params.setAreaCode(areaCode);
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));
        params.setDataType(temporalGranularity.getCode());
        params.setPage(new Page<EmsElectricPowerAreaConsumption>(request, response));
        params.getSqlMap().getOrder().setOrderBy("a.data_date_time desc");

        Page<ActiveEnergyEntity> rlt = new Page<>(request, response);
        Page<EmsElectricPowerAreaConsumption> areaConsPage = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsPage = emsElectricPowerAreaConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getAreaName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList()));
                break;
            case VD_Day:
                areaConsPage = emsElectricPowerAreaConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getAreaName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList()));
                break;
            case VD_Month:
            case VD_Year:
                EmsElectricPowerAreaConsumptionStatistics sparams = new EmsElectricPowerAreaConsumptionStatistics();
                sparams.setAreaCode(areaCode);
                sparams.setQryStartTime(qryStartTime);
                sparams.setQryEndTime(qryEndTime);
                sparams.setDataType(temporalGranularity.getCode());
                sparams.setPage(new Page<EmsElectricPowerAreaConsumptionStatistics>(request, response));
                sparams.getSqlMap().getOrder().setOrderBy("a.data_date_key desc");
                Page<EmsElectricPowerAreaConsumptionStatistics> statisticsPage = emsElectricPowerAreaConsumptionStatisticsService.findPage(sparams);
                rlt.setPageNo(statisticsPage.getPageNo());
                rlt.setCount(statisticsPage.getCount());
                rlt.setList(statisticsPage.getList().stream().map(o -> {
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(o.getDataDateKey());
                    activeEnergyEntity.setTypeInfo(o.getAreaName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList()));
                break;
        }
        return rlt;
    }

    public String activeEnergyHisListExp(EnergyManageParam energyManageParam) throws IOException {
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        String presignedUrl = null;
        List<ActiveEnergyEntity> list = null;
        switch (energyGroup) {
            case GROUP_Area:
                list = areaActiveEnergyHisList(energyManageParam);
                break;
            case GROUP_Device:
                list = deviceActiveEnergyHisList(energyManageParam);
                break;
        }
        String fileName = "有功电量记录导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try (ExcelExport ee = new ExcelExport("有功电量记录导出", ActiveEnergyEntity.class)) {
            presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
        }
        return presignedUrl;
    }

    private List<ActiveEnergyEntity> deviceActiveEnergyHisList(EnergyManageParam energyManageParam) {
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        String areaCode = energyManageParam.getAreaCode();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));
        params.setDataType(temporalGranularity.getCode());
        params.setDeviceId(energyManageParam.getMeterCode());

        List<ActiveEnergyEntity> rlt = null;
        List<EmsElectricPowerConsumption> areaConsList = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsList = emsElectricPowerConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getDeviceName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList());
                break;
            case VD_Day:
                areaConsList = emsElectricPowerConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getDeviceName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList());
                break;
            case VD_Month:
            case VD_Year:
                EmsElectricPowerConsumptionStatistics sparams = new EmsElectricPowerConsumptionStatistics();
                sparams.setAreaCode(areaCode);
                sparams.setQryStartTime(qryStartTime);
                sparams.setQryEndTime(qryEndTime);
                sparams.setDataType(temporalGranularity.getCode());
                List<EmsElectricPowerConsumptionStatistics> statisticsList = emsElectricPowerConsumptionStatisticsService.findList(sparams);
                rlt = statisticsList.stream().map(o -> {
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(o.getDataDateKey());
                    activeEnergyEntity.setTypeInfo(o.getDeviceName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList());
                break;
        }
        return rlt;
    }

    private List<ActiveEnergyEntity> areaActiveEnergyHisList(EnergyManageParam energyManageParam) {
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        String areaCode = energyManageParam.getAreaCode();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerAreaConsumption params = new EmsElectricPowerAreaConsumption();
        params.setAreaCode(areaCode);
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));
        params.setDataType(temporalGranularity.getCode());
        params.getSqlMap().getOrder().setOrderBy("a.data_date_time desc");

        List<ActiveEnergyEntity> rlt = null;
        List<EmsElectricPowerAreaConsumption> areaConsList = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsList = emsElectricPowerAreaConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getAreaName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList());
                break;
            case VD_Day:
                areaConsList = emsElectricPowerAreaConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(label);
                    activeEnergyEntity.setTypeInfo(o.getAreaName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList());
                break;
            case VD_Month:
            case VD_Year:
                EmsElectricPowerAreaConsumptionStatistics sparams = new EmsElectricPowerAreaConsumptionStatistics();
                sparams.setAreaCode(areaCode);
                sparams.setQryStartTime(qryStartTime);
                sparams.setQryEndTime(qryEndTime);
                sparams.setDataType(temporalGranularity.getCode());
                sparams.getSqlMap().getOrder().setOrderBy("a.data_date_key desc");
                List<EmsElectricPowerAreaConsumptionStatistics> statisticsList = emsElectricPowerAreaConsumptionStatisticsService.findList(sparams);
                rlt = statisticsList.stream().map(o -> {
                    ActiveEnergyEntity activeEnergyEntity = new ActiveEnergyEntity();
                    activeEnergyEntity.setDateTime(o.getDataDateKey());
                    activeEnergyEntity.setTypeInfo(o.getAreaName());
                    activeEnergyEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    activeEnergyEntity.setReverseActivePower(o.getReverseActivePower());
                    return activeEnergyEntity;
                }).collect(Collectors.toList());
                break;
        }
        return rlt;
    }

}
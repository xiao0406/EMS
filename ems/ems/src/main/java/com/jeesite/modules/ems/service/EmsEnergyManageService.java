package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
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
public class EmsEnergyManageService extends CrudService<EmsAreaMeterDao, EmsAreaMeter> {

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

    public EnergyManageStatisticsEntity energyManageStatistics(EnergyManageParam energyManageParam) throws ExecutionException, InterruptedException, TimeoutException {
        User loginUser = UserUtils.getUser();
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        EnergyManageStatisticsEntity rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = energyManageAreaStatistics(loginUser, energyManageParam);
                break;
            case GROUP_Device:
                rlt = energyManageDeviceStatistics(loginUser, energyManageParam);
                break;
        }
        return rlt;
    }

    private EnergyManageStatisticsEntity energyManageDeviceStatistics(User loginUser, EnergyManageParam energyManageParam) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = loginUser.getCorpCode();

        //正向有功总电量
        String activeSql = "";
        //最大值
        String activeMaxSql = "";
        //最小值
        String activeMinSql = "";
        //反向有功总电量
        String revActiveSql = "";
        //最大值
        String revActiveMaxSql = "";
        //最小值
        String revActiveMinSql = "";
        //正向无功总电量
        String reactiveSql = "";
        //最大值
        String reactiveMaxSql = "";
        //最小值
        String reactiveMinSql = "";
        //反向无功总电量
        String revReactiveSql = "";
        //最大值
        String revReactiveMaxSql = "";
        //最小值
        String revReactiveMinSql = "";

        //组装查询参数
        String areaCode = energyManageParam.getAreaCode();
        ArrayList<Object> params = new ArrayList<>();
        params.add(corpCode);
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
            case VD_Day:
                activeSql = "select sum(eepc.positive_active_energy) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMaxSql = "select MAX(eepc.positive_active_energy) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMinSql = "select MIN(eepc.positive_active_energy) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                revActiveSql = "select sum(eepc.reverse_active_power) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                revActiveMaxSql = "select MAX(eepc.reverse_active_power) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                revActiveMinSql = "select MIN(eepc.reverse_active_power) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                reactiveSql = "select sum(eepc.positive_reactive_energy) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                reactiveMaxSql = "select MAX(eepc.positive_reactive_energy) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                reactiveMinSql = "select MIN(eepc.positive_reactive_energy) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                revReactiveSql = "select sum(eepc.reverse_reactive_power) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                revReactiveMaxSql = "select MAX(eepc.reverse_reactive_power) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";
                revReactiveMinSql = "select MIN(eepc.reverse_reactive_power) AS  VALUE FROM ems_electric_power_consumption eepc WHERE eepc.corp_code = ?  AND  eepc.data_date_time > ? and eepc.data_date_time <= ? AND eepc.data_type = ? AND eepc.device_id = ? ";

                params.add(DateUtils.parseDate(energyManageParam.getQryStartTime()));
                params.add(DateUtils.parseDate(energyManageParam.getQryEndTime()));
                break;
            case VD_Month:
            case VD_Year:
                activeSql = "select sum(eepc.positive_active_energy) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMaxSql = "select MAX(eepc.positive_active_energy) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                activeMinSql = "select MIN(eepc.positive_active_energy) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                revActiveSql = "select sum(eepc.reverse_active_power) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                revActiveMaxSql = "select MAX(eepc.reverse_active_power) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                revActiveMinSql = "select MIN(eepc.reverse_active_power) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                reactiveSql = "select sum(eepc.positive_reactive_energy) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                reactiveMaxSql = "select MAX(eepc.positive_reactive_energy) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                reactiveMinSql = "select MIN(eepc.positive_reactive_energy) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                revReactiveSql = "select sum(eepc.reverse_reactive_power) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                revReactiveMaxSql = "select MAX(eepc.reverse_reactive_power) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";
                revReactiveMinSql = "select MIN(eepc.reverse_reactive_power) AS  VALUE FROM ems_electric_power_consumption_statistics eepc WHERE eepc.corp_code = ?  AND eepc.data_date_key BETWEEN ? AND ?  AND eepc.data_type = ? AND eepc.device_id = ? ";

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
        threads.put("revActive", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revActiveSql, args, rse)));
        threads.put("revActiveMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revActiveMaxSql, args, rse)));
        threads.put("revActiveMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revActiveMinSql, args, rse)));
        threads.put("reactive", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reactiveSql, args, rse)));
        threads.put("reactiveMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reactiveMaxSql, args, rse)));
        threads.put("reactiveMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reactiveMinSql, args, rse)));
        threads.put("revReactive", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revReactiveSql, args, rse)));
        threads.put("revReactiveMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revReactiveMaxSql, args, rse)));
        threads.put("revReactiveMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revReactiveMinSql, args, rse)));


        Double active = threads.get("active").get(20, TimeUnit.SECONDS);
        Double activeMax = threads.get("activeMax").get(20, TimeUnit.SECONDS);
        Double activeMin = threads.get("activeMin").get(20, TimeUnit.SECONDS);
        Double revActive = threads.get("revActive").get(20, TimeUnit.SECONDS);
        Double revActiveMax = threads.get("revActiveMax").get(20, TimeUnit.SECONDS);
        Double revActiveMin = threads.get("revActiveMin").get(20, TimeUnit.SECONDS);
        Double reactive = threads.get("reactive").get(20, TimeUnit.SECONDS);
        Double reactiveMax = threads.get("reactiveMax").get(20, TimeUnit.SECONDS);
        Double reactiveMin = threads.get("reactiveMin").get(20, TimeUnit.SECONDS);
        Double revReactive = threads.get("revReactive").get(20, TimeUnit.SECONDS);
        Double revReactiveMax = threads.get("revReactiveMax").get(20, TimeUnit.SECONDS);
        Double revReactiveMin = threads.get("revReactiveMin").get(20, TimeUnit.SECONDS);
        //封装返回结果
        return EnergyManageStatisticsEntity.builder()
                .positiveActiveEnergy(active)
                .positiveActiveEnergyMax(activeMax)
                .positiveActiveEnergyMin(activeMin)
                .positiveActiveEnergyDiff(NumberUtils.sub(activeMax, activeMin))
//                .positiveActiveEnergyAverage()
                .reverseActivePower(revActive)
                .reverseActivePowerMax(revActiveMax)
                .reverseActivePowerMin(revActiveMin)
                .reverseActivePowerDiff(NumberUtils.sub(revActiveMax, revActiveMin))
//                .reverseActivePowerAverage()
                .positiveReactiveEnergy(reactive)
                .positiveReactiveEnergyMax(reactiveMax)
                .positiveReactiveEnergyMin(reactiveMin)
                .positiveReactiveEnergyDiff(NumberUtils.sub(reactiveMax, reactiveMin))
//                .positiveReactiveEnergyAverage()
                .reverseReactivePower(revReactive)
                .reverseReactivePowerMax(revReactiveMax)
                .reverseReactivePowerMin(revReactiveMin)
                .reverseReactivePowerDiff(NumberUtils.sub(revReactiveMax, revReactiveMin))
//                .reverseReactivePowerAverage()
                .build();
    }

    private EnergyManageStatisticsEntity energyManageAreaStatistics(User loginUser, EnergyManageParam energyManageParam) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = loginUser.getCorpCode();

        //正向有功总电量
        String activeSql = "";
        //最大值
        String activeMaxSql = "";
        //最小值
        String activeMinSql = "";
        //反向有功总电量
        String revActiveSql = "";
        //最大值
        String revActiveMaxSql = "";
        //最小值
        String revActiveMinSql = "";
        //正向无功总电量
        String reactiveSql = "";
        //最大值
        String reactiveMaxSql = "";
        //最小值
        String reactiveMinSql = "";
        //反向无功总电量
        String revReactiveSql = "";
        //最大值
        String revReactiveMaxSql = "";
        //最小值
        String revReactiveMinSql = "";

        //组装查询参数
        String areaCode = energyManageParam.getAreaCode();
        ArrayList<Object> params = new ArrayList<>();
        params.add(corpCode);
        params.add(areaCode);
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
            case VD_Day:
                activeSql = "select sum(eepc.positive_active_energy) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                activeMaxSql = "select MAX(eepc.positive_active_energy) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                activeMinSql = "select MIN(eepc.positive_active_energy) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                revActiveSql = "select sum(eepc.reverse_active_power) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                revActiveMaxSql = "select MAX(eepc.reverse_active_power) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                revActiveMinSql = "select MIN(eepc.reverse_active_power) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                reactiveSql = "select sum(eepc.positive_reactive_energy) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                reactiveMaxSql = "select MAX(eepc.positive_reactive_energy) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                reactiveMinSql = "select MIN(eepc.positive_reactive_energy) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                revReactiveSql = "select sum(eepc.reverse_reactive_power) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                revReactiveMaxSql = "select MAX(eepc.reverse_reactive_power) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";
                revReactiveMinSql = "select MIN(eepc.reverse_reactive_power) as value FROM ems_electric_power_area_consumption eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_time > ? and eepc.data_date_time <= ? and eepc.data_type = ? ";

                params.add(DateUtils.parseDate(energyManageParam.getQryStartTime()));
                params.add(DateUtils.parseDate(energyManageParam.getQryEndTime()));
                break;
            case VD_Month:
            case VD_Year:
                activeSql = "select sum(eepc.positive_active_energy) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                activeMaxSql = "select MAX(eepc.positive_active_energy) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                activeMinSql = "select MIN(eepc.positive_active_energy) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                revActiveSql = "select sum(eepc.reverse_active_power) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                revActiveMaxSql = "select MAX(eepc.reverse_active_power) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                revActiveMinSql = "select MIN(eepc.reverse_active_power) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                reactiveSql = "select sum(eepc.positive_reactive_energy) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                reactiveMaxSql = "select MAX(eepc.positive_reactive_energy) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                reactiveMinSql = "select MIN(eepc.positive_reactive_energy) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                revReactiveSql = "select sum(eepc.reverse_reactive_power) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                revReactiveMaxSql = "select MAX(eepc.reverse_reactive_power) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";
                revReactiveMinSql = "select MIN(eepc.reverse_reactive_power) as value FROM ems_electric_power_area_consumption_statistics eepc where eepc.corp_code = ? and eepc.area_code = ? and eepc.data_date_key BETWEEN ? and ? and eepc.data_type = ? ";

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
        threads.put("revActive", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revActiveSql, args, rse)));
        threads.put("revActiveMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revActiveMaxSql, args, rse)));
        threads.put("revActiveMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revActiveMinSql, args, rse)));
        threads.put("reactive", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reactiveSql, args, rse)));
        threads.put("reactiveMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reactiveMaxSql, args, rse)));
        threads.put("reactiveMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, reactiveMinSql, args, rse)));
        threads.put("revReactive", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revReactiveSql, args, rse)));
        threads.put("revReactiveMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revReactiveMaxSql, args, rse)));
        threads.put("revReactiveMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, revReactiveMinSql, args, rse)));


        Double active = threads.get("active").get(20, TimeUnit.SECONDS);
        Double activeMax = threads.get("activeMax").get(20, TimeUnit.SECONDS);
        Double activeMin = threads.get("activeMin").get(20, TimeUnit.SECONDS);
        Double revActive = threads.get("revActive").get(20, TimeUnit.SECONDS);
        Double revActiveMax = threads.get("revActiveMax").get(20, TimeUnit.SECONDS);
        Double revActiveMin = threads.get("revActiveMin").get(20, TimeUnit.SECONDS);
        Double reactive = threads.get("reactive").get(20, TimeUnit.SECONDS);
        Double reactiveMax = threads.get("reactiveMax").get(20, TimeUnit.SECONDS);
        Double reactiveMin = threads.get("reactiveMin").get(20, TimeUnit.SECONDS);
        Double revReactive = threads.get("revReactive").get(20, TimeUnit.SECONDS);
        Double revReactiveMax = threads.get("revReactiveMax").get(20, TimeUnit.SECONDS);
        Double revReactiveMin = threads.get("revReactiveMin").get(20, TimeUnit.SECONDS);
        //封装返回结果
        return EnergyManageStatisticsEntity.builder()
                .positiveActiveEnergy(active)
                .positiveActiveEnergyMax(activeMax)
                .positiveActiveEnergyMin(activeMin)
                .positiveActiveEnergyDiff(NumberUtils.sub(activeMax, activeMin))
//                .positiveActiveEnergyAverage()
                .reverseActivePower(revActive)
                .reverseActivePowerMax(revActiveMax)
                .reverseActivePowerMin(revActiveMin)
                .reverseActivePowerDiff(NumberUtils.sub(revActiveMax, revActiveMin))
//                .reverseActivePowerAverage()
                .positiveReactiveEnergy(reactive)
                .positiveReactiveEnergyMax(reactiveMax)
                .positiveReactiveEnergyMin(reactiveMin)
                .positiveReactiveEnergyDiff(NumberUtils.sub(reactiveMax, reactiveMin))
//                .positiveReactiveEnergyAverage()
                .reverseReactivePower(revReactive)
                .reverseReactivePowerMax(revReactiveMax)
                .reverseReactivePowerMin(revReactiveMin)
                .reverseReactivePowerDiff(NumberUtils.sub(revReactiveMax, revReactiveMin))
//                .reverseReactivePowerAverage()
                .build();
    }

    public EChart energyManageEChart(EnergyManageParam energyManageParam) {
        //获取前端传过来的公司code
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
        List<Double> reactive = new ArrayList<>();
        List<Double> revReactive = new ArrayList<>();
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
                    reactive.add(o.getPositiveReactiveEnergy());
                    revReactive.add(o.getReverseReactivePower());
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
                    reactive.add(o.getPositiveReactiveEnergy());
                    revReactive.add(o.getReverseReactivePower());
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
                    reactive.add(o.getPositiveReactiveEnergy());
                    revReactive.add(o.getReverseReactivePower());
                });
                break;
        }

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("正向有功电能", active),
                new EChartItem("反向有功电能", revActive),
                new EChartItem("正向无功电能", reactive),
                new EChartItem("反向无功电能", revReactive)));
        return EChart.builder().body(body).build();
    }

    private EChart buildEChartBody(TemporalGranularityEnum temporalGranularity, String thisKey, Map<String, EChartData> thisMap, String lastKey, Map<String, EChartData> lastMap) {
        ArrayList<String> xAxes = ListUtils.newArrayList();
        ArrayList<String> yAxes0 = ListUtils.newArrayList();
        ArrayList<String> yAxes1 = ListUtils.newArrayList();
        for (Map.Entry<String, EChartData> entry : thisMap.entrySet()) {
            String lable = entry.getKey();
            EChartData t = entry.getValue();
            EChartData y = lastMap.get(lable);
            //组装X轴
            xAxes.add(lable);
            //组装Y轴
            yAxes0.add(Objects.nonNull(t) ? t.getValue() : "-");
            yAxes1.add(Objects.nonNull(y) ? y.getValue() : "-");
        }
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(new EChartItem(thisKey, yAxes0), new EChartItem(lastKey, yAxes1)));
        //组装最后返回结果
        return EChart.builder()
                .temporalGranularity(temporalGranularity)
                .body(body)
                .build();
    }

    public Page<EnergyManageEntity> energyManageHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        Page<EnergyManageEntity> rlt = null;
        switch (energyGroup) {
            case GROUP_Area:
                rlt = energyHisPageList(energyManageParam, request, response);
                break;
            case GROUP_Device:
                rlt = deviceEnergyHisPageList(energyManageParam, request, response);
                break;
        }
        return rlt;
    }

    private Page<EnergyManageEntity> deviceEnergyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        String areaCode = energyManageParam.getAreaCode();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setAreaCode(areaCode);
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));
        params.setDataType(temporalGranularity.getCode());
        params.setDeviceId(energyManageParam.getMeterCode());
        params.setPage(new Page<EmsElectricPowerConsumption>(request, response));
        params.getSqlMap().getOrder().setOrderBy("a.data_date_time desc");

        Page<EnergyManageEntity> rlt = new Page<>(request, response);
        Page<EmsElectricPowerConsumption> areaConsPage = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsPage = emsElectricPowerConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    return buildEnergyManageEntity(label, o.getDeviceName(), o);
                }).collect(Collectors.toList()));
                break;
            case VD_Day:
                areaConsPage = emsElectricPowerConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    return buildEnergyManageEntity(label, o.getDeviceName(), o);
                }).collect(Collectors.toList()));
                break;
            case VD_Month:
            case VD_Year:
                EmsElectricPowerConsumptionStatistics sparams = new EmsElectricPowerConsumptionStatistics();
                sparams.setAreaCode(areaCode);
                sparams.setQryStartTime(qryStartTime);
                sparams.setQryEndTime(qryEndTime);
                sparams.setDataType(temporalGranularity.getCode());
                sparams.setPage(new Page<EmsElectricPowerConsumptionStatistics>(request, response));
                sparams.getSqlMap().getOrder().setOrderBy("a.data_date_key desc");
                Page<EmsElectricPowerConsumptionStatistics> statisticsPage = emsElectricPowerConsumptionStatisticsService.findPage(sparams);
                rlt.setPageNo(statisticsPage.getPageNo());
                rlt.setCount(statisticsPage.getCount());
                rlt.setList(statisticsPage.getList().stream().map(o -> {
                    EnergyManageEntity energyManageEntity = new EnergyManageEntity();
                    energyManageEntity.setDateTime(o.getDataDateKey());
                    energyManageEntity.setTypeInfo(o.getDeviceName());
                    energyManageEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    energyManageEntity.setReverseActivePower(o.getReverseActivePower());
                    energyManageEntity.setPositiveReactiveEnergy(o.getPositiveReactiveEnergy());
                    energyManageEntity.setReverseReactivePower(o.getReverseReactivePower());
                    return energyManageEntity;
                }).collect(Collectors.toList()));
                break;
        }
        return rlt;
    }

    private EnergyManageEntity buildEnergyManageEntity(String label, String typeInfo, EmsElectricPowerConsumption obj) {
        EnergyManageEntity energyManageEntity = new EnergyManageEntity();
        energyManageEntity.setDateTime(label);
        energyManageEntity.setTypeInfo(typeInfo);
        energyManageEntity.setPositiveActiveEnergy(obj.getPositiveActiveEnergy());
        energyManageEntity.setReverseActivePower(obj.getReverseActivePower());
        energyManageEntity.setPositiveReactiveEnergy(obj.getPositiveReactiveEnergy());
        energyManageEntity.setReverseReactivePower(obj.getReverseReactivePower());
        return energyManageEntity;
    }

    private Page<EnergyManageEntity> energyHisPageList(EnergyManageParam energyManageParam, HttpServletRequest request, HttpServletResponse response) {
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

        Page<EnergyManageEntity> rlt = new Page<>(request, response);
        Page<EmsElectricPowerAreaConsumption> areaConsPage = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsPage = emsElectricPowerAreaConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    return buildEnergyManageEntity(label, o.getAreaName(), o);
                }).collect(Collectors.toList()));
                break;
            case VD_Day:
                areaConsPage = emsElectricPowerAreaConsumptionService.findPage(params);
                rlt.setPageNo(areaConsPage.getPageNo());
                rlt.setCount(areaConsPage.getCount());
                rlt.setList(areaConsPage.getList().stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    return buildEnergyManageEntity(label, o.getAreaName(), o);
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
                    EnergyManageEntity energyManageEntity = new EnergyManageEntity();
                    energyManageEntity.setDateTime(o.getDataDateKey());
                    energyManageEntity.setTypeInfo(o.getAreaName());
                    energyManageEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    energyManageEntity.setReverseActivePower(o.getReverseActivePower());
                    energyManageEntity.setPositiveReactiveEnergy(o.getPositiveReactiveEnergy());
                    energyManageEntity.setReverseReactivePower(o.getReverseReactivePower());
                    return energyManageEntity;
                }).collect(Collectors.toList()));
                break;
        }
        return rlt;
    }

    private EnergyManageEntity buildEnergyManageEntity(String label, String typeInfo, EmsElectricPowerAreaConsumption obj) {
        EnergyManageEntity energyManageEntity = new EnergyManageEntity();
        energyManageEntity.setDateTime(label);
        energyManageEntity.setTypeInfo(typeInfo);
        energyManageEntity.setPositiveActiveEnergy(obj.getPositiveActiveEnergy());
        energyManageEntity.setReverseActivePower(obj.getReverseActivePower());
        energyManageEntity.setPositiveReactiveEnergy(obj.getPositiveReactiveEnergy());
        energyManageEntity.setReverseReactivePower(obj.getReverseReactivePower());
        return energyManageEntity;
    }

    public String energyManageHisListExp(EnergyManageParam energyManageParam) throws IOException {
        EnergyGroupEnum energyGroup = energyManageParam.getEnergyGroup();
        String presignedUrl = null;
        List<EnergyManageEntity> list = null;
        switch (energyGroup) {
            case GROUP_Area:
                list = energyHisList(energyManageParam);
                break;
            case GROUP_Device:
                list = deviceEnergyHisList(energyManageParam);
                break;
        }
        String fileName = "有功-无功电量记录导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try (ExcelExport ee = new ExcelExport("有功-无功电量记录导出", EnergyManageEntity.class)) {
            presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
        }
        return presignedUrl;
    }

    private List<EnergyManageEntity> deviceEnergyHisList(EnergyManageParam energyManageParam) {
        TemporalGranularityEnum temporalGranularity = energyManageParam.getTemporalGranularity();
        String areaCode = energyManageParam.getAreaCode();
        String qryStartTime = energyManageParam.getQryStartTime();
        String qryEndTime = energyManageParam.getQryEndTime();

        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setQryStartTime(DateUtils.parseDate(qryStartTime));
        params.setQryEndTime(DateUtils.parseDate(qryEndTime));
        params.setDataType(temporalGranularity.getCode());
        params.setDeviceId(energyManageParam.getMeterCode());

        List<EnergyManageEntity> rlt = null;
        List<EmsElectricPowerConsumption> areaConsList = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsList = emsElectricPowerConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    return buildEnergyManageEntity(label, o.getDeviceName(), o);
                }).collect(Collectors.toList());
                break;
            case VD_Day:
                areaConsList = emsElectricPowerConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    return buildEnergyManageEntity(label, o.getDeviceName(), o);
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
                    EnergyManageEntity energyManageEntity = new EnergyManageEntity();
                    energyManageEntity.setDateTime(o.getDataDateKey());
                    energyManageEntity.setTypeInfo(o.getDeviceName());
                    energyManageEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    energyManageEntity.setReverseActivePower(o.getReverseActivePower());
                    energyManageEntity.setPositiveReactiveEnergy(o.getPositiveReactiveEnergy());
                    energyManageEntity.setReverseReactivePower(o.getReverseReactivePower());
                    return energyManageEntity;
                }).collect(Collectors.toList());
                break;
        }
        return rlt;
    }

    private List<EnergyManageEntity> energyHisList(EnergyManageParam energyManageParam) {
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

        List<EnergyManageEntity> rlt = null;
        List<EmsElectricPowerAreaConsumption> areaConsList = null;
        switch (temporalGranularity) {
            case VD_Quarter:
            case VD_Hour:
                areaConsList = emsElectricPowerAreaConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd HH:mm");
                    return buildEnergyManageEntity(label, o.getAreaName(), o);
                }).collect(Collectors.toList());
                break;
            case VD_Day:
                areaConsList = emsElectricPowerAreaConsumptionService.findList(params);
                rlt = areaConsList.stream().map(o -> {
                    String label = DateUtils.formatDate(o.getDataDateTime(), "yyyy-MM-dd");
                    return buildEnergyManageEntity(label, o.getAreaName(), o);
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
                    EnergyManageEntity energyManageEntity = new EnergyManageEntity();
                    energyManageEntity.setDateTime(o.getDataDateKey());
                    energyManageEntity.setTypeInfo(o.getAreaName());
                    energyManageEntity.setPositiveActiveEnergy(o.getPositiveActiveEnergy());
                    energyManageEntity.setReverseActivePower(o.getReverseActivePower());
                    energyManageEntity.setPositiveReactiveEnergy(o.getPositiveReactiveEnergy());
                    energyManageEntity.setReverseReactivePower(o.getReverseReactivePower());
                    return energyManageEntity;
                }).collect(Collectors.toList());
                break;
        }
        return rlt;
    }
}
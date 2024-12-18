package com.jeesite.modules.ems.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.ems.dao.EmsAreaMeterDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.ems.entity.enums.MeterStatusFieldEnum;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import com.jeesite.modules.sys.utils.UserHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

/**
 * 有功电量Service
 *
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly = true)
public class EmsMeterSafetyService extends CrudService<EmsAreaMeterDao, EmsAreaMeter> {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;
    @Resource
    private EmsAlarmEventService emsAlarmEventService;

    public MeterSafetyStatisticsEntity meterSafetyStatistics(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        String deviceId = emsMeterSafetyEntity.getDeviceId();
        //查询设备的阈值设置
        EmsElectricPowerConsumption consumption = emsElectricPowerConsumptionService.getLatestEquipmentStatus(deviceId);
        MeterSafetyStatisticsEntity.MeterSafetyStatisticsEntityBuilder builder = MeterSafetyStatisticsEntity.builder();
        if (Objects.nonNull(consumption)) {
            builder
                    .power(
                            EquipmentPowerEntity.builder()
                                    .ratedPower(null)
                                    .activePower(consumption.getTotalActivePower())
                                    .reactivePower(consumption.getTotalReactivePower())
                                    .build()
                    )
                    .current(
                            EquipmentCurrentEntity.builder()
                                    .ratedCurrent(null)
                                    .aCurrent(consumption.getAphaseCurrent())
                                    .bCurrent(consumption.getBphaseCurrent())
                                    .cCurrent(consumption.getCphaseCurrent())
                                    .build()
                    )
                    .voltage(
                            EquipmentVoltageEntity.builder()
                                    .ratedVoltage(null)
                                    .aVoltage(consumption.getAphaseVoltage())
                                    .bVoltage(consumption.getBphaseVoltage())
                                    .cVoltage(consumption.getCphaseVoltage())
                                    .build()
                    )
                    .powerFactor(
                            EquipmentPowerFactorEntity.builder()
                                    .ratedPowerFactor(null)
                                    .totalPowerFactor(consumption.getTotalPowerFactor())
                                    .aPowerFactor(consumption.getAphasePowerFactor())
                                    .bPowerFactor(consumption.getBphasePowerFactor())
                                    .cPowerFactor(consumption.getCphasePowerFactor())
                                    .build()
                    );
        }
        return builder.build();
    }

    public EmsMeterSafetyEntity meterSafetyEChartStatistics(EmsMeterSafetyEntity emsMeterSafetyEntity) throws ExecutionException, InterruptedException, TimeoutException {
        User user = UserHelper.getUser();
        MeterStatusFieldEnum meterStatusField = emsMeterSafetyEntity.getMeterStatusField();
        switch (meterStatusField) {
            case ACTIVE_POWER:
                //有功功率
                buildActivePower(user, emsMeterSafetyEntity);
                break;
            case REACTIVE_POWER:
                //无功功率
                break;
            case CURRENT:
                //电流
                buildCurrent(user, emsMeterSafetyEntity);
                break;
            case VOLTAGE:
                //电压
                buildVoltage(user, emsMeterSafetyEntity);
                break;
            case POWER_FACTOR:
                //功率因数
                buildPowerFactor(user, emsMeterSafetyEntity);
                break;
        }
        return emsMeterSafetyEntity;
    }

    private void buildPowerFactor(User user, EmsMeterSafetyEntity emsMeterSafetyEntity) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = user.getCorpCode();
        String deviceId = emsMeterSafetyEntity.getDeviceId();
        Date qryDate = DateUtils.parseDate(emsMeterSafetyEntity.getQryDate());
        Date ofDayFirst = DateUtils.getOfDayFirst(qryDate);
        Date ofDayLast = DateUtils.getOfDayLast(qryDate);

        //组装查询参数
        ArrayList<Object> params = ListUtils.newArrayList(corpCode, deviceId, ofDayFirst, ofDayLast, TemporalGranularityEnum.VD_Quarter.getCode());
        Object[] args = params.toArray();

        //执行的sql语句
        String maxSql = "select max(total_pf) as value from ems_meter_collected_data where corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ?";
        String minSql = "select min(total_pf) as value from ems_meter_collected_data where corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ?";

        //查询结果处理
        ResultSetExtractor rse = new ResultSetExtractor<Double>() {
            @Override
            public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
                Double item = 0d;
                if (rs.next()) {
                    item = rs.getDouble("value");
                }
                return item;
            }
        };

        //多线程
        Map<String, Future<Double>> threads = new LinkedHashMap<>();
        threads.put("max", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, maxSql, args, rse)));
        threads.put("min", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, minSql, args, rse)));

        Double max = threads.get("max").get(20, TimeUnit.SECONDS);
        Double min = threads.get("min").get(20, TimeUnit.SECONDS);

        JSONObject rlt = new JSONObject();
        rlt.put("max", max);
        rlt.put("min", min);
        emsMeterSafetyEntity.setResult(rlt);
    }

    private void buildVoltage(User user, EmsMeterSafetyEntity emsMeterSafetyEntity) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = user.getCorpCode();
        String deviceId = emsMeterSafetyEntity.getDeviceId();
        Date qryDate = DateUtils.parseDate(emsMeterSafetyEntity.getQryDate());
        Date ofDayFirst = DateUtils.getOfDayFirst(qryDate);
        Date ofDayLast = DateUtils.getOfDayLast(qryDate);

        //组装查询参数
        ArrayList<Object> params = ListUtils.newArrayList(corpCode, deviceId, ofDayFirst, ofDayLast, TemporalGranularityEnum.VD_Quarter.getCode());
        Object[] args = params.toArray();

        //执行的sql语句
        String aMaxSql = "select max(a_phase_v) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, a_phase_v FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY a_phase_v desc ) tmp";
        String aMinSql = "select min(a_phase_v) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, a_phase_v FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY a_phase_v ) tmp";
        String bMaxSql = "select max(b_phase_v) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, b_phase_v FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY b_phase_v desc ) tmp";
        String bMinSql = "select min(b_phase_v) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, b_phase_v FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY b_phase_v ) tmp";
        String cMaxSql = "select max(c_phase_v) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, c_phase_v FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY c_phase_v desc ) tmp";
        String cMinSql = "select min(c_phase_v) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, c_phase_v FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY c_phase_v ) tmp";

        //查询结果处理
        ResultSetExtractor rse = new ResultSetExtractor<JSONObject>() {
            @Override
            public JSONObject extractData(ResultSet rs) throws SQLException, DataAccessException {
                JSONObject item = new JSONObject();
                if (rs.next()) {
                    Date time = rs.getDate("time");
                    double value = rs.getDouble("value");
                    item.put("value", value);
                    item.put("time", time);
                }
                return item;
            }
        };

        //多线程
        Map<String, Future<JSONObject>> threads = new LinkedHashMap<>();
        threads.put("aMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, aMaxSql, args, rse)));
        threads.put("aMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, aMinSql, args, rse)));
        threads.put("bMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, bMaxSql, args, rse)));
        threads.put("bMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, bMinSql, args, rse)));
        threads.put("cMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, cMaxSql, args, rse)));
        threads.put("cMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, cMinSql, args, rse)));

        JSONObject aMax = threads.get("aMax").get(20, TimeUnit.SECONDS);
        JSONObject aMin = threads.get("aMin").get(20, TimeUnit.SECONDS);
        JSONObject bMax = threads.get("bMax").get(20, TimeUnit.SECONDS);
        JSONObject bMin = threads.get("bMin").get(20, TimeUnit.SECONDS);
        JSONObject cMax = threads.get("cMax").get(20, TimeUnit.SECONDS);
        JSONObject cMin = threads.get("cMin").get(20, TimeUnit.SECONDS);

        JSONObject rlt = new JSONObject();
        rlt.put("aPhase", new EquipmentCurrentStatisticsEntity(aMax.getDouble("value"), aMax.getDate("time"), aMin.getDouble("value"), aMin.getDate("time")));
        rlt.put("bPhase", new EquipmentCurrentStatisticsEntity(bMax.getDouble("value"), bMax.getDate("time"), bMin.getDouble("value"), bMin.getDate("time")));
        rlt.put("cPhase", new EquipmentCurrentStatisticsEntity(cMax.getDouble("value"), cMax.getDate("time"), cMin.getDouble("value"), cMin.getDate("time")));
        emsMeterSafetyEntity.setResult(rlt);
    }

    private void buildCurrent(User user, EmsMeterSafetyEntity emsMeterSafetyEntity) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = user.getCorpCode();
        String deviceId = emsMeterSafetyEntity.getDeviceId();
        Date qryDate = DateUtils.parseDate(emsMeterSafetyEntity.getQryDate());
        Date ofDayFirst = DateUtils.getOfDayFirst(qryDate);
        Date ofDayLast = DateUtils.getOfDayLast(qryDate);

        //组装查询参数
        ArrayList<Object> params = ListUtils.newArrayList(corpCode, deviceId, ofDayFirst, ofDayLast, TemporalGranularityEnum.VD_Quarter.getCode());
        Object[] args = params.toArray();

        //执行的sql语句
        String aMaxSql = "select max(a_phase_a) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, a_phase_a FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY a_phase_a desc ) tmp";
        String aMinSql = "select min(a_phase_a) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, a_phase_a FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY a_phase_a ) tmp";
        String bMaxSql = "select max(b_phase_a) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, b_phase_a FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY b_phase_a desc ) tmp";
        String bMinSql = "select min(b_phase_a) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, b_phase_a FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY b_phase_a ) tmp";
        String cMaxSql = "select max(c_phase_a) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, c_phase_a FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY c_phase_a desc ) tmp";
        String cMinSql = "select min(c_phase_a) as value, ANY_VALUE(data_date_time) as time from ( select DISTINCT data_date_time, c_phase_a FROM ems_meter_collected_data WHERE corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ? ORDER BY c_phase_a ) tmp";

        //查询结果处理
        ResultSetExtractor rse = new ResultSetExtractor<JSONObject>() {
            @Override
            public JSONObject extractData(ResultSet rs) throws SQLException, DataAccessException {
                JSONObject item = new JSONObject();
                if (rs.next()) {
                    Date time = rs.getTimestamp("time");
                    double value = rs.getDouble("value");
                    item.put("value", value);
                    item.put("time", time);
                }
                return item;
            }
        };

        //多线程
        Map<String, Future<JSONObject>> threads = new LinkedHashMap<>();
        threads.put("aMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, aMaxSql, args, rse)));
        threads.put("aMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, aMinSql, args, rse)));
        threads.put("bMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, bMaxSql, args, rse)));
        threads.put("bMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, bMinSql, args, rse)));
        threads.put("cMax", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, cMaxSql, args, rse)));
        threads.put("cMin", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, cMinSql, args, rse)));

        JSONObject aMax = threads.get("aMax").get(20, TimeUnit.SECONDS);
        JSONObject aMin = threads.get("aMin").get(20, TimeUnit.SECONDS);
        JSONObject bMax = threads.get("bMax").get(20, TimeUnit.SECONDS);
        JSONObject bMin = threads.get("bMin").get(20, TimeUnit.SECONDS);
        JSONObject cMax = threads.get("cMax").get(20, TimeUnit.SECONDS);
        JSONObject cMin = threads.get("cMin").get(20, TimeUnit.SECONDS);

        JSONObject rlt = new JSONObject();
        rlt.put("aPhase", new EquipmentCurrentStatisticsEntity(aMax.getDouble("value"), aMax.getDate("time"), aMin.getDouble("value"), aMin.getDate("time")));
        rlt.put("bPhase", new EquipmentCurrentStatisticsEntity(bMax.getDouble("value"), bMax.getDate("time"), bMin.getDouble("value"), bMin.getDate("time")));
        rlt.put("cPhase", new EquipmentCurrentStatisticsEntity(cMax.getDouble("value"), cMax.getDate("time"), cMin.getDouble("value"), cMin.getDate("time")));
        emsMeterSafetyEntity.setResult(rlt);
    }

    private void buildActivePower(User user, EmsMeterSafetyEntity emsMeterSafetyEntity) throws ExecutionException, InterruptedException, TimeoutException {
        String corpCode = user.getCorpCode();
        String deviceId = emsMeterSafetyEntity.getDeviceId();
        Date qryDate = DateUtils.parseDate(emsMeterSafetyEntity.getQryDate());
        Date ofDayFirst = DateUtils.getOfDayFirst(qryDate);
        Date ofDayLast = DateUtils.getOfDayLast(qryDate);

        //组装查询参数
        ArrayList<Object> params = ListUtils.newArrayList(corpCode, deviceId, ofDayFirst, ofDayLast, TemporalGranularityEnum.VD_Quarter.getCode());
        Date of30DayFirst = DateUtils.calculateDay(ofDayFirst, -30);
        ArrayList<Object> day30Params = ListUtils.newArrayList(corpCode, deviceId, of30DayFirst, ofDayLast, TemporalGranularityEnum.VD_Quarter.getCode());

        //执行的sql语句
        String averageSql = "select avg(ifnull(total_ap, 0)) as value from ems_meter_collected_data where corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ?";
        String maxSql = "select max(total_ap) as value from ems_meter_collected_data where corp_code = ? and device_id = ? and data_date_time > ? and data_date_time <= ? and data_type = ?";

        Object[] args = params.toArray();
        Object[] day30Args = day30Params.toArray();

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

        //多线程
        Map<String, Future<Double>> threads = new LinkedHashMap<>();
        threads.put("average", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, averageSql, args, rse)));
        threads.put("max", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, maxSql, args, rse)));
        threads.put("day30average", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, averageSql, day30Args, rse)));
        threads.put("day30Max", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, maxSql, day30Args, rse)));

        //有功功率
        Double average = threads.get("average").get(20, TimeUnit.SECONDS);
        Double max = threads.get("max").get(20, TimeUnit.SECONDS);
        Double coefficient = NumberUtils.mul(NumberUtils.div(average, max, 2), 100);

        Double day30average = threads.get("day30average").get(20, TimeUnit.SECONDS);
        Double day30Max = threads.get("day30Max").get(20, TimeUnit.SECONDS);
        Double day30Coefficient = NumberUtils.mul(NumberUtils.div(day30average, day30Max, 2), 100);
        JSONObject rlt = new JSONObject();
        rlt.put("average", average);
        rlt.put("apMax", max);
        rlt.put("powerCoefficient", coefficient);
        rlt.put("day30average", day30average);
        rlt.put("day30ApMax", day30Max);
        rlt.put("day30PowerCoefficient", day30Coefficient);
        emsMeterSafetyEntity.setResult(rlt);
    }

    public EChart meterSafetyEChart(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        User user = UserHelper.getUser();
        MeterStatusFieldEnum meterStatusField = emsMeterSafetyEntity.getMeterStatusField();
        EChart eChart = null;
        switch (meterStatusField) {
            case ACTIVE_POWER:
                //有功功率
                eChart = buildActivePowerEChart(emsMeterSafetyEntity);
                break;
            case REACTIVE_POWER:
                //无功功率
                eChart = buildReactivePowerEChart(emsMeterSafetyEntity);
                break;
            case CURRENT:
                //电流
                eChart = buildCurrentEChart(emsMeterSafetyEntity);
                break;
            case VOLTAGE:
                //电压
                eChart = buildVoltageEChart(emsMeterSafetyEntity);
                break;
            case POWER_FACTOR:
                //功率因数
                eChart = buildPowerFactorEChart(emsMeterSafetyEntity);
                break;
        }
        return eChart;
    }

    private EChart buildPowerFactorEChart(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        List<EmsElectricPowerConsumption> consumptionList = getPowerConsumptionList(emsMeterSafetyEntity);
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> total = new ArrayList<>();
        List<Double> aPhase = new ArrayList<>();
        List<Double> bPhase = new ArrayList<>();
        List<Double> cPhase = new ArrayList<>();
        consumptionList.stream().forEach(o -> {
            String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd HH:mm");
            xAxes.add(label);
            total.add(o.getTotalPowerFactor());
            aPhase.add(o.getAphasePowerFactor());
            bPhase.add(o.getBphasePowerFactor());
            cPhase.add(o.getCphasePowerFactor());
        });

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("总功率因数", total),
                new EChartItem("A相功率因数", aPhase),
                new EChartItem("B相功率因数", bPhase),
                new EChartItem("C相功率因数", cPhase)));
        return EChart.builder().body(body).build();
    }

    private EChart buildVoltageEChart(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        List<EmsElectricPowerConsumption> consumptionList = getPowerConsumptionList(emsMeterSafetyEntity);
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> aPhase = new ArrayList<>();
        List<Double> bPhase = new ArrayList<>();
        List<Double> cPhase = new ArrayList<>();
        consumptionList.stream().forEach(o -> {
            String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd HH:mm");
            xAxes.add(label);
            aPhase.add(o.getAphaseVoltage());
            bPhase.add(o.getBphaseVoltage());
            cPhase.add(o.getCphaseVoltage());
        });

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("A相电压", aPhase),
                new EChartItem("B相电压", bPhase),
                new EChartItem("C相电压", cPhase)));
        return EChart.builder().body(body).build();
    }

    private EChart buildCurrentEChart(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        List<EmsElectricPowerConsumption> consumptionList = getPowerConsumptionList(emsMeterSafetyEntity);
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> aPhase = new ArrayList<>();
        List<Double> bPhase = new ArrayList<>();
        List<Double> cPhase = new ArrayList<>();
        consumptionList.stream().forEach(o -> {
            String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd HH:mm");
            xAxes.add(label);
            aPhase.add(o.getAphaseCurrent());
            bPhase.add(o.getBphaseCurrent());
            cPhase.add(o.getCphaseCurrent());
        });

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("A相电流", aPhase),
                new EChartItem("B相电流", bPhase),
                new EChartItem("C相电流", cPhase)));
        return EChart.builder().body(body).build();
    }

    private EChart buildReactivePowerEChart(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        List<EmsElectricPowerConsumption> consumptionList = getPowerConsumptionList(emsMeterSafetyEntity);
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> total = new ArrayList<>();
        List<Double> aPhase = new ArrayList<>();
        List<Double> bPhase = new ArrayList<>();
        List<Double> cPhase = new ArrayList<>();
        consumptionList.stream().forEach(o -> {
            String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd HH:mm");
            xAxes.add(label);
            total.add(o.getTotalReactivePower());
            aPhase.add(o.getAphaseReactivePower());
            bPhase.add(o.getBphaseReactivePower());
            cPhase.add(o.getCphaseReactivePower());
        });

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("总无功功率", total),
                new EChartItem("A相无功功率", aPhase),
                new EChartItem("B相无功功率", bPhase),
                new EChartItem("C相无功功率", cPhase)));
        return EChart.builder().body(body).build();
    }

    private EChart buildActivePowerEChart(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        List<EmsElectricPowerConsumption> consumptionList = getPowerConsumptionList(emsMeterSafetyEntity);
        List<String> xAxes = ListUtils.newArrayList();
        List<Double> total = new ArrayList<>();
        List<Double> aPhase = new ArrayList<>();
        List<Double> bPhase = new ArrayList<>();
        List<Double> cPhase = new ArrayList<>();
        consumptionList.stream().forEach(o -> {
            String label = DateUtils.formatDate(o.getDataDateTime(), "MM-dd HH:mm");
            xAxes.add(label);
            total.add(o.getTotalActivePower());
            aPhase.add(o.getAphaseActivePower());
            bPhase.add(o.getBphaseActivePower());
            cPhase.add(o.getCphaseActivePower());
        });

        //组装数据
        EChartBody body = new EChartBody();
        body.setX(xAxes);
        body.setY(ListUtils.newArrayList(
                new EChartItem("总有功功率", total),
                new EChartItem("A相有功功率", aPhase),
                new EChartItem("B相有功功率", bPhase),
                new EChartItem("C相有功功率", cPhase)));
        return EChart.builder().body(body).build();
    }

    private List<EmsElectricPowerConsumption> getPowerConsumptionList(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        Date qryDate = DateUtils.parseDate(emsMeterSafetyEntity.getQryDate());
        Date ofDayFirst = DateUtils.getOfDayFirst(qryDate);
        Date ofDayLast = DateUtils.getOfDayLast(qryDate);

        EmsElectricPowerConsumption where = new EmsElectricPowerConsumption();
        where.setDeviceId(emsMeterSafetyEntity.getDeviceId());
        where.setQryStartTime(ofDayFirst);
        where.setQryEndTime(ofDayLast);
        where.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
        return emsElectricPowerConsumptionService.findList(where);
    }

    public Page<EmsAlarmRecordEntity> meterSafetyRecPageList(EmsMeterSafetyEntity emsMeterSafetyEntity, HttpServletRequest request, HttpServletResponse response) {
        Page<EmsAlarmRecordEntity> page = new Page<>(request, response);
        emsMeterSafetyEntity.setPage(page);
        page.setList(this.meterSafetyRecListData(emsMeterSafetyEntity));
        return page;
    }

    public List<EmsAlarmRecordEntity> meterSafetyRecListData(EmsMeterSafetyEntity emsMeterSafetyEntity) {
        return this.dao.meterSafetyRecListData(emsMeterSafetyEntity);
    }

    public String meterSafetyRecListExp(EmsMeterSafetyEntity emsMeterSafetyEntity) throws IOException {
        String presignedUrl = null;
        List<EmsAlarmRecordEntity> list = this.meterSafetyRecListData(emsMeterSafetyEntity);
        String fileName = "设备报警记录导出" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try (ExcelExport ee = new ExcelExport("设备报警记录导出", EmsAlarmRecordEntity.class)) {
            presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(list), fileName);
        }
        return presignedUrl;
    }

    /**
     * 分页查询检测报警记录
     * @param emsAlarmEvent
     * @param request
     * @param response
     * @return
     */
    public Page<EmsAlarmEvent> meterSafetyRecord(EmsAlarmEvent emsAlarmEvent, HttpServletRequest request, HttpServletResponse response) {
        Page<EmsAlarmEvent> page = new Page<>(request, response);
        emsAlarmEvent.setPage(page);
        page.setList(emsAlarmEventService.findList(emsAlarmEvent));
        return page;
    }

    public List<EmsAlarmEvent> meterSafetyRecordExport(EmsAlarmEvent emsAlarmEvent) {
        return emsAlarmEventService.findList(emsAlarmEvent);
    }
}
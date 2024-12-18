package com.jeesite.modules.ems.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.cecec.api.redis.RedisKeyUtil;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.ems.api.EmsElectricPowerConsumptionServiceApi;
import com.jeesite.modules.ems.dao.EmsElectricPowerConsumptionDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.support.adapt.GlobalCalculateAdapter;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import com.jeesite.modules.sys.utils.UserHelper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

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
 * 电耗表Service
 *
 * @author 李鹏
 * @version 2023-05-25
 */
@Service
@RestController
@Transactional(readOnly = true)
public class EmsElectricPowerConsumptionService extends CrudService<EmsElectricPowerConsumptionDao, EmsElectricPowerConsumption>
        implements EmsElectricPowerConsumptionServiceApi {
    // 电能
    public static final String POWER = "Power";
    // 电压
    public static final String VOLTAGE = "Voltage";
    // 电流
    public static final String CURRENT = "Current";

    @Resource
    private RedisService redisService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取单条数据
     *
     * @param emsElectricPowerConsumption
     * @return
     */
    @Override
    public EmsElectricPowerConsumption get(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        return super.get(emsElectricPowerConsumption);
    }

    /**
     * 查询分页数据
     *
     * @param emsElectricPowerConsumption 查询条件
     * @return
     */
    @Override
    public Page<EmsElectricPowerConsumption> findPage(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        return super.findPage(emsElectricPowerConsumption);
    }

    /**
     * 查询列表数据
     *
     * @param emsElectricPowerConsumption
     * @return
     */
    @Override
    public List<EmsElectricPowerConsumption> findList(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        return super.findList(emsElectricPowerConsumption);
    }

    /**
     * 查询根据电表排序顺序的数据列表
     *
     * @param emsElectricPowerConsumption
     * @return
     */
    public List<EmsElectricPowerConsumption> findMeterSortList(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        return this.dao.findMeterSortList(emsElectricPowerConsumption);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsElectricPowerConsumption
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void save(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        super.save(emsElectricPowerConsumption);
    }

    /**
     * 更新状态
     *
     * @param emsElectricPowerConsumption
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void updateStatus(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        super.updateStatus(emsElectricPowerConsumption);
    }

    /**
     * 删除数据
     *
     * @param emsElectricPowerConsumption
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void delete(EmsElectricPowerConsumption emsElectricPowerConsumption) {
        super.delete(emsElectricPowerConsumption);
    }

    public List<Date> getStockPendulum(String execDate, TemporalGranularityEnum temporalGranularityEnum) {
        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setDataDate(DateUtils.parseDate(execDate));
        params.setDataType(temporalGranularityEnum.getCode());
        return this.dao.getStockPendulum(params);
    }

    public EmsElectricPowerConsumption isStockedRec(EmsElectricPowerConsumption eepc) {
        return this.dao.isStockedRec(eepc);
    }

    /**
     * 查询时间段内电耗数据
     *
     * @param entity
     * @return
     */
    public Double getStageCumulativeConsumption(StageCumulativeQueryEntity entity) {
        if (StringUtils.isEmpty(entity.getCompanyCode())){
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }

        return this.dao.getStageCumulativeConsumption(entity);
    }

    public List<EChartData> getStageConsumption2EChart(StageCumulativeQueryEntity entity) {
        if (StringUtils.isEmpty(entity.getCompanyCode())){
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }
        return this.dao.getStageConsumption2EChart(entity);
    }

    public List<EChartData> consumptionRanking(EmsElectricPowerConsumption entity) {
        if (StringUtils.isEmpty(entity.getCompanyCode())) {
            User user = UserHelper.getUser();
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            entity.setCompanyCode(company.getCompanyCode());
            entity.setCorpCode(user.getCorpCode());
        }
        return this.dao.consumptionRanking(entity);
    }

    public List<EmsElectricPowerConsumption> activeEnergyEChart(EmsElectricPowerConsumption params) {
        return this.dao.activeEnergyEChart(params);
    }

    public EmsElectricPowerAreaConsumption getAreaStageCumulativeConsumption(EmsElectricPowerConsumption params) {
        return this.dao.getAreaStageCumulativeConsumption(params);
    }

    /**
     * 设备有功无功功率查询
     * @param deviceStatisticsParam
     */
    public EmsDevicePowerEntity deviceActiveAndReactivePower(DeviceStatisticsParam deviceStatisticsParam) throws ExecutionException, InterruptedException, TimeoutException {
        EmsDevicePowerEntity result = new EmsDevicePowerEntity();
        // 当日实时值
        EmsElectricPowerConsumption emsElectricPowerConsumption = this.getLatestEquipmentStatus(deviceStatisticsParam.getDeviceId());
        if(!ObjectUtil.isEmpty(emsElectricPowerConsumption)){
            result.setCurrentData(emsElectricPowerConsumption.getPositiveActiveEnergy());
            result.setCurrentDataRe(emsElectricPowerConsumption.getReverseActivePower());
        }
        // 多线程获取最大值和最小值
        Map<String, Future<Double>> threads = new LinkedHashMap<>();
        // SQL
        String maxSql = "SELECT MAX(positive_active_energy) as value FROM ems_electric_power_consumption a where  a.device_id = ? and a.data_date_time between ? and ?";
        String minSql = "SELECT MIN(positive_active_energy) as value FROM ems_electric_power_consumption a where  a.device_id = ? and a.data_date_time between ? and ?";
        String maxReSql = "SELECT MAX(reverse_active_power) as value FROM ems_electric_power_consumption a where  a.device_id = ? and a.data_date_time between ? and ?";
        String minReSql = "SELECT MIN(reverse_active_power) as value FROM ems_electric_power_consumption a where  a.device_id = ? and a.data_date_time between ? and ?";

        ArrayList<Object> params = new ArrayList<>();
        // 参数
        params.add(deviceStatisticsParam.getDeviceId());
        params.add(deviceStatisticsParam.getStart());
        params.add(deviceStatisticsParam.getEnd());
        Object[] args = params.toArray();
        // 结果类型
        ResultSetExtractor rse = new ResultSetExtractor<Double>() {
            @Override
            public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getDouble("value");
                }
                return 0d;
            }
        };
        // 多线程执行
        threads.put("maxPositiveActiveEnergy", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, maxSql, args, rse)));
        threads.put("minPositiveActiveEnergy", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, minSql, args, rse)));
        threads.put("maxReverseActivePower", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, maxReSql, args, rse)));
        threads.put("minReverseActivePower", threadPoolTaskExecutor.submit(new SqlExecuteCallable<Double>(jdbcTemplate, minReSql, args, rse)));

        Double max = threads.get("maxPositiveActiveEnergy").get(20, TimeUnit.SECONDS);
        Double min = threads.get("minPositiveActiveEnergy").get(20, TimeUnit.SECONDS);
        Double maxRe = threads.get("maxReverseActivePower").get(20, TimeUnit.SECONDS);
        Double minRe = threads.get("minReverseActivePower").get(20, TimeUnit.SECONDS);
        result.setMax(max);
        result.setMin(min);
        result.setDiff(NumberUtils.sub(max,min));
        result.setMaxRe(maxRe);
        result.setMinRe(minRe);
        result.setDiffRe(NumberUtils.sub(maxRe,minRe));
        return result;
    }


    /**
     * 获取该设备最新的一条记录
     * @param param
     * @return
     */
    public EmsElectricPowerConsumption getLastRecord(EmsElectricPowerConsumption param) {
        return this.dao.getLastRecord(param);
    }

    /**
     * 获取该设备最新的一条记录
     * @param deviceId
     * @return
     */
    public EmsElectricPowerConsumption getLatestEquipmentStatus(String deviceId) {
        // 缓存取
        Object obj = redisService.get(RedisKeyUtil.deviceCurrentPower(deviceId));
        if(ObjectUtil.isEmpty(obj)){
            return null;
        }
        String text = obj.toString();
        if(StringUtils.hasText(text)){
            EmsElectricPowerConsumption emsElectricPowerConsumption = JSONObject.parseObject(text, EmsElectricPowerConsumption.class);
            return emsElectricPowerConsumption;
        }
        // 数据库取
        EmsElectricPowerConsumption param = new EmsElectricPowerConsumption();
        param.setDeviceId(deviceId);
        param.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
        param.setDataDate(DateUtils.parseDate(DateUtils.formatDate(new Date())));
        return this.dao.getLastRecord(param);
    }

    /**
     * 设备功率电压电流柱状图
     * @param deviceStatisticsParam
     * @return
     */
    public HomePageEntity activeAndReactivePowerChart(DeviceStatisticsParam deviceStatisticsParam) {
        HomePageEntity homePageEntity = new HomePageEntity();
        EmsElectricPowerConsumption emsElectricPowerConsumption = new EmsElectricPowerConsumption();
        emsElectricPowerConsumption.setDeviceId(deviceStatisticsParam.getDeviceId());
        emsElectricPowerConsumption.setQryStartTime(deviceStatisticsParam.getStart());
        emsElectricPowerConsumption.setQryEndTime(deviceStatisticsParam.getEnd());
        emsElectricPowerConsumption.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
        emsElectricPowerConsumption.getSqlMap().getOrder().setOrderBy("a.data_date_time asc");
        List<EmsElectricPowerConsumption> list = this.findList(emsElectricPowerConsumption);
        List<String> dateList = ListUtils.newArrayList();
        // 总有功
        List<Double> positiveActiveEnergy = ListUtils.newArrayList();
        // 总无功
        List<Double> positiveReactiveEnergy = ListUtils.newArrayList();
        // A相电压
        List<Double> aPhaseVoltage = ListUtils.newArrayList();
        // B相电压
        List<Double> bPhaseVoltage = ListUtils.newArrayList();
        // C相电压
        List<Double> cPhaseVoltage = ListUtils.newArrayList();
        // 三相电压不平衡度
        List<Double> voltageUnbalanceDegree = ListUtils.newArrayList();
        // A相电流
        List<Double> aPhaseCurrent = ListUtils.newArrayList();
        // B相电流
        List<Double> bPhaseCurrent = ListUtils.newArrayList();
        // C相电流
        List<Double> cPhaseCurrent = ListUtils.newArrayList();
        // 三相电流不平衡度
        List<Double> currentUnbalanceDegree = ListUtils.newArrayList();

        list.stream().forEach(obj -> {
            String label = DateUtils.formatDate(obj.getDataDateTime(),"yyyy-MM-dd HH:mm");
            //组装X轴
            dateList.add(label);
            //组装Y轴
            positiveActiveEnergy.add(obj.getPositiveActiveEnergy());
            positiveReactiveEnergy.add(obj.getPositiveReactiveEnergy());
            aPhaseVoltage.add(obj.getAphaseVoltage());
            bPhaseVoltage.add(obj.getBphaseVoltage());
            cPhaseVoltage.add(obj.getCphaseVoltage());
            voltageUnbalanceDegree.add(obj.getVoltageUnbalanceDegree());
            aPhaseCurrent.add(obj.getAphaseCurrent());
            bPhaseCurrent.add(obj.getBphaseCurrent());
            cPhaseCurrent.add(obj.getCphaseCurrent());
            currentUnbalanceDegree.add(obj.getCurrentUnbalanceDegree());
        });
        //组装数据
        EChartBody body = new EChartBody();
        body.setX(dateList);
        if(deviceStatisticsParam.getTemporalGranularity().equals(POWER)){
            body.setY(ListUtils.newArrayList(
                    new EChartItem("总有功功率", positiveActiveEnergy),
                    new EChartItem("总无功功率", positiveReactiveEnergy)));
        }
        if(deviceStatisticsParam.getTemporalGranularity().equals(VOLTAGE)){
            body.setY(ListUtils.newArrayList(
                    new EChartItem("A相电压", aPhaseVoltage),
                    new EChartItem("B相电压", bPhaseVoltage),
                    new EChartItem("C相电压", cPhaseVoltage),
                    new EChartItem("三相电压不平衡度", voltageUnbalanceDegree)));
        }
        if(deviceStatisticsParam.getTemporalGranularity().equals(CURRENT)){
            body.setY(ListUtils.newArrayList(
                    new EChartItem("A相电流", aPhaseCurrent),
                    new EChartItem("B相电流", bPhaseCurrent),
                    new EChartItem("C相电流", cPhaseCurrent),
                    new EChartItem("三相电流不平衡度", currentUnbalanceDegree)));
        }

        EChart eChart = EChart.builder().body(body).build();
        homePageEntity.setEChart(eChart);
        return homePageEntity;
    }

    /**
     * 电耗表参数历史记录
     * @param deviceStatisticsParam
     * @param request
     * @param response
     * @return
     */
    public Page<EmsElectricPowerConsumption> recordList(DeviceStatisticsParam deviceStatisticsParam, HttpServletRequest request, HttpServletResponse response) {
        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setDeviceId(deviceStatisticsParam.getDeviceId());
        params.setQryStartTime(deviceStatisticsParam.getStart());
        params.setQryEndTime(deviceStatisticsParam.getEnd());
        params.setDataType(TemporalGranularityEnum.VD_Quarter.getCode());
        params.getSqlMap().getOrder().setOrderBy("a.data_date_time desc");
        params.setPage(new Page<EmsTimeShareDeviceRuntime>(request, response));
        Page<EmsElectricPowerConsumption> page = this.findPage(params);
        return page;
    }

    /**
     * 电耗表参数记录导出
     * @param deviceStatisticsParam
     * @return
     */
    public String export(DeviceStatisticsParam deviceStatisticsParam) throws IOException {
        String presignedUrl = null;
        EmsElectricPowerConsumption params = new EmsElectricPowerConsumption();
        params.setDeviceId(deviceStatisticsParam.getDeviceId());
        params.setQryStartTime(deviceStatisticsParam.getStart());
        params.setQryEndTime(deviceStatisticsParam.getEnd());
        params.getSqlMap().getOrder().setOrderBy("a.data_date_time desc");
        List<EmsElectricPowerConsumption> list = this.findList(params);
        if(CollectionUtils.isEmpty(list)){
            return "";
        }
        // 电能导出
        if(deviceStatisticsParam.getTemporalGranularity().equals(POWER)){
            List<EmsElectricPowerExport> powerExportList = new ArrayList<>();
            list.forEach(en->{
                EmsElectricPowerExport emsElectricPowerExport = new EmsElectricPowerExport();
                emsElectricPowerExport.setDataDateTime(en.getDataDateTime());
                emsElectricPowerExport.setAphaseReactivePower(en.getAphaseReactivePower());
                emsElectricPowerExport.setBphaseReactivePower(en.getBphaseReactivePower());
                emsElectricPowerExport.setCphaseReactivePower(en.getCphaseReactivePower());
                emsElectricPowerExport.setDeviceName(en.getDeviceName());
                emsElectricPowerExport.setTotalActivePower(en.getTotalActivePower());
                emsElectricPowerExport.setTotalReactivePower(en.getTotalReactivePower());
                emsElectricPowerExport.setAphaseActivePower(en.getAphaseActivePower());
                emsElectricPowerExport.setBphaseActivePower(en.getBphaseActivePower());
                emsElectricPowerExport.setCphaseActivePower(en.getCphaseActivePower());
                powerExportList.add(emsElectricPowerExport);
            });
            String fileName = "设备有功无功能耗历史记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            try (ExcelExport ee = new ExcelExport("设备有功无功能耗历史记录导出", EmsElectricPowerExport.class)) {
                presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(powerExportList), fileName);
            }
        }

        // 电压导出
        if(deviceStatisticsParam.getTemporalGranularity().equals(VOLTAGE)){
            List<EmsElectricVoltageExport> voltageExportList = new ArrayList<>();
            list.forEach(en->{
                EmsElectricVoltageExport emsElectricVoltageExport = new EmsElectricVoltageExport();
                emsElectricVoltageExport.setDeviceName(en.getDeviceName());
                emsElectricVoltageExport.setDataDateTime(en.getDataDateTime());
                emsElectricVoltageExport.setAphaseVoltage(en.getAphaseVoltage());
                emsElectricVoltageExport.setBphaseVoltage(en.getBphaseVoltage());
                emsElectricVoltageExport.setCphaseVoltage(en.getCphaseVoltage());
                emsElectricVoltageExport.setVoltageUnbalanceDegree(en.getVoltageUnbalanceDegree());
                voltageExportList.add(emsElectricVoltageExport);
            });
            String fileName = "设备电压历史参数记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            try (ExcelExport ee = new ExcelExport("设备电压历史参数记录", EmsElectricVoltageExport.class)) {
                presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(voltageExportList), fileName);
            }
        }

        // 电流导出
        if(deviceStatisticsParam.getTemporalGranularity().equals(CURRENT)){
            List<EmsElectricCurrentExport> currentExportList = new ArrayList<>();
            list.forEach(en->{
                EmsElectricCurrentExport emsElectricCurrentExport = new EmsElectricCurrentExport();
                emsElectricCurrentExport.setDeviceName(en.getDeviceName());
                emsElectricCurrentExport.setDataDateTime(en.getDataDateTime());
                emsElectricCurrentExport.setAphaseCurrent(en.getAphaseCurrent());
                emsElectricCurrentExport.setBphaseCurrent(en.getBphaseCurrent());
                emsElectricCurrentExport.setCphaseCurrent(en.getCphaseCurrent());
                emsElectricCurrentExport.setCurrentUnbalanceDegree(en.getCurrentUnbalanceDegree());
                currentExportList.add(emsElectricCurrentExport);
            });
            String fileName = "设备电流历史参数记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            try (ExcelExport ee = new ExcelExport("设备电流历史参数记录", EmsElectricCurrentExport.class)) {
                presignedUrl = ExcelExportUtil.uploadOss(ee.setDataList(currentExportList), fileName);
            }
        }


        return presignedUrl;
    }
}
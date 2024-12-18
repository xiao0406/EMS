package com.jeesite.modules.ems.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.entity.SheetHead;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsTerminalDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.utils.ExcelExportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 终端表Service
 *
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly = true)
public class EmsStatisticsReportService extends CrudService<EmsTerminalDao, EmsTerminal> {

    @Resource
    private EmsElectricPowerConsumptionService emsElectricPowerConsumptionService;
    @Resource
    private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;
    @Resource
    private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;
    @Resource
    private EmsTimeSharePowerConsumptionStatisticsService emsTimeSharePowerConsumptionStatisticsService;
    @Resource
    private EmsTimeShareDeviceRuntimeService emsTimeShareDeviceRuntimeService;
    @Resource
    private EmsTimeShareDeviceRuntimeStatisticsService emsTimeShareDeviceRuntimeStatisticsService;
    @Resource
    private EmsTimeShareOfficeConsumptionService emsTimeShareOfficeConsumptionService;
    @Resource
    private EmsTimeShareOfficeConsumptionStatisticsService emsTimeShareOfficeConsumptionStatisticsService;
    @Resource
    private EmsMeterCollectedDataService emsMeterCollectedDataService;

    public EmsStatisticsReportEntity meterElectricConsumption(EmsStatisticsReportEntity emsStatisticsReportEntity) {
//            获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }

        TemporalGranularityEnum temporalGranularity = emsStatisticsReportEntity.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Day:
                //查询数据列表
                EmsElectricPowerConsumption eepcParams = new EmsElectricPowerConsumption();
                //数据类型
                eepcParams.setDataType(temporalGranularity.getCode());
                //查询开始时间
                eepcParams.setQryStartTime(DateUtils.parseDate(emsStatisticsReportEntity.getQryStartTime()));
                //查询结束时间
                eepcParams.setQryEndTime(DateUtils.parseDate(emsStatisticsReportEntity.getQryEndTime()));
                eepcParams.setCompanyCode(companyCode);
                //数据列表
                List<EmsElectricPowerConsumption> vdDay = emsElectricPowerConsumptionService.findMeterSortList(eepcParams);
                ArrayList<SheetHead> dayHeads = new ArrayList<>();
                dayHeads.add(new SheetHead("设备名称"));
                dayHeads.add(new SheetHead("用电量（Kwh）"));
                final boolean[] dayAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdDay.stream().collect(
                                Collectors.groupingBy(EmsElectricPowerConsumption::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsElectricPowerConsumption> consumptionList = obj.getValue();

                            Double totalEnergy = 0d;
                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("用电量（Kwh）", totalEnergy);

                            for (EmsElectricPowerConsumption consumption : consumptionList) {
                                //计算总电量
                                totalEnergy = NumberUtils.add(totalEnergy, consumption.getPositiveActiveEnergy());
                                String key = DateUtils.formatDate(consumption.getDataDate(), "MM-dd");
                                item.put(key, consumption.getPositiveActiveEnergy());
                                if (dayAddHead[0]) {
                                    dayHeads.add(new SheetHead(key));
                                }
                            }
                            dayAddHead[0] = false;
                            item.put("用电量（Kwh）", totalEnergy);
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(dayHeads);
                break;
            case VD_Month:
                //查询数据列表
                EmsElectricPowerConsumptionStatistics eepcsParams = new EmsElectricPowerConsumptionStatistics();
                //数据类型
                eepcsParams.setDataType(temporalGranularity.getCode());
                //查询开始时间
                eepcsParams.setQryStartTime(emsStatisticsReportEntity.getQryStartTime());
                //查询结束时间
                eepcsParams.setQryEndTime(emsStatisticsReportEntity.getQryEndTime());
                eepcsParams.setCompanyCode(companyCode);
                //数据列表
                List<EmsElectricPowerConsumptionStatistics> vdMonth = emsElectricPowerConsumptionStatisticsService.findMeterSortList(eepcsParams);
                ArrayList<SheetHead> monthHeads = new ArrayList<>();
                monthHeads.add(new SheetHead("设备名称"));
                monthHeads.add(new SheetHead("用电量（Kwh）"));
                final boolean[] monthAddHead = {true};
                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdMonth.stream().collect(
                                Collectors.groupingBy(EmsElectricPowerConsumptionStatistics::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsElectricPowerConsumptionStatistics> consumptionList = obj.getValue();

                            Double totalEnergy = 0d;
                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("用电量（Kwh）", totalEnergy);

                            for (EmsElectricPowerConsumptionStatistics consumption : consumptionList) {
                                //计算总电量
                                totalEnergy = NumberUtils.add(totalEnergy, consumption.getPositiveActiveEnergy());
                                String key = consumption.getDataDateKey();
                                item.put(key, consumption.getPositiveActiveEnergy());
                                if (monthAddHead[0]) {
                                    monthHeads.add(new SheetHead(key));
                                }
                            }
                            monthAddHead[0] = false;
                            item.put("用电量（Kwh）", totalEnergy);
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(monthHeads);
                break;
            default:
                emsStatisticsReportEntity.setData(new ArrayList<>());
                break;
        }
        return emsStatisticsReportEntity;
    }

    @Transactional(readOnly = false)
    public String meterElectricConsumptionExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        EmsStatisticsReportEntity rltEntity = meterElectricConsumption(emsStatisticsReportEntity);

        //转换表头
        List<SheetHead> heads = rltEntity.getHeads();
        String[] excelHeader = heads.stream().map(o -> {
            return o.getLabel();
        }).collect(Collectors.toList()).toArray(new String[0]);

        //转换数据
        List<JSONObject> data = rltEntity.getData();
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            for (int i = 0; i < excelHeader.length; i++) {
                rowData[i] = o.get(excelHeader[i]);
            }
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, null, excelHeader, dataList);
    }

    public EmsStatisticsReportEntity meterTimeShareConsumption(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        //获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }

        TemporalGranularityEnum temporalGranularity = emsStatisticsReportEntity.getTemporalGranularity();
        String qryType = emsStatisticsReportEntity.getQryType();

        switch (temporalGranularity) {
            case VD_Day:
                //查询数据列表
                EmsTimeSharePowerConsumption etspc = new EmsTimeSharePowerConsumption();
                //数据类型
                etspc.setDataType(temporalGranularity.getCode());
                //查询开始时间
                etspc.setDataDateStart(DateUtils.parseDate(emsStatisticsReportEntity.getQryStartTime()));
                //查询结束时间
                etspc.setDataDateEnd(DateUtils.parseDate(emsStatisticsReportEntity.getQryEndTime()));
                etspc.setCompanyCode(companyCode);
                //数据列表
                List<EmsTimeSharePowerConsumption> vdDay = emsTimeSharePowerConsumptionService.findMeterSortList(etspc);
                ArrayList<SheetHead> dayHeads = new ArrayList<>();
                dayHeads.add(new SheetHead("设备名称"));
                dayHeads.add(new SheetHead("总电量（Kwh）",
                        Arrays.asList(
                                new SheetHead("总"),
                                new SheetHead("尖"),
                                new SheetHead("峰"),
                                new SheetHead("平"),
                                new SheetHead("谷"))));
                final boolean[] dayAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdDay.stream().collect(
                                Collectors.groupingBy(EmsTimeSharePowerConsumption::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsTimeSharePowerConsumption> consumptionList = obj.getValue();

                            Double total = 0d;
                            Double cusp = 0d;
                            Double peak = 0d;
                            Double fair = 0d;
                            Double valley = 0d;
                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("总电量（Kwh）", packageEnergy(qryType, total, cusp, peak, fair, valley));

                            for (EmsTimeSharePowerConsumption consumption : consumptionList) {
                                Double totalEnergy = consumption.getTotalEnergy();
                                Double cuspEnergy = consumption.getCuspTimeEnergy();
                                Double peakEnergy = consumption.getPeakTimeEnergy();
                                Double fairEnergy = consumption.getFairTimeEnergy();
                                Double valleyEnergy = consumption.getValleyTimeEnergy();

                                //计算总电量
                                total = NumberUtils.add(total, totalEnergy);
                                cusp = NumberUtils.add(cusp, cuspEnergy);
                                peak = NumberUtils.add(peak, peakEnergy);
                                fair = NumberUtils.add(fair, fairEnergy);
                                valley = NumberUtils.add(valley, valleyEnergy);

                                //封装对象
                                String key = DateUtils.formatDate(consumption.getDataDate(), "MM-dd");
                                item.put(key, packageEnergy(qryType, totalEnergy, cuspEnergy, peakEnergy, fairEnergy, valleyEnergy));
                                if (dayAddHead[0]) {
                                    dayHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总"),
                                                    new SheetHead("尖"),
                                                    new SheetHead("峰"),
                                                    new SheetHead("平"),
                                                    new SheetHead("谷"))));
                                }
                            }
                            dayAddHead[0] = false;
                            item.put("总电量（Kwh）", packageEnergy(qryType, total, cusp, peak, fair, valley));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(dayHeads);
                break;
            case VD_Month:
                //查询数据列表
                EmsTimeSharePowerConsumptionStatistics etspcs = new EmsTimeSharePowerConsumptionStatistics();
                //数据类型
                etspcs.setDataType(temporalGranularity.getCode());
                //查询开始时间
                etspcs.setQryStartTime(emsStatisticsReportEntity.getQryStartTime());
                //查询结束时间
                etspcs.setQryEndTime(emsStatisticsReportEntity.getQryEndTime());
                etspcs.setCompanyCode(companyCode);
                //数据列表
                List<EmsTimeSharePowerConsumptionStatistics> vdMonth = emsTimeSharePowerConsumptionStatisticsService.findList(etspcs);
                ArrayList<SheetHead> monthHeads = new ArrayList<>();
                monthHeads.add(new SheetHead("设备名称"));
                monthHeads.add(new SheetHead("总电量（Kwh）",
                        Arrays.asList(
                                new SheetHead("总"),
                                new SheetHead("尖"),
                                new SheetHead("峰"),
                                new SheetHead("平"),
                                new SheetHead("谷"))));
                final boolean[] monthAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdMonth.stream().collect(
                                Collectors.groupingBy(EmsTimeSharePowerConsumptionStatistics::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsTimeSharePowerConsumptionStatistics> consumptionList = obj.getValue();

                            Double total = 0d;
                            Double cusp = 0d;
                            Double peak = 0d;
                            Double fair = 0d;
                            Double valley = 0d;
                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("总电量（Kwh）", packageEnergy(qryType, total, cusp, peak, fair, valley));

                            for (EmsTimeSharePowerConsumptionStatistics consumption : consumptionList) {
                                Double totalEnergy = consumption.getTotalEnergy();
                                Double cuspEnergy = consumption.getCuspTimeEnergy();
                                Double peakEnergy = consumption.getPeakTimeEnergy();
                                Double fairEnergy = consumption.getFairTimeEnergy();
                                Double valleyEnergy = consumption.getValleyTimeEnergy();

                                //计算总电量
                                total = NumberUtils.add(total, totalEnergy);
                                cusp = NumberUtils.add(cusp, cuspEnergy);
                                peak = NumberUtils.add(peak, peakEnergy);
                                fair = NumberUtils.add(fair, fairEnergy);
                                valley = NumberUtils.add(valley, valleyEnergy);

                                //封装对象
                                String key = consumption.getDataDateKey();
                                item.put(key, packageEnergy(qryType, totalEnergy, cuspEnergy, peakEnergy, fairEnergy, valleyEnergy));
                                if (monthAddHead[0]) {
                                    monthHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总"),
                                                    new SheetHead("尖"),
                                                    new SheetHead("峰"),
                                                    new SheetHead("平"),
                                                    new SheetHead("谷"))));
                                }
                            }
                            monthAddHead[0] = false;
                            item.put("总电量（Kwh）", packageEnergy(qryType, total, cusp, peak, fair, valley));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(monthHeads);
                break;
            default:
                emsStatisticsReportEntity.setData(new ArrayList<>());
                emsStatisticsReportEntity.setHeads(new ArrayList<>());
                break;
        }
        return emsStatisticsReportEntity;
    }

    private Object packageRunTime(Double total, Double cusp, Double peak, Double fair, Double valley) {
        JSONObject totalEnergy = new JSONObject(new LinkedHashMap<>());
        totalEnergy.put("总", NumberUtils.div(total, 60, 2));
        totalEnergy.put("尖", NumberUtils.div(cusp, 60, 2));
        totalEnergy.put("峰", NumberUtils.div(peak, 60, 2));
        totalEnergy.put("平", NumberUtils.div(fair, 60, 2));
        totalEnergy.put("谷", NumberUtils.div(valley, 60, 2));
        return totalEnergy;
    }

    private Object packageEnergy(String qryType, Double total, Double cusp, Double peak, Double fair, Double valley) {
        JSONObject totalEnergy = new JSONObject(new LinkedHashMap<>());
        if ("0".equals(qryType)) {
            totalEnergy.put("总", total);
            totalEnergy.put("尖", cusp);
            totalEnergy.put("峰", peak);
            totalEnergy.put("平", fair);
            totalEnergy.put("谷", valley);
        } else if ("1".equals(qryType)) {
            totalEnergy.put("总", total);
            totalEnergy.put("尖", new BigDecimal(NumberUtils.mul(NumberUtils.div(cusp, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%");
            totalEnergy.put("峰", new BigDecimal(NumberUtils.mul(NumberUtils.div(peak, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%");
            totalEnergy.put("平", new BigDecimal(NumberUtils.mul(NumberUtils.div(fair, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%");
            totalEnergy.put("谷", new BigDecimal(NumberUtils.mul(NumberUtils.div(valley, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%");
        } else if ("2".equals(qryType)) {
            totalEnergy.put("总", total);
            totalEnergy.put("尖", cusp + "（" + new BigDecimal(NumberUtils.mul(NumberUtils.div(cusp, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%）");
            totalEnergy.put("峰", peak + "（" + new BigDecimal(NumberUtils.mul(NumberUtils.div(peak, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%）");
            totalEnergy.put("平", fair + "（" + new BigDecimal(NumberUtils.mul(NumberUtils.div(fair, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%）");
            totalEnergy.put("谷", valley + "（" + new BigDecimal(NumberUtils.mul(NumberUtils.div(valley, total), 100)).setScale(2, RoundingMode.UP).doubleValue() + "%）");
        }
        return totalEnergy;
    }

    @Transactional(readOnly = false)
    public String meterTimeShareConsumptionExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        EmsStatisticsReportEntity rltEntity = meterTimeShareConsumption(emsStatisticsReportEntity);

        //转换表头
        List<SheetHead> heads = rltEntity.getHeads();
        List<Map<String, String>> mainHead = new ArrayList<>();
        mainHead.add(MapUtils.EMPTY_MAP);
        ArrayList<String> subHeader = new ArrayList<>();
        subHeader.add("设备名称");

        for (int i = 1; i < heads.size(); i++) {
            SheetHead sheetHead = heads.get(i);
            String lable = sheetHead.getLabel();
            List<SheetHead> children = sheetHead.getChildren();

            HashMap<String, String> keyVal = new HashMap<>();
            keyVal.put("lable", lable);
            keyVal.put("cellSize", CollectionUtils.isEmpty(children) ? 1 + "" : children.size() + "");
            mainHead.add(keyVal);

            if (!CollectionUtils.isEmpty(children)) {
                for (SheetHead child : children) {
                    subHeader.add(child.getLabel());
                }
            }
        }
        String[] excelHeader = subHeader.toArray(new String[0]);

        //转换数据
        List<JSONObject> data = rltEntity.getData();
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            int index = 0;
            for (int i = 0; i < heads.size(); i++) {
                SheetHead sheetHead = heads.get(i);
                String lable = sheetHead.getLabel();
                List<SheetHead> children = sheetHead.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    rowData[index++] = o.get(lable);
                } else {
                    JSONObject jos = o.getJSONObject(lable);
                    if (Objects.nonNull(jos)) {
                        for (SheetHead child : children) {
                            rowData[index++] = jos.get(child.getLabel());
                        }
                    }
                }
            }
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, mainHead, excelHeader, dataList);
    }

    public EmsStatisticsReportEntity meterEfficacy(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        //获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }

        TemporalGranularityEnum temporalGranularity = emsStatisticsReportEntity.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Month:
                //数据列表
                emsStatisticsReportEntity.setCompanyCode(companyCode);
                List<EmsStatisticsEfficacyEntity> vdMonth = emsElectricPowerConsumptionStatisticsService.meterMonthEfficacy(emsStatisticsReportEntity);
                ArrayList<SheetHead> monthHeads = new ArrayList<>();
                monthHeads.add(new SheetHead("设备名称"));
                monthHeads.add(new SheetHead("总产量（吨）"));
                monthHeads.add(new SheetHead("总用电（kwh）"));
                monthHeads.add(new SheetHead("工效"));
                final boolean[] monthAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdMonth.stream().collect(
                                Collectors.groupingBy(EmsStatisticsEfficacyEntity::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsStatisticsEfficacyEntity> efficacyList = obj.getValue();

                            Double tolYield = 0d;
                            Double tolElectr = 0d;

                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("总产量（吨）", tolYield);
                            item.put("总用电（kwh）", tolElectr);
                            item.put("工效", NumberUtils.div(tolYield, tolElectr, 2));

                            for (EmsStatisticsEfficacyEntity efficacy : efficacyList) {
                                Double yield = efficacy.getYield();
                                Double electr = efficacy.getElectricityConsumption();


                                //计算总电量
                                tolYield = NumberUtils.add(tolYield, yield);
                                tolElectr = NumberUtils.add(tolElectr, electr);

                                //封装对象
                                String key = efficacy.getDataDateKey();
                                item.put(key, packageEfficacy(yield, electr));
                                if (monthAddHead[0]) {
                                    monthHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("产量"),
                                                    new SheetHead("用电量"),
                                                    new SheetHead("工效"))));
                                }
                            }
                            monthAddHead[0] = false;
                            item.put("总产量（吨）", tolYield);
                            item.put("总用电（kwh）", tolElectr);
                            item.put("工效", NumberUtils.div(tolYield, tolElectr, 2));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(monthHeads);
                break;
            case VD_Year:
                //数据列表
                emsStatisticsReportEntity.setCompanyCode(companyCode);
                List<EmsStatisticsEfficacyEntity> vdYear = emsElectricPowerConsumptionStatisticsService.meterYearEfficacy(emsStatisticsReportEntity);
                ArrayList<SheetHead> yearHeads = new ArrayList<>();
                yearHeads.add(new SheetHead("设备名称"));
                yearHeads.add(new SheetHead("总产量（吨）"));
                yearHeads.add(new SheetHead("总用电（kwh）"));
                yearHeads.add(new SheetHead("工效"));
                final boolean[] yearAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdYear.stream().collect(
                                Collectors.groupingBy(EmsStatisticsEfficacyEntity::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsStatisticsEfficacyEntity> efficacyList = obj.getValue();

                            Double tolYield = 0d;
                            Double tolElectr = 0d;

                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("总产量（吨）", tolYield);
                            item.put("总用电（kwh）", tolElectr);
                            item.put("工效", NumberUtils.div(tolYield, tolElectr, 2));

                            for (EmsStatisticsEfficacyEntity efficacy : efficacyList) {
                                Double yield = efficacy.getYield();
                                Double electr = efficacy.getElectricityConsumption();


                                //计算总电量
                                tolYield = NumberUtils.add(tolYield, yield);
                                tolElectr = NumberUtils.add(tolElectr, electr);

                                //封装对象
                                String key = efficacy.getDataDateKey();
                                item.put(key, packageEfficacy(yield, electr));
                                if (yearAddHead[0]) {
                                    yearHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("产量"),
                                                    new SheetHead("用电量"),
                                                    new SheetHead("工效"))));
                                }
                            }
                            yearAddHead[0] = false;
                            item.put("总产量（吨）", tolYield);
                            item.put("总用电（kwh）", tolElectr);
                            item.put("工效", NumberUtils.div(tolYield, tolElectr, 2));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(yearHeads);
                break;
            default:
                emsStatisticsReportEntity.setData(new ArrayList<>());
                emsStatisticsReportEntity.setHeads(new ArrayList<>());
                break;
        }
        return emsStatisticsReportEntity;
    }

    private JSONObject packageEfficacy(Double tolYield, Double tolElectr) {
        JSONObject totalEnergy = new JSONObject(new LinkedHashMap<>());
        totalEnergy.put("产量", tolYield);
        totalEnergy.put("用电量", tolElectr);
        totalEnergy.put("工效", NumberUtils.div(tolYield, tolElectr, 2));
        return totalEnergy;
    }

    @Transactional(readOnly = false)
    public String meterEfficacyExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        EmsStatisticsReportEntity rltEntity = meterEfficacy(emsStatisticsReportEntity);

        //转换表头
        List<SheetHead> heads = rltEntity.getHeads();
        List<Map<String, String>> mainHead = new ArrayList<>();
        mainHead.add(MapUtils.EMPTY_MAP);
        mainHead.add(MapUtils.EMPTY_MAP);
        mainHead.add(MapUtils.EMPTY_MAP);
        mainHead.add(MapUtils.EMPTY_MAP);
        ArrayList<String> subHeader = new ArrayList<>();
        subHeader.add("设备名称");
        subHeader.add("总产量（吨）");
        subHeader.add("总用电（kwh）");
        subHeader.add("工效");

        for (int i = 4; i < heads.size(); i++) {
            SheetHead sheetHead = heads.get(i);
            String lable = sheetHead.getLabel();
            List<SheetHead> children = sheetHead.getChildren();

            HashMap<String, String> keyVal = new HashMap<>();
            keyVal.put("lable", lable);
            keyVal.put("cellSize", CollectionUtils.isEmpty(children) ? 1 + "" : children.size() + "");
            mainHead.add(keyVal);

            if (!CollectionUtils.isEmpty(children)) {
                for (SheetHead child : children) {
                    subHeader.add(child.getLabel());
                }
            }
        }
        String[] excelHeader = subHeader.toArray(new String[0]);

        //转换数据
        List<JSONObject> data = rltEntity.getData();
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            int index = 0;
            for (int i = 0; i < heads.size(); i++) {
                SheetHead sheetHead = heads.get(i);
                String lable = sheetHead.getLabel();
                List<SheetHead> children = sheetHead.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    rowData[index++] = o.get(lable);
                } else {
                    JSONObject jos = o.getJSONObject(lable);
                    for (SheetHead child : children) {
                        rowData[index++] = jos.get(child.getLabel());
                    }
                }
            }
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, mainHead, excelHeader, dataList);
    }

    public EmsStatisticsReportEntity meterRuntime(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        //获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }
        TemporalGranularityEnum temporalGranularity = emsStatisticsReportEntity.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Day:
                //查询数据列表
                EmsTimeShareDeviceRuntime dayWhere = new EmsTimeShareDeviceRuntime();
                //数据类型
                dayWhere.setDataType(temporalGranularity.getCode());
                //查询开始时间
                dayWhere.setDataDateStart(DateUtils.parseDate(emsStatisticsReportEntity.getQryStartTime()));
                //查询结束时间
                dayWhere.setDataDateEnd(DateUtils.parseDate(emsStatisticsReportEntity.getQryEndTime()));
                dayWhere.setCompanyCode(companyCode);
                dayWhere.getSqlMap().getOrder().setOrderBy("a.data_date");
                //数据列表
                List<EmsTimeShareDeviceRuntime> vdDay = emsTimeShareDeviceRuntimeService.findMeterSortList(dayWhere);
                ArrayList<SheetHead> dayHeads = new ArrayList<>();
                dayHeads.add(new SheetHead("设备名称"));
                dayHeads.add(new SheetHead("运行时长（小时）",
                        Arrays.asList(
                                new SheetHead("总"),
                                new SheetHead("尖"),
                                new SheetHead("峰"),
                                new SheetHead("平"),
                                new SheetHead("谷"))));
                final boolean[] dayAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdDay.stream().collect(
                                Collectors.groupingBy(EmsTimeShareDeviceRuntime::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsTimeShareDeviceRuntime> runtimeList = obj.getValue();

                            Double total = 0d;
                            Double cusp = 0d;
                            Double peak = 0d;
                            Double fair = 0d;
                            Double valley = 0d;

                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("运行时长（小时）", packageRunTime(total, cusp, peak, fair, valley));

                            for (EmsTimeShareDeviceRuntime runtime : runtimeList) {
                                Double totalTime = runtime.getTotalRunning();
                                Double cuspTime = runtime.getCuspRunningTime();
                                Double peakTime = runtime.getPeakRunningTime();
                                Double fairTime = runtime.getFairRunningTime();
                                Double valleyTime = runtime.getValleyRunningTime();

                                //计算总电量
                                total = NumberUtils.add(total, totalTime);
                                cusp = NumberUtils.add(cusp, cuspTime);
                                peak = NumberUtils.add(peak, peakTime);
                                fair = NumberUtils.add(fair, fairTime);
                                valley = NumberUtils.add(valley, valleyTime);

                                //封装对象
                                String key = DateUtils.formatDate(runtime.getDataDate(), "MM-dd");
                                item.put(key, packageRunTime(totalTime, cuspTime, peakTime, fairTime, valleyTime));
                                if (dayAddHead[0]) {
                                    dayHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总"),
                                                    new SheetHead("尖"),
                                                    new SheetHead("峰"),
                                                    new SheetHead("平"),
                                                    new SheetHead("谷"))));
                                }
                            }
                            dayAddHead[0] = false;
                            item.put("运行时长（小时）", packageRunTime(total, cusp, peak, fair, valley));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(dayHeads);
                break;
            case VD_Month:
                //查询数据列表
                EmsTimeShareDeviceRuntimeStatistics monthWhere = new EmsTimeShareDeviceRuntimeStatistics();
                //数据类型
                monthWhere.setDataType(temporalGranularity.getCode());
                //查询开始时间
                monthWhere.setQryStartTime(emsStatisticsReportEntity.getQryStartTime());
                //查询结束时间
                monthWhere.setQryEndTime(emsStatisticsReportEntity.getQryEndTime());
                monthWhere.setCompanyCode(companyCode);
                //数据列表
                List<EmsTimeShareDeviceRuntimeStatistics> monthDay = emsTimeShareDeviceRuntimeStatisticsService.findMeterSortList(monthWhere);
                ArrayList<SheetHead> monthHeads = new ArrayList<>();
                monthHeads.add(new SheetHead("设备名称"));
                monthHeads.add(new SheetHead("运行时长（小时）",
                        Arrays.asList(
                                new SheetHead("总"),
                                new SheetHead("尖"),
                                new SheetHead("峰"),
                                new SheetHead("平"),
                                new SheetHead("谷"))));
                final boolean[] monthAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        monthDay.stream().collect(
                                Collectors.groupingBy(EmsTimeShareDeviceRuntimeStatistics::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsTimeShareDeviceRuntimeStatistics> runtimeList = obj.getValue();

                            Double total = 0d;
                            Double cusp = 0d;
                            Double peak = 0d;
                            Double fair = 0d;
                            Double valley = 0d;

                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("运行时长（小时）", packageRunTime(total, cusp, peak, fair, valley));

                            for (EmsTimeShareDeviceRuntimeStatistics runtime : runtimeList) {
                                Double totalTime = runtime.getTotalRunning();
                                Double cuspTime = runtime.getCuspRunningTime();
                                Double peakTime = runtime.getPeakRunningTime();
                                Double fairTime = runtime.getFairRunningTime();
                                Double valleyTime = runtime.getValleyRunningTime();

                                //计算总电量
                                total = NumberUtils.add(total, totalTime);
                                cusp = NumberUtils.add(cusp, cuspTime);
                                peak = NumberUtils.add(peak, peakTime);
                                fair = NumberUtils.add(fair, fairTime);
                                valley = NumberUtils.add(valley, valleyTime);

                                //封装对象
                                String key = runtime.getDataDateKey();
                                item.put(key, packageRunTime(totalTime, cuspTime, peakTime, fairTime, valleyTime));
                                if (monthAddHead[0]) {
                                    monthHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总"),
                                                    new SheetHead("尖"),
                                                    new SheetHead("峰"),
                                                    new SheetHead("平"),
                                                    new SheetHead("谷"))));
                                }
                            }
                            monthAddHead[0] = false;
                            item.put("运行时长（小时）", packageRunTime(total, cusp, peak, fair, valley));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(monthHeads);
                break;
            default:
                emsStatisticsReportEntity.setData(new ArrayList<>());
                emsStatisticsReportEntity.setHeads(new ArrayList<>());
                break;
        }
        return emsStatisticsReportEntity;
    }

    @Transactional(readOnly = false)
    public String meterRuntimeExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        EmsStatisticsReportEntity rltEntity = this.meterRuntime(emsStatisticsReportEntity);

        //转换表头
        List<SheetHead> heads = rltEntity.getHeads();
        List<Map<String, String>> mainHead = new ArrayList<>();
        mainHead.add(MapUtils.EMPTY_MAP);
        ArrayList<String> subHeader = new ArrayList<>();
        subHeader.add("设备名称");

        for (int i = 1; i < heads.size(); i++) {
            SheetHead sheetHead = heads.get(i);
            String lable = sheetHead.getLabel();
            List<SheetHead> children = sheetHead.getChildren();

            HashMap<String, String> keyVal = new HashMap<>();
            keyVal.put("lable", lable);
            keyVal.put("cellSize", CollectionUtils.isEmpty(children) ? 1 + "" : children.size() + "");
            mainHead.add(keyVal);

            if (!CollectionUtils.isEmpty(children)) {
                for (SheetHead child : children) {
                    subHeader.add(child.getLabel());
                }
            }
        }
        String[] excelHeader = subHeader.toArray(new String[0]);

        //转换数据
        List<JSONObject> data = rltEntity.getData();
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            int index = 0;
            for (int i = 0; i < heads.size(); i++) {
                SheetHead sheetHead = heads.get(i);
                String lable = sheetHead.getLabel();
                List<SheetHead> children = sheetHead.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    rowData[index++] = o.get(lable);
                } else {
                    JSONObject jos = o.getJSONObject(lable);
                    for (SheetHead child : children) {
                        rowData[index++] = jos.get(child.getLabel());
                    }
                }
            }
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, mainHead, excelHeader, dataList);
    }

    public EmsStatisticsReportEntity officeConsumption(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        //获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }
        TemporalGranularityEnum temporalGranularity = emsStatisticsReportEntity.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Day:
                //查询数据列表
                EmsTimeShareOfficeConsumption dayWhere = new EmsTimeShareOfficeConsumption();
                //数据类型
                dayWhere.setDataType(temporalGranularity.getCode());
                //查询开始时间
                dayWhere.setDataDateStart(DateUtils.parseDate(emsStatisticsReportEntity.getQryStartTime()));
                //查询结束时间
                dayWhere.setDataDateEnd(DateUtils.parseDate(emsStatisticsReportEntity.getQryEndTime()));
                dayWhere.setCompanyCode(companyCode);
                //数据列表
                List<EmsTimeShareOfficeConsumption> vdDay = emsTimeShareOfficeConsumptionService.findOfficeList(dayWhere);
                ArrayList<SheetHead> dayHeads = new ArrayList<>();
                dayHeads.add(new SheetHead("部门名称"));
                dayHeads.add(new SheetHead("总电量（Kwh）",
                        Arrays.asList(
                                new SheetHead("总"),
                                new SheetHead("尖"),
                                new SheetHead("峰"),
                                new SheetHead("平"),
                                new SheetHead("谷"))));
                final boolean[] dayAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdDay.stream().collect(
                                Collectors.groupingBy(EmsTimeShareOfficeConsumption::getOfficeName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String officeName = obj.getKey();
                            List<EmsTimeShareOfficeConsumption> consumptionList = obj.getValue();

                            Double total = 0d;
                            Double cusp = 0d;
                            Double peak = 0d;
                            Double fair = 0d;
                            Double valley = 0d;
                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("部门名称", officeName);
                            item.put("总电量（Kwh）", packageEnergy("0", total, cusp, peak, fair, valley));

                            for (EmsTimeShareOfficeConsumption consumption : consumptionList) {
                                Double totalEnergy = consumption.getTotalEnergy();
                                Double cuspEnergy = consumption.getCuspTimeEnergy();
                                Double peakEnergy = consumption.getPeakTimeEnergy();
                                Double fairEnergy = consumption.getFairTimeEnergy();
                                Double valleyEnergy = consumption.getValleyTimeEnergy();

                                //计算总电量
                                total = NumberUtils.add(total, totalEnergy);
                                cusp = NumberUtils.add(cusp, cuspEnergy);
                                peak = NumberUtils.add(peak, peakEnergy);
                                fair = NumberUtils.add(fair, fairEnergy);
                                valley = NumberUtils.add(valley, valleyEnergy);

                                //封装对象
                                String key = DateUtils.formatDate(consumption.getDataDate(), "MM-dd");
                                item.put(key, packageEnergy("0", totalEnergy, cuspEnergy, peakEnergy, fairEnergy, valleyEnergy));
                                if (dayAddHead[0]) {
                                    dayHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总"),
                                                    new SheetHead("尖"),
                                                    new SheetHead("峰"),
                                                    new SheetHead("平"),
                                                    new SheetHead("谷"))));
                                }
                            }
                            dayAddHead[0] = false;
                            item.put("总电量（Kwh）", packageEnergy("0", total, cusp, peak, fair, valley));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(dayHeads);
                break;
            case VD_Month:
                //查询数据列表
                EmsTimeShareOfficeConsumptionStatistics monthWhere = new EmsTimeShareOfficeConsumptionStatistics();
                //数据类型
                monthWhere.setDataType(temporalGranularity.getCode());
                //查询开始时间
                monthWhere.setQryStartTime(emsStatisticsReportEntity.getQryStartTime());
                //查询结束时间
                monthWhere.setQryEndTime(emsStatisticsReportEntity.getQryEndTime());
                monthWhere.setCompanyCode(companyCode);
                //数据列表
                List<EmsTimeShareOfficeConsumptionStatistics> vdMonth = emsTimeShareOfficeConsumptionStatisticsService.findOfficeList(monthWhere);
                ArrayList<SheetHead> monthHeads = new ArrayList<>();
                monthHeads.add(new SheetHead("部门名称"));
                monthHeads.add(new SheetHead("总电量（Kwh）",
                        Arrays.asList(
                                new SheetHead("总"),
                                new SheetHead("尖"),
                                new SheetHead("峰"),
                                new SheetHead("平"),
                                new SheetHead("谷"))));
                final boolean[] monthAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdMonth.stream().collect(
                                Collectors.groupingBy(EmsTimeShareOfficeConsumptionStatistics::getOfficeName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String officeName = obj.getKey();
                            List<EmsTimeShareOfficeConsumptionStatistics> consumptionList = obj.getValue();

                            Double total = 0d;
                            Double cusp = 0d;
                            Double peak = 0d;
                            Double fair = 0d;
                            Double valley = 0d;
                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("部门名称", officeName);
                            item.put("总电量（Kwh）", packageEnergy("0", total, cusp, peak, fair, valley));

                            for (EmsTimeShareOfficeConsumptionStatistics consumption : consumptionList) {
                                Double totalEnergy = consumption.getTotalEnergy();
                                Double cuspEnergy = consumption.getCuspTimeEnergy();
                                Double peakEnergy = consumption.getPeakTimeEnergy();
                                Double fairEnergy = consumption.getFairTimeEnergy();
                                Double valleyEnergy = consumption.getValleyTimeEnergy();

                                //计算总电量
                                total = NumberUtils.add(total, totalEnergy);
                                cusp = NumberUtils.add(cusp, cuspEnergy);
                                peak = NumberUtils.add(peak, peakEnergy);
                                fair = NumberUtils.add(fair, fairEnergy);
                                valley = NumberUtils.add(valley, valleyEnergy);

                                //封装对象
                                String key = consumption.getDataDateKey();
                                item.put(key, packageEnergy("0", totalEnergy, cuspEnergy, peakEnergy, fairEnergy, valleyEnergy));
                                if (monthAddHead[0]) {
                                    monthHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总"),
                                                    new SheetHead("尖"),
                                                    new SheetHead("峰"),
                                                    new SheetHead("平"),
                                                    new SheetHead("谷"))));
                                }
                            }
                            monthAddHead[0] = false;
                            item.put("总电量（Kwh）", packageEnergy("0", total, cusp, peak, fair, valley));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(monthHeads);
                break;
            default:
                emsStatisticsReportEntity.setData(new ArrayList<>());
                emsStatisticsReportEntity.setHeads(new ArrayList<>());
                break;
        }
        return emsStatisticsReportEntity;
    }
    @Transactional(readOnly = false)
    public String officeConsumptionExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        EmsStatisticsReportEntity rltEntity = officeConsumption(emsStatisticsReportEntity);

        //转换表头
        List<SheetHead> heads = rltEntity.getHeads();
        List<Map<String, String>> mainHead = new ArrayList<>();
        mainHead.add(MapUtils.EMPTY_MAP);
        ArrayList<String> subHeader = new ArrayList<>();
        subHeader.add("部门名称");

        for (int i = 1; i < heads.size(); i++) {
            SheetHead sheetHead = heads.get(i);
            String lable = sheetHead.getLabel();
            List<SheetHead> children = sheetHead.getChildren();

            HashMap<String, String> keyVal = new HashMap<>();
            keyVal.put("lable", lable);
            keyVal.put("cellSize", CollectionUtils.isEmpty(children) ? 1 + "" : children.size() + "");
            mainHead.add(keyVal);

            if (!CollectionUtils.isEmpty(children)) {
                for (SheetHead child : children) {
                    subHeader.add(child.getLabel());
                }
            }
        }
        String[] excelHeader = subHeader.toArray(new String[0]);

        //转换数据
        List<JSONObject> data = rltEntity.getData();
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            int index = 0;
            for (int i = 0; i < heads.size(); i++) {
                SheetHead sheetHead = heads.get(i);
                String lable = sheetHead.getLabel();
                List<SheetHead> children = sheetHead.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    rowData[index++] = o.get(lable);
                } else {
                    JSONObject jos = o.getJSONObject(lable);
                    for (SheetHead child : children) {
                        if (Objects.nonNull(jos)) {
                            rowData[index++] = jos.get(child.getLabel());
                        } else {
                            rowData[index++] = "";
                        }
                    }
                }
            }
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, mainHead, excelHeader, dataList);
    }

    public EmsStatisticsReportEntity meterUseRatio(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        //获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }
        TemporalGranularityEnum temporalGranularity = emsStatisticsReportEntity.getTemporalGranularity();
        switch (temporalGranularity) {
            case VD_Day:
                //查询数据列表
                EmsTimeShareDeviceRuntime dayWhere = new EmsTimeShareDeviceRuntime();
                //数据类型
                dayWhere.setDataType(temporalGranularity.getCode());
                //查询开始时间
                dayWhere.setDataDateStart(DateUtils.parseDate(emsStatisticsReportEntity.getQryStartTime()));
                //查询结束时间
                dayWhere.setDataDateEnd(DateUtils.parseDate(emsStatisticsReportEntity.getQryEndTime()));
                dayWhere.setCompanyCode(companyCode);
                //数据列表
                List<EmsTimeShareDeviceRuntime> vdDay = emsTimeShareDeviceRuntimeService.findMeterSortList(dayWhere);
                ArrayList<SheetHead> dayHeads = new ArrayList<>();
                dayHeads.add(new SheetHead("设备名称"));
                dayHeads.add(new SheetHead("工作时长（小时）",
                        Arrays.asList(
                                new SheetHead("总时长"),
                                new SheetHead("停机"),
                                new SheetHead("空载"),
                                new SheetHead("运行"),
                                new SheetHead("利用率"))));
                final boolean[] dayAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        vdDay.stream().collect(
                                Collectors.groupingBy(EmsTimeShareDeviceRuntime::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsTimeShareDeviceRuntime> runtimeList = obj.getValue();

                            Double total = 0d;
                            Double stop = 0d;
                            Double noload = 0d;
                            Double running = 0d;

                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("工作时长（小时）", packageUseRatio(total, stop, noload, running));

                            for (EmsTimeShareDeviceRuntime runtime : runtimeList) {
                                //计算总电量
                                Double totalTime = NumberUtils.addAll(runtime.getTotalStop(), runtime.getTotalNoLoad(), runtime.getTotalRunning());
                                Double stopTime = NumberUtils.addAll(runtime.getCuspStopTime(), runtime.getPeakStopTime(), runtime.getFairStopTime(), runtime.getValleyStopTime());
                                Double noLoadTime = NumberUtils.addAll(runtime.getCuspNoLoadTime(), runtime.getPeakNoLoadTime(), runtime.getFairNoLoadTime(), runtime.getValleyNoLoadTime());
                                Double runningTime = NumberUtils.addAll(runtime.getCuspRunningTime(), runtime.getPeakRunningTime(), runtime.getFairRunningTime(), runtime.getValleyRunningTime());

                                total = NumberUtils.add(total, totalTime);
                                stop = NumberUtils.add(stop, stopTime);
                                noload = NumberUtils.add(noload, noLoadTime);
                                running = NumberUtils.add(running, runningTime);

                                //封装对象
                                String key = DateUtils.formatDate(runtime.getDataDate(), "MM-dd");
                                item.put(key, packageUseRatio(totalTime, stopTime, noLoadTime, runningTime));
                                if (dayAddHead[0]) {
                                    dayHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总时长"),
                                                    new SheetHead("停机"),
                                                    new SheetHead("空载"),
                                                    new SheetHead("运行"),
                                                    new SheetHead("利用率"))));
                                }
                            }
                            dayAddHead[0] = false;
                            item.put("工作时长（小时）", packageUseRatio(total, stop, noload, running));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(dayHeads);
                break;
            case VD_Month:
                //查询数据列表
                EmsTimeShareDeviceRuntimeStatistics monthWhere = new EmsTimeShareDeviceRuntimeStatistics();
                //数据类型
                monthWhere.setDataType(temporalGranularity.getCode());
                //查询开始时间
                monthWhere.setQryStartTime(emsStatisticsReportEntity.getQryStartTime());
                //查询结束时间
                monthWhere.setQryEndTime(emsStatisticsReportEntity.getQryEndTime());
                monthWhere.setCompanyCode(companyCode);
                monthWhere.getSqlMap().getOrder().setOrderBy("a.data_date_key");
                //数据列表
                List<EmsTimeShareDeviceRuntimeStatistics> monthDay = emsTimeShareDeviceRuntimeStatisticsService.findMeterSortList(monthWhere);
                ArrayList<SheetHead> monthHeads = new ArrayList<>();
                monthHeads.add(new SheetHead("设备名称"));
                monthHeads.add(new SheetHead("工作时长（小时）",
                        Arrays.asList(
                                new SheetHead("总时长"),
                                new SheetHead("停机"),
                                new SheetHead("空载"),
                                new SheetHead("运行"),
                                new SheetHead("利用率"))));
                final boolean[] monthAddHead = {true};

                //转换返回参数
                emsStatisticsReportEntity.setData(
                        monthDay.stream().collect(
                                Collectors.groupingBy(EmsTimeShareDeviceRuntimeStatistics::getDeviceName, LinkedHashMap::new, Collectors.toList())
                        ).entrySet().stream().map(obj -> {
                            String deviceName = obj.getKey();
                            List<EmsTimeShareDeviceRuntimeStatistics> runtimeList = obj.getValue();

                            Double total = 0d;
                            Double stop = 0d;
                            Double noload = 0d;
                            Double running = 0d;

                            JSONObject item = new JSONObject(new LinkedHashMap());
                            item.put("设备名称", deviceName);
                            item.put("工作时长（小时）", packageUseRatio(total, stop, noload, running));

                            for (EmsTimeShareDeviceRuntimeStatistics runtime : runtimeList) {
                                //计算总电量
                                Double totalTime = NumberUtils.addAll(runtime.getTotalStop(), runtime.getTotalNoLoad(), runtime.getTotalRunning());
                                Double stopTime = NumberUtils.addAll(runtime.getCuspStopTime(), runtime.getPeakStopTime(), runtime.getFairStopTime(), runtime.getValleyStopTime());
                                Double noLoadTime = NumberUtils.addAll(runtime.getCuspNoLoadTime(), runtime.getPeakNoLoadTime(), runtime.getFairNoLoadTime(), runtime.getValleyNoLoadTime());
                                Double runningTime = NumberUtils.addAll(runtime.getCuspRunningTime(), runtime.getPeakRunningTime(), runtime.getFairRunningTime(), runtime.getValleyRunningTime());

                                total = NumberUtils.add(total, totalTime);
                                stop = NumberUtils.add(stop, stopTime);
                                noload = NumberUtils.add(noload, noLoadTime);
                                running = NumberUtils.add(running, runningTime);

                                //封装对象
                                String key = runtime.getDataDateKey();
                                item.put(key, packageUseRatio(totalTime, stopTime, noLoadTime, runningTime));
                                if (monthAddHead[0]) {
                                    monthHeads.add(new SheetHead(key,
                                            Arrays.asList(
                                                    new SheetHead("总时长"),
                                                    new SheetHead("停机"),
                                                    new SheetHead("空载"),
                                                    new SheetHead("运行"),
                                                    new SheetHead("利用率"))));
                                }
                            }
                            monthAddHead[0] = false;
                            item.put("工作时长（小时）", packageUseRatio(total, stop, noload, running));
                            return item;
                        }).collect(Collectors.toList())
                );
                emsStatisticsReportEntity.setHeads(monthHeads);
                break;
            default:
                emsStatisticsReportEntity.setData(new ArrayList<>());
                emsStatisticsReportEntity.setHeads(new ArrayList<>());
                break;
        }
        return emsStatisticsReportEntity;
    }

    private Object packageUseRatio(Double total, Double stop, Double noload, Double running) {
        JSONObject totalEnergy = new JSONObject(new LinkedHashMap<>());
        totalEnergy.put("总时长", NumberUtils.div(total, 60, 2));
        totalEnergy.put("停机", NumberUtils.div(stop, 60, 2));
        totalEnergy.put("空载", NumberUtils.div(noload, 60, 2));
        totalEnergy.put("运行", NumberUtils.div(running, 60, 2));
        totalEnergy.put("利用率", NumberUtils.div(running, total, 2));
        return totalEnergy;
    }

    @Transactional(readOnly=false)
    public String meterUseRatioExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        EmsStatisticsReportEntity rltEntity = this.meterUseRatio(emsStatisticsReportEntity);
        //转换表头
        List<SheetHead> heads = rltEntity.getHeads();
        List<Map<String, String>> mainHead = new ArrayList<>();
        mainHead.add(MapUtils.EMPTY_MAP);
        ArrayList<String> subHeader = new ArrayList<>();
        subHeader.add("设备名称");

        for (int i = 1; i < heads.size(); i++) {
            SheetHead sheetHead = heads.get(i);
            String lable = sheetHead.getLabel();
            List<SheetHead> children = sheetHead.getChildren();

            HashMap<String, String> keyVal = new HashMap<>();
            keyVal.put("lable", lable);
            keyVal.put("cellSize", CollectionUtils.isEmpty(children) ? 1 + "" : children.size() + "");
            mainHead.add(keyVal);

            if (!CollectionUtils.isEmpty(children)) {
                for (SheetHead child : children) {
                    subHeader.add(child.getLabel());
                }
            }
        }
        String[] excelHeader = subHeader.toArray(new String[0]);

        //转换数据
        List<JSONObject> data = rltEntity.getData();
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            int index = 0;
            for (int i = 0; i < heads.size(); i++) {
                SheetHead sheetHead = heads.get(i);
                String lable = sheetHead.getLabel();
                List<SheetHead> children = sheetHead.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    rowData[index++] = o.get(lable);
                } else {
                    JSONObject jos = o.getJSONObject(lable);
                    for (SheetHead child : children) {
                        rowData[index++] = jos.get(child.getLabel());
                    }
                }
            }
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, mainHead, excelHeader, dataList);
    }

    /**
     * 电表示数查询
     * @param emsStatisticsReportEntity
     * @return
     */
    public List<MeterPendulumDisplayEntity> meterPendulumDisplay(EmsStatisticsReportEntity emsStatisticsReportEntity) {
        //获取前端传参公司
        String companyCode = emsStatisticsReportEntity.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)){
            Company company = EmsUserHelper.userCompany();
            companyCode = company.getCompanyCode();
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startQueryTime", emsStatisticsReportEntity.getQryStartTime());
        dataMap.put("endQueryTime",  emsStatisticsReportEntity.getQryEndTime());

        EmsMeterCollectedData params = new EmsMeterCollectedData();
        params.setCompanyCode(companyCode);
        params.setDataMap(dataMap);
        return emsMeterCollectedDataService.meterPendulumDisplay(params);
    }
    @Transactional(readOnly=false)
    public String meterPendulumDisplayExp(String fileName, EmsStatisticsReportEntity emsStatisticsReportEntity) throws IOException {
        List<MeterPendulumDisplayEntity> data = meterPendulumDisplay(emsStatisticsReportEntity);

        //组装表头
        List<Map<String, String>> mainHead = new ArrayList<>();
        mainHead.add(MapUtils.EMPTY_MAP);
        HashMap<String, String> keyVal = new HashMap<>();
        keyVal.put("lable", "电表示数");
        keyVal.put("cellSize", "2");
        mainHead.add(keyVal);
        mainHead.add(MapUtils.EMPTY_MAP);
        mainHead.add(MapUtils.EMPTY_MAP);
        mainHead.add(MapUtils.EMPTY_MAP);

        ArrayList<String> subHeader = new ArrayList<>();
        subHeader.add("设备名称");
        subHeader.add(emsStatisticsReportEntity.getQryStartTime());
        subHeader.add(emsStatisticsReportEntity.getQryEndTime());
        subHeader.add("倍率");
        subHeader.add("示数差值");
        subHeader.add("实际用电");

        String[] excelHeader = subHeader.toArray(new String[0]);

        //转换数据
        List<Object[]> dataList = data.stream().map(o -> {
            Object[] rowData = new Object[excelHeader.length];
            rowData[0] = o.getDeviceName();
            rowData[1] = o.getPositiveActiveEnergyDisplayStart();
            rowData[2] = o.getPositiveActiveEnergyDisplayEnd();
            rowData[3] = o.getQt();
            rowData[4] = o.getPositiveActiveEnergyDisplayDiffValue();
            rowData[5] = o.getPositiveActiveEnergyConsumption();
            return rowData;
        }).collect(Collectors.toList());

        //返回下载路径
        return ExcelExportUtil.uploadOss(fileName, mainHead, excelHeader, dataList);
    }
}
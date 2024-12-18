package com.jeesite.modules.job.task.ems.powercons;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.entity.EmsElectricPowerAreaConsumptionStatistics;
import com.jeesite.modules.ems.entity.EmsElectricPowerConsumptionStatistics;
import com.jeesite.modules.ems.service.EmsAreaService;
import com.jeesite.modules.ems.service.EmsElectricPowerAreaConsumptionStatisticsService;
import com.jeesite.modules.ems.service.EmsElectricPowerConsumptionStatisticsService;
import com.jeesite.modules.job.entity.CalculateJobParam;
import com.jeesite.modules.job.task.utils.Calculatehelper;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.service.entity.CompanyConfig;
import com.jeesite.modules.sys.service.impl.CompanyConfigServiceImpl;
import com.jeesite.modules.sys.service.support.adapt.GlobalCalculateAdapter;
import com.jeesite.modules.sys.utils.CorpUtils;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 类说明
 * 区域每年电耗计算定时任务
 *
 * @author 李鹏
 * @date 2022/6/5
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class AreaCalculateYearDataTask {

    @Resource
    private UserService userService;
    @Resource
    private EmsAreaService emsAreaService;
    @Resource
    private EmsElectricPowerConsumptionStatisticsService emsElectricPowerConsumptionStatisticsService;
    @Resource
    private EmsElectricPowerAreaConsumptionStatisticsService emsElectricPowerAreaConsumptionStatisticsService;
    @Resource
    private GlobalCalculateAdapter calculateAdapter;
    @Resource
    private CompanyConfigServiceImpl companyConfigServiceImpl;

    /**
     * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void execute(String jobParam) throws ParseException {
        XxlJobHelper.log("######区域每年电耗计算定时任务【开始】######");
        log.info("######区域每年电耗计算定时任务【开始】######");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User user = new User();
        user.setLoginCode("virtual_gd_job_admin");
        User jobAdmin = userService.getByLoginCode(user);
        CorpUtils.setCurrentCorpCode(jobAdmin.getCorpCode(), jobAdmin.getCorpName());

        CalculateJobParam calculateJobParam = Calculatehelper.parseJobParam(jobParam);
        String execDate = Calculatehelper.getExecDate_Def_OS(calculateJobParam.getAppointedTime());

        XxlJobHelper.log("当前业务时间=》{}", execDate);
        log.info("当前业务时间=》{}", execDate);

        XxlJobHelper.log("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));
        log.info("当前指定公司=》{}（未指定跑全量）", JSONObject.toJSONString(calculateJobParam.getCompanyCodes()));

        GlobalCalculateAdapter.OfFirst_Last ofYearFirstLast = calculateAdapter.getOfYearFirst_Last(DateUtils.parseDate(execDate));
        String busiKey = ofYearFirstLast.getBusiKey();
        XxlJobHelper.log("年份=》{}", busiKey);
        log.info("年份=》{}", busiKey);

        //查询所有区域编码
        Map<String, List<EmsArea>> areaCompanyMap = emsAreaService.findAreaCompanyMap(new EmsArea());
        Map<String, CompanyConfig> companyConfigMap = companyConfigServiceImpl.getAllConfig();

        for (Map.Entry<String, List<EmsArea>> mapEntry : areaCompanyMap.entrySet()) {
            String companyCode = mapEntry.getKey();
            List<EmsArea> emsAreas = mapEntry.getValue();

            if (!Calculatehelper.executeCheck(companyCode, calculateJobParam)) {
                continue;
            }

            CompanyConfig cc = companyConfigMap.get(companyCode);
            if (Objects.isNull(cc)) {
                XxlJobHelper.log("当前处理公司=》{}，配置信息不存在，不进行数据处理", companyCode);
                log.info("当前处理公司=》{}，配置信息不存在，不进行数据处理", companyCode);
                continue;
            }

            //批处理开关
            String jobSwitch = cc.getJobSwitch();
            if (SysYesNoEnum._1.getCode().equals(jobSwitch)) {
                execute(busiKey, companyCode, emsAreas);
            } else {
                XxlJobHelper.log("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
                log.info("当前处理公司=》{}，定时任务开关未打开（{}），不进行数据处理", companyCode, jobSwitch);
            }
        }

        stopWatch.stop();
        XxlJobHelper.log("######区域每年电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
        log.info("######区域每年电耗计算定时任务【结束】######，耗时=》【{}s】", stopWatch.getTotalTimeSeconds());
    }

    private void execute(String busiKey, String companyCode, List<EmsArea> emsAreas) {
        for (EmsArea emsArea : emsAreas) {
            //按区域生成数据
            String areaCode = emsArea.getAreaCode();
            String areaName = emsArea.getAreaName();
            String companyName = emsArea.getCompanyName();
            List<String> areaMarkList = Calculatehelper.getMeterCalculateCodeList(emsArea);

            XxlJobHelper.log("当前公司=》{}，当前区域=》{}（{}），计量电表=》{}", companyCode, areaName, areaCode, areaMarkList);
            log.info("当前公司=》{}，当前区域=》{}（{}），计量电表=》{}", companyCode, areaName, areaCode, areaMarkList);

            //组装统计条件
            EmsElectricPowerConsumptionStatistics where = new EmsElectricPowerConsumptionStatistics();
            where.setAreaCode(areaCode);
            where.setDataDateKey(busiKey);
            where.setDataType(TemporalGranularityEnum.VD_Year.getCode());
            where.setAreaMarkList(areaMarkList);
            EmsElectricPowerAreaConsumptionStatistics areaConsumption = emsElectricPowerConsumptionStatisticsService.getAreaStageCumulativeConsumption(where);
            if (Objects.isNull(areaConsumption)) {
                XxlJobHelper.log("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                log.info("如果当前时刻对应的数据不存在，则表明数据还未生成，跳过当前设备往后执行");
                continue;
            }
            //补充属性
            areaConsumption.setAreaCode(areaCode);
            areaConsumption.setAreaName(areaName);
            areaConsumption.setCompanyCode(companyCode);
            areaConsumption.setCompanyName(companyName);
            //查询存量数据
            EmsElectricPowerAreaConsumptionStatistics stockedRec = emsElectricPowerAreaConsumptionStatisticsService.isStockedRec(areaConsumption);
            if (Objects.nonNull(stockedRec)) {
                areaConsumption.setId(stockedRec.getId());
                areaConsumption.setIsNewRecord(stockedRec.getIsNewRecord());
            }
            emsElectricPowerAreaConsumptionStatisticsService.save(areaConsumption);

            List<EmsArea> childList = emsArea.getChildList();
            if (!CollectionUtils.isEmpty(childList)) {
                execute(busiKey, companyCode, childList);
            }
        }
    }
}

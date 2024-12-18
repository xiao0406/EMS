package com.jeesite.modules.ems.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsElectricityConsumptionSummarDao;
import com.jeesite.modules.ems.api.EmsElectricityConsumptionSummarServiceApi;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * 用电量汇总Service
 *
 * @author 范富华
 * @version 2024-05-13
 */
@Service
@RestController
@Transactional(readOnly = true)
public class EmsElectricityConsumptionSummarService extends CrudService<EmsElectricityConsumptionSummarDao, EmsElectricityConsumptionSummar>
        implements EmsElectricityConsumptionSummarServiceApi {

    /**
     * 获取单条数据
     *
     * @param emsElectricityConsumptionSummar
     * @return
     */
    @Override
    public EmsElectricityConsumptionSummar get(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        return super.get(emsElectricityConsumptionSummar);
    }

    /**
     * 查询分页数据
     *
     * @param emsElectricityConsumptionSummar 查询条件
     * @return
     */
    @Override
    public Page<EmsElectricityConsumptionSummar> findPage(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        return super.findPage(emsElectricityConsumptionSummar);
    }

    /**
     * 查询列表数据
     *
     * @param emsElectricityConsumptionSummar
     * @return
     */
    @Override
    public List<EmsElectricityConsumptionSummar> findList(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        return super.findList(emsElectricityConsumptionSummar);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsElectricityConsumptionSummar
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void save(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        super.save(emsElectricityConsumptionSummar);
    }

    /**
     * 更新状态
     *
     * @param emsElectricityConsumptionSummar
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void updateStatus(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        super.updateStatus(emsElectricityConsumptionSummar);
    }

    /**
     * 删除数据
     *
     * @param emsElectricityConsumptionSummar
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void delete(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        super.delete(emsElectricityConsumptionSummar);
    }

    public EmsElectricityConsumptionSummar findOne(EmsElectricityConsumptionSummar emsElectricityConsumptionSummar) {
        return this.dao.findOne(emsElectricityConsumptionSummar);
    }

    public HomePageEntity dataHandle(HomePageEntity homePageEntity, String type) {
        EmsElectricityConsumptionSummar emsElectricityConsumptionSummar = new EmsElectricityConsumptionSummar();
        String companyCode = homePageEntity.getCompanyCode();
        if (StringUtils.isEmpty(homePageEntity.getCompanyCode())) {
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            companyCode = company.getCompanyCode();
            if (StringUtils.isEmpty(companyCode)) {
                companyCode = "0001A110000000002ZWA";
            }
        }
        emsElectricityConsumptionSummar.setCompanyCode(companyCode);
        emsElectricityConsumptionSummar.setFindType(type);
        EmsElectricityConsumptionSummar summary = this.findOne(emsElectricityConsumptionSummar);
        HomePageEntity<Object, Object, Object> homePage = new HomePageEntity<>();
        EChart<Object, Object> objectObjectEChart = new EChart<>();
        EChartBody eChartBody = new EChartBody();
        if (summary == null) {
            return homePage;
        }
        //组装数据
        String[] split = summary.getXaxis().split(",");
        List<EChartItem> arrayY = new ArrayList<>();
        String[] ys = summary.getYaxis().split("Y");
        for (int i = 0; i < ys.length; i++) {
            List<String> strings = Arrays.asList(ys[i].split(","));
            EChartItem eChartItem = new EChartItem();
            String[] lables = summary.getLable().split(",");
            eChartItem.setLable(lables[i]);
            eChartItem.setValue(strings);
            arrayY.add(eChartItem);
        }
        eChartBody.setX(Arrays.asList(split));
        eChartBody.setY(arrayY);

        objectObjectEChart.setBody(eChartBody);
        objectObjectEChart.setTemporalGranularity(TemporalGranularityEnum.VD_Hour);
        homePage.setCompanyCode(companyCode);
        homePage.setEChart(objectObjectEChart);
        String findType = summary.getFindType();
        if ("1".equals(findType)) {
            TodayCumulativeEntity todayCumulative = new TodayCumulativeEntity();
            todayCumulative.setTodayCumulative(summary.getCumulative());
            todayCumulative.setModelTopic(summary.getModeltopic());
            todayCumulative.setLastWeekQoQ(summary.getLastqoq());
            todayCumulative.setYesterdayQoQ(summary.getYesterdayqoq());
            homePage.setData(todayCumulative);
        } else if ("2".equals(findType)) {
            MonthCumulativeEntity monthCumulative = new MonthCumulativeEntity();
            monthCumulative.setLastMonthQoQ(summary.getLastqoq());
            monthCumulative.setModelTopic(summary.getModeltopic());
            monthCumulative.setMonthCumulative(summary.getCumulative());
            homePage.setData(monthCumulative);
        } else if ("3".equals(findType)) {
            YearCumulativeEntity yearCumulative = new YearCumulativeEntity();
            yearCumulative.setLastYearQoQ(summary.getLastqoq());
            yearCumulative.setYearCumulative(summary.getCumulative());
            yearCumulative.setModelTopic(summary.getModeltopic());
            homePage.setData(yearCumulative);
        }
        return homePage;
    }
}
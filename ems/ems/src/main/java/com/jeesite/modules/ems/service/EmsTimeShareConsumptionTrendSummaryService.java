package com.jeesite.modules.ems.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsTimeShareConsumptionTrendSummaryDao;
import com.jeesite.modules.ems.api.EmsTimeShareConsumptionTrendSummaryServiceApi;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * 尖峰平谷用电趋势汇总Service
 *
 * @author 范富华
 * @version 2024-05-17
 */
@Service
@RestController
@Transactional(readOnly = true)
public class EmsTimeShareConsumptionTrendSummaryService extends CrudService<EmsTimeShareConsumptionTrendSummaryDao, EmsTimeShareConsumptionTrendSummary>
        implements EmsTimeShareConsumptionTrendSummaryServiceApi {

    /**
     * 获取单条数据
     *
     * @param emsTimeShareConsumptionTrendSummary
     * @return
     */
    @Override
    public EmsTimeShareConsumptionTrendSummary get(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
        return super.get(emsTimeShareConsumptionTrendSummary);
    }

    /**
     * 查询分页数据
     *
     * @param emsTimeShareConsumptionTrendSummary 查询条件
     * @return
     */
    @Override
    public Page<EmsTimeShareConsumptionTrendSummary> findPage(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
        return super.findPage(emsTimeShareConsumptionTrendSummary);
    }

    /**
     * 查询列表数据
     *
     * @param emsTimeShareConsumptionTrendSummary
     * @return
     */
    @Override
    public List<EmsTimeShareConsumptionTrendSummary> findList(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
        return super.findList(emsTimeShareConsumptionTrendSummary);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsTimeShareConsumptionTrendSummary
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void save(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
        super.save(emsTimeShareConsumptionTrendSummary);
    }

    /**
     * 更新状态
     *
     * @param emsTimeShareConsumptionTrendSummary
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void updateStatus(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
        super.updateStatus(emsTimeShareConsumptionTrendSummary);
    }

    /**
     * 删除数据
     *
     * @param emsTimeShareConsumptionTrendSummary
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void delete(EmsTimeShareConsumptionTrendSummary emsTimeShareConsumptionTrendSummary) {
        super.delete(emsTimeShareConsumptionTrendSummary);
    }

    public HomePageEntity dataHandle(HomePageEntity homePageEntity) {
        EmsTimeShareConsumptionTrendSummary where = new EmsTimeShareConsumptionTrendSummary();
        String companyCode = homePageEntity.getCompanyCode();
        if (StringUtils.isEmpty(homePageEntity.getCompanyCode())) {
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            companyCode = company.getCompanyCode();
            if (StringUtils.isEmpty(companyCode)){
                companyCode = "0001A110000000002ZWA";
            }
        }
        where.setCompanyCode(companyCode);
        List<EmsTimeShareConsumptionTrendSummary> list = this.findList(where);
        HomePageEntity<Object, Object, Object> homePage = new HomePageEntity<>();
        if (!CollectionUtils.isEmpty(list)){
            EmsTimeShareConsumptionTrendSummary summary = list.get(0);
            EChart<Object, Object> objectObjectEChart = new EChart<>();
            EChartBody eChartBody = new EChartBody();
            if (summary != null) {
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
            }
            objectObjectEChart.setBody(eChartBody);
            homePage.setCompanyCode(summary.getCompanyCode());
            homePage.setEChart(objectObjectEChart);
        }
        return homePage;
    }

}
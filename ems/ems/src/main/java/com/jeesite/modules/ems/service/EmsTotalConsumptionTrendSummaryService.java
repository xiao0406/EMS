package com.jeesite.modules.ems.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsTotalConsumptionTrendSummaryDao;
import com.jeesite.modules.ems.api.EmsTotalConsumptionTrendSummaryServiceApi;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * 总用电量汇总Service
 *
 * @author 范富华
 * @version 2024-05-13
 */
@Service
@RestController
@Transactional(readOnly = true)
public class EmsTotalConsumptionTrendSummaryService extends CrudService<EmsTotalConsumptionTrendSummaryDao, EmsTotalConsumptionTrendSummary>
        implements EmsTotalConsumptionTrendSummaryServiceApi {

    /**
     * 获取单条数据
     *
     * @param emsTotalConsumptionTrendSummary
     * @return
     */
    @Override
    public EmsTotalConsumptionTrendSummary get(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        return super.get(emsTotalConsumptionTrendSummary);
    }

    /**
     * 查询分页数据
     *
     * @param emsTotalConsumptionTrendSummary 查询条件
     * @return
     */
    @Override
    public Page<EmsTotalConsumptionTrendSummary> findPage(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        return super.findPage(emsTotalConsumptionTrendSummary);
    }

    /**
     * 查询列表数据
     *
     * @param emsTotalConsumptionTrendSummary
     * @return
     */
    @Override
    public List<EmsTotalConsumptionTrendSummary> findList(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        return super.findList(emsTotalConsumptionTrendSummary);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsTotalConsumptionTrendSummary
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void save(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        super.save(emsTotalConsumptionTrendSummary);
    }

    /**
     * 更新状态
     *
     * @param emsTotalConsumptionTrendSummary
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void updateStatus(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        super.updateStatus(emsTotalConsumptionTrendSummary);
    }

    /**
     * 删除数据
     *
     * @param emsTotalConsumptionTrendSummary
     */
    @Override
    @GlobalTransactional
    @Transactional(readOnly = false)
    public void delete(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        super.delete(emsTotalConsumptionTrendSummary);
    }

    public EmsTotalConsumptionTrendSummary findOne(EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary) {
        return this.dao.findOne(emsTotalConsumptionTrendSummary);
    }

    public HomePageEntity dataHandle(HomePageEntity homePageEntity) {
        EmsTotalConsumptionTrendSummary emsTotalConsumptionTrendSummary = new EmsTotalConsumptionTrendSummary();
        String companyCode = homePageEntity.getCompanyCode();
        if (StringUtils.isEmpty(homePageEntity.getCompanyCode())){
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            companyCode = company.getCompanyCode();
            if (StringUtils.isEmpty(companyCode)){
                companyCode = "0001A110000000002ZWA";
            }
        }
        emsTotalConsumptionTrendSummary.setCompanyCode(companyCode);
        emsTotalConsumptionTrendSummary.setTemporalgranularity(homePageEntity.getEChart().getTemporalGranularity().getCode());
        EmsTotalConsumptionTrendSummary summary = this.findOne(emsTotalConsumptionTrendSummary);
        EChartBody eChartBody = new EChartBody();
        if (summary != null){
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
        EChart<Object, Object> objectObjectEChart = new EChart<>();
        objectObjectEChart.setBody(eChartBody);
        objectObjectEChart.setTemporalGranularity(homePageEntity.getEChart().getTemporalGranularity());
        HomePageEntity<Object, Object, Object> objectObjectObjectHomePageEntity = new HomePageEntity<>();
        objectObjectObjectHomePageEntity.setCompanyCode(companyCode);
        objectObjectObjectHomePageEntity.setEChart(objectObjectEChart);
        return objectObjectObjectHomePageEntity;
    }

}
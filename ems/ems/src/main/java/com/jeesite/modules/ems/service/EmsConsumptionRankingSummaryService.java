package com.jeesite.modules.ems.service;

import java.util.ArrayList;
import java.util.List;

import com.jeesite.common.constant.enums.TemporalGranularityEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsConsumptionRankingSummaryDao;
import com.jeesite.modules.ems.api.EmsConsumptionRankingSummaryServiceApi;

import io.seata.spring.annotation.GlobalTransactional;

import javax.annotation.Resource;

/**
 * 用电量排行Service
 * @author 范富华
 * @version 2024-05-14
 */
@Service
@RestController
@Transactional(readOnly=true)
public class EmsConsumptionRankingSummaryService extends CrudService<EmsConsumptionRankingSummaryDao, EmsConsumptionRankingSummary>
		implements EmsConsumptionRankingSummaryServiceApi {

	@Resource
	private EmsRankingSummaryInfoService emsRankingSummaryInfoService;

	/**
	 * 获取单条数据
	 * @param emsConsumptionRankingSummary
	 * @return
	 */
	@Override
	public EmsConsumptionRankingSummary get(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		return super.get(emsConsumptionRankingSummary);
	}
	
	/**
	 * 查询分页数据
	 * @param emsConsumptionRankingSummary 查询条件
	 * @return
	 */
	@Override
	public Page<EmsConsumptionRankingSummary> findPage(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		return super.findPage(emsConsumptionRankingSummary);
	}
	
	/**
	 * 查询列表数据
	 * @param emsConsumptionRankingSummary
	 * @return
	 */
	@Override
	public List<EmsConsumptionRankingSummary> findList(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		return super.findList(emsConsumptionRankingSummary);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsConsumptionRankingSummary
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void save(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		super.save(emsConsumptionRankingSummary);
	}
	
	/**
	 * 更新状态
	 * @param emsConsumptionRankingSummary
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void updateStatus(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		super.updateStatus(emsConsumptionRankingSummary);
	}
	
	/**
	 * 删除数据
	 * @param emsConsumptionRankingSummary
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void delete(EmsConsumptionRankingSummary emsConsumptionRankingSummary) {
		super.delete(emsConsumptionRankingSummary);
	}

	public EmsConsumptionRankingSummary findOne(EmsConsumptionRankingSummary emsConsumptionRankingSummary){
		return this.dao.findOne(emsConsumptionRankingSummary);
	}

	public HomePageEntity dataHandle(HomePageEntity homePageEntity) {
		EmsConsumptionRankingSummary rankingSummaryWhere = new EmsConsumptionRankingSummary();
		String companyCode = homePageEntity.getCompanyCode();
		if (StringUtils.isEmpty(companyCode)){
			Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
			companyCode = company.getCompanyCode();
			if (StringUtils.isEmpty(companyCode)){
				companyCode = "0001A110000000002ZWA";
			}
		}
		rankingSummaryWhere.setCompanyCode(companyCode);
		rankingSummaryWhere.setTemporalgranularity(homePageEntity.getEChart().getTemporalGranularity().getCode());
		EmsConsumptionRankingSummary rankingSummary = this.findOne(rankingSummaryWhere);
		if (rankingSummary == null){
			return homePageEntity;
		}
		EmsRankingSummaryInfo summaryInfo = new EmsRankingSummaryInfo();
		summaryInfo.setRankingSummaryId(rankingSummary.getId());
		List<EmsRankingSummaryInfo> summaryInfos = emsRankingSummaryInfoService.findList(summaryInfo);
		//组装数据
		List<EChartData> eChartData = new ArrayList<>();
		if (!CollectionUtils.isEmpty(summaryInfos)){
			for (EmsRankingSummaryInfo info : summaryInfos) {
				EChartData data = new EChartData();
				data.setLabel(info.getLabel());
				data.setValue(info.getValue());
				eChartData.add(data);
			}
		}
		EChart<Object, Object> eChart = new EChart<>();
		eChart.setBody(eChartData);
		String temporalgranularity = rankingSummary.getTemporalgranularity();
		if ("VD_Day".equals(temporalgranularity)){
			eChart.setTemporalGranularity(TemporalGranularityEnum.VD_Day);
		} else if ("VD_Month".equals(temporalgranularity)){
			eChart.setTemporalGranularity(TemporalGranularityEnum.VD_Month);
		} else if ("VD_Year".equals(temporalgranularity)){
			eChart.setTemporalGranularity(TemporalGranularityEnum.VD_Year);
		}
		homePageEntity.setEChart(eChart);
		return homePageEntity;
	}

}
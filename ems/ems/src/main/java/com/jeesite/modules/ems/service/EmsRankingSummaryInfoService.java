package com.jeesite.modules.ems.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsRankingSummaryInfo;
import com.jeesite.modules.ems.dao.EmsRankingSummaryInfoDao;
import com.jeesite.modules.ems.api.EmsRankingSummaryInfoServiceApi;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * 耗电量排行关联表Service
 * @author 范富华
 * @version 2024-05-15
 */
@Service
@RestController
@Transactional(readOnly=true)
public class EmsRankingSummaryInfoService extends CrudService<EmsRankingSummaryInfoDao, EmsRankingSummaryInfo>
		implements EmsRankingSummaryInfoServiceApi {
	
	/**
	 * 获取单条数据
	 * @param emsRankingSummaryInfo
	 * @return
	 */
	@Override
	public EmsRankingSummaryInfo get(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		return super.get(emsRankingSummaryInfo);
	}
	
	/**
	 * 查询分页数据
	 * @param emsRankingSummaryInfo 查询条件
	 * @return
	 */
	@Override
	public Page<EmsRankingSummaryInfo> findPage(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		return super.findPage(emsRankingSummaryInfo);
	}
	
	/**
	 * 查询列表数据
	 * @param emsRankingSummaryInfo
	 * @return
	 */
	@Override
	public List<EmsRankingSummaryInfo> findList(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		return super.findList(emsRankingSummaryInfo);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsRankingSummaryInfo
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void save(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		super.save(emsRankingSummaryInfo);
	}
	
	/**
	 * 更新状态
	 * @param emsRankingSummaryInfo
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void updateStatus(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		super.updateStatus(emsRankingSummaryInfo);
	}
	
	/**
	 * 删除数据
	 * @param emsRankingSummaryInfo
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void delete(EmsRankingSummaryInfo emsRankingSummaryInfo) {
		super.delete(emsRankingSummaryInfo);
	}

	@Transactional
	public void deleteBySummaryId(EmsRankingSummaryInfo emsRankingSummaryInfo){
		this.dao.deleteBySummaryId(emsRankingSummaryInfo);
	}

	@Transactional
	public void insertBatch(List<EmsRankingSummaryInfo> emsRankingSummaryInfoList){
		this.dao.insertBatch(emsRankingSummaryInfoList);
	}
}
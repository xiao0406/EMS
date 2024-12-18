package com.jeesite.modules.ems.service;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import com.jeesite.modules.ems.entity.TimeSharePowerConsumptionEntity;
import com.jeesite.modules.ems.entity.TimeSharePowerQueryEntity;
import com.jeesite.modules.ems.entity.enums.EnergyGroupEnum;
import com.jeesite.modules.ems.vo.TimeSharePowerConsumptionVo;
import com.jeesite.modules.ems.vo.TimeSharePowerQueryVo;
import com.jeesite.modules.sys.utils.CorpUtils;
import io.minio.GetObjectResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsEquipmentOnlineSummary;
import com.jeesite.modules.ems.dao.EmsEquipmentOnlineSummaryDao;
import com.jeesite.modules.ems.api.EmsEquipmentOnlineSummaryServiceApi;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在线设备统计Service
 * @author 范富华
 * @version 2024-05-29
 */
@Service
@RestController
@Transactional(readOnly=true)
public class EmsEquipmentOnlineSummaryService extends CrudService<EmsEquipmentOnlineSummaryDao, EmsEquipmentOnlineSummary>
		implements EmsEquipmentOnlineSummaryServiceApi {

	@Resource
	private EmsTimeSharePowerConsumptionService emsTimeSharePowerConsumptionService;

	/**
	 * 获取单条数据
	 * @param emsEquipmentOnlineSummary
	 * @return
	 */
	@Override
	public EmsEquipmentOnlineSummary get(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		return super.get(emsEquipmentOnlineSummary);
	}
	
	/**
	 * 查询分页数据
	 * @param emsEquipmentOnlineSummary 查询条件
	 * @return
	 */
	@Override
	public Page<EmsEquipmentOnlineSummary> findPage(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		return super.findPage(emsEquipmentOnlineSummary);
	}
	
	/**
	 * 查询列表数据
	 * @param emsEquipmentOnlineSummary
	 * @return
	 */
	@Override
	public List<EmsEquipmentOnlineSummary> findList(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		return super.findList(emsEquipmentOnlineSummary);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsEquipmentOnlineSummary
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void save(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		super.save(emsEquipmentOnlineSummary);
	}
	
	/**
	 * 更新状态
	 * @param emsEquipmentOnlineSummary
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void updateStatus(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		super.updateStatus(emsEquipmentOnlineSummary);
	}
	
	/**
	 * 删除数据
	 * @param emsEquipmentOnlineSummary
	 */
	@Override
	@GlobalTransactional
	@Transactional(readOnly=false)
	public void delete(EmsEquipmentOnlineSummary emsEquipmentOnlineSummary) {
		super.delete(emsEquipmentOnlineSummary);
	}

	@Transactional(readOnly=false)
	public List<TimeSharePowerConsumptionVo> findTimeShare(TimeSharePowerQueryVo timeSharePowerQueryVo){
		CorpUtils.setCurrentCorpCode("SXJT", "山西建投");
		HttpServletRequest request1=((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		TimeSharePowerQueryEntity timeSharePowerQueryEntity = new TimeSharePowerQueryEntity();
		BeanUtil.copyProperties(timeSharePowerQueryVo,timeSharePowerQueryEntity);
		//默认按设备查询
		timeSharePowerQueryEntity.setEnergyGroup(EnergyGroupEnum.GROUP_Device);
		if ("GROUP_Area".equals(timeSharePowerQueryVo.getEnergyGroup())){
			timeSharePowerQueryEntity.setEnergyGroup(EnergyGroupEnum.GROUP_Area);
		}
		Page<TimeSharePowerConsumptionEntity> page = emsTimeSharePowerConsumptionService.findTimeSharePage(timeSharePowerQueryEntity,request1,null);
		List<TimeSharePowerConsumptionEntity> TimeSharePowerConsumptions = page.getList();
		List<TimeSharePowerConsumptionVo> timeSharePowerConsumptionVos = new ArrayList<>();
		for (TimeSharePowerConsumptionEntity timeSharePowerConsumption : TimeSharePowerConsumptions) {
			TimeSharePowerConsumptionVo consumptionVo = new TimeSharePowerConsumptionVo();
			consumptionVo.setDateTime(timeSharePowerConsumption.getDateTime());
			consumptionVo.setTypeInfo(timeSharePowerConsumption.getTypeInfo());
			consumptionVo.setTotalEnergy(timeSharePowerConsumption.getTotalEnergy());
			timeSharePowerConsumptionVos.add(consumptionVo);
		}
		return timeSharePowerConsumptionVos;
	}

}
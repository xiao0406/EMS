package com.jeesite.modules.ems.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.entity.DataScope;
import com.jeesite.common.expr.BusinessException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.dao.EmsAreaDao;
import org.springframework.util.CollectionUtils;

/**
 * 区域表Service
 * @author 李鹏
 * @version 2023-06-12
 */
@Service
@Transactional(readOnly=true)
public class EmsAreaService extends TreeService<EmsAreaDao, EmsArea> {
	
	/**
	 * 获取单条数据
	 * @param emsArea
	 * @return
	 */
	@Override
	public EmsArea get(EmsArea emsArea) {
		return super.get(emsArea);
	}

	private void addCompanyFilter(EmsArea entity){
		if (StringUtils.isBlank(entity.getCompanyCode())){
			//Company company = EmsUserHelper.userCompany();
			User user = UserUtils.getUser();
			Employee employee = EmpUtils.get(user);
			Company company = employee.getCompany();
			entity.setCompanyCode(company.getCompanyCode());
		}
	}
	
	/**
	 * 查询分页数据
	 * @param emsArea 查询条件
	 * @return
	 */
	@Override
	public Page<EmsArea> findPage(EmsArea emsArea) {
		return super.findPage(emsArea);
	}
	
	/**
	 * 查询列表数据
	 * @param emsArea
	 * @return
	 */
	@Override
	public List<EmsArea> findList(EmsArea emsArea) {
		addCompanyFilter(emsArea);
		return super.findList(emsArea);
	}
	/**
	 * 保存数据（插入或更新）
	 * @param emsArea
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsArea emsArea) {
		EmsArea stock = this.getById(emsArea);
		if(emsArea.getIsNewRecord() && Objects.nonNull(stock)){
			if(EmsArea.STATUS_NORMAL.equals(stock.getStatus())){
				throw new BusinessException(StringUtils.messageFormat("区域编码【{0}】已存在", emsArea.getAreaCode()));
			}else{
				emsArea.setIsNewRecord(false);
				emsArea.setStatus(EmsArea.STATUS_NORMAL);
				this.updateStatus(emsArea);
			}
		}
		Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
		emsArea.setCompanyCode(company.getCompanyCode());
		emsArea.setCompanyName(company.getCompanyName());
		super.save(emsArea);
	}

	private EmsArea getById(EmsArea emsArea) {
		return this.dao.getById(emsArea);
	}

	/**
	 * 更新状态
	 * @param emsArea
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsArea emsArea) {
		super.updateStatus(emsArea);
	}
	
	/**
	 * 删除数据
	 * @param emsArea
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsArea emsArea) {
		super.delete(emsArea);
	}

	public Map<String, List<EmsArea>> findAreaCompanyMap(EmsArea emsArea) {
		List<EmsArea> areaList = this.findAllMarked(emsArea);
		Map<String, List<EmsArea>> companyCollect = areaList.stream().collect(Collectors.groupingBy(EmsArea::getCompanyCode));
		for (Map.Entry<String, List<EmsArea>> mapEntry : companyCollect.entrySet()) {
			String companyCode = mapEntry.getKey();
			List<EmsArea> emsAreas = mapEntry.getValue();
			//按照父级code -- list  分组
			Map<String, List<EmsArea>> parrentMap = emsAreas.stream().collect(Collectors.groupingBy(EmsArea::getParentCode));
			//从最顶层开始遍历
			String parentCode = DefaultConstant.PID;
			List<EmsArea> areas = parrentMap.get(parentCode);
			//区域套娃
			matryoshkaDoll(parrentMap, areas);
			//重新赋值
			companyCollect.put(companyCode, areas);
		}
		return companyCollect;
	}

	private void matryoshkaDoll(Map<String, List<EmsArea>> parrentMap, List<EmsArea> areas){
		if(!CollectionUtils.isEmpty(areas)){
			for (EmsArea area : areas) {
				String areaCode = area.getAreaCode();
				List<EmsArea> childList = parrentMap.get(areaCode);
				if(!CollectionUtils.isEmpty(childList)){
					matryoshkaDoll(parrentMap, childList);
					area.setChildList(childList);
				}
			}
		}
	}

	private List<EmsArea> findAllMarked(EmsArea emsArea) {
		return this.dao.findAllMarked(emsArea);
	}

}
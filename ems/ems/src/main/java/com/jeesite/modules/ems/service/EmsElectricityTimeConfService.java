package com.jeesite.modules.ems.service;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.CompanyVo;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.utils.UserHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsElectricityTimeConf;
import com.jeesite.modules.ems.dao.EmsElectricityTimeConfDao;

/**
 * 用电分时配置表Service
 * @author 李鹏
 * @version 2023-06-15
 */
@Service
@Transactional(readOnly=true)
public class EmsElectricityTimeConfService extends CrudService<EmsElectricityTimeConfDao, EmsElectricityTimeConf> {
	
	/**
	 * 获取单条数据
	 * @param emsElectricityTimeConf
	 * @return
	 */
	@Override
	public EmsElectricityTimeConf get(EmsElectricityTimeConf emsElectricityTimeConf) {
		return super.get(emsElectricityTimeConf);
	}
	
	/**
	 * 查询分页数据
	 * @param emsElectricityTimeConf 查询条件
	 * @return
	 */
	@Override
	public Page<EmsElectricityTimeConf> findPage(EmsElectricityTimeConf emsElectricityTimeConf) {
		return super.findPage(emsElectricityTimeConf);
	}

	private void addCompanyFilter(EmsElectricityTimeConf entity){
		if (StringUtils.isBlank(entity.getCompanyCode())){
			Company company = EmsUserHelper.userCompany();
			entity.setCompanyCode(company.getCompanyCode());
		}
	}

	/**
	 * 查询列表数据
	 * @param emsElectricityTimeConf
	 * @return
	 */
	@Override
	public List<EmsElectricityTimeConf> findList(EmsElectricityTimeConf emsElectricityTimeConf) {
		addCompanyFilter(emsElectricityTimeConf);
		return super.findList(emsElectricityTimeConf);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsElectricityTimeConf
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsElectricityTimeConf emsElectricityTimeConf) {
		super.save(emsElectricityTimeConf);
	}
	
	/**
	 * 更新状态
	 * @param emsElectricityTimeConf
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsElectricityTimeConf emsElectricityTimeConf) {
		super.updateStatus(emsElectricityTimeConf);
	}
	
	/**
	 * 删除数据
	 * @param emsElectricityTimeConf
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsElectricityTimeConf emsElectricityTimeConf) {
		super.delete(emsElectricityTimeConf);
	}

	public EmsElectricityTimeConf getData(CompanyVo vo) {
		EmsElectricityTimeConf where = new EmsElectricityTimeConf();
		where.setCompanyCode(vo.getCompanyCode());
		addCompanyFilter(where);
		return this.dao.getCompanyElectricityTimeConf(where);
	}

	public EmsElectricityTimeConf getByCompanyCode(String officeCode) {
		EmsElectricityTimeConf where = new EmsElectricityTimeConf();
		where.setCompanyCode(officeCode);
		return this.dao.getByCompanyCode(where);
	}

}
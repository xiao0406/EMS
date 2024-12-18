package com.jeesite.modules.ems.service;

import java.util.List;

import com.jeesite.common.entity.DataScope;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.entity.EmsTerminal;
import com.jeesite.modules.sys.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsMeterYield;
import com.jeesite.modules.ems.dao.EmsMeterYieldDao;

/**
 * 电表产量表Service
 * @author 李鹏
 * @version 2023-06-15
 */
@Service
@Transactional(readOnly=true)
public class EmsMeterYieldService extends CrudService<EmsMeterYieldDao, EmsMeterYield> {
	
	/**
	 * 获取单条数据
	 * @param emsMeterYield
	 * @return
	 */
	@Override
	public EmsMeterYield get(EmsMeterYield emsMeterYield) {
		return super.get(emsMeterYield);
	}
	
	/**
	 * 查询分页数据
	 * @param emsMeterYield 查询条件
	 * @return
	 */
	@Override
	public Page<EmsMeterYield> findPage(EmsMeterYield emsMeterYield) {
		return super.findPage(emsMeterYield);
	}

	private void addCompanyFilter(EmsMeterYield entity) {
		if (StringUtils.isBlank(entity.getCompanyCode())){
			Company company = EmsUserHelper.userCompany();
			entity.setCompanyCode(company.getCompanyCode());
		}
	}

	/**
	 * 查询列表数据
	 * @param emsMeterYield
	 * @return
	 */
	@Override
	public List<EmsMeterYield> findList(EmsMeterYield emsMeterYield) {
		addCompanyFilter(emsMeterYield);
		return super.findList(emsMeterYield);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsMeterYield
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsMeterYield emsMeterYield) {
		Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
		emsMeterYield.setCompanyCode(company.getCompanyCode());
		emsMeterYield.setCompanyName(company.getCompanyName());
		super.save(emsMeterYield);
	}
	
	/**
	 * 更新状态
	 * @param emsMeterYield
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsMeterYield emsMeterYield) {
		super.updateStatus(emsMeterYield);
	}
	
	/**
	 * 删除数据
	 * @param emsMeterYield
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsMeterYield emsMeterYield) {
		super.delete(emsMeterYield);
	}
	
}
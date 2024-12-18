package com.jeesite.modules.ems.service;

import java.util.List;
import java.util.Objects;

import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.enums.ThresholdConfigEnum;
import com.jeesite.modules.sys.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.entity.EmsMeterThresholdConfig;
import com.jeesite.modules.ems.dao.EmsMeterThresholdConfigDao;

/**
 * 电表阈值配置表Service
 * @author 李鹏
 * @version 2023-07-22
 */
@Service
@Transactional(readOnly=true)
public class EmsMeterThresholdConfigService extends CrudService<EmsMeterThresholdConfigDao, EmsMeterThresholdConfig> {
	
	/**
	 * 获取单条数据
	 * @param emsMeterThresholdConfig
	 * @return
	 */
	@Override
	public EmsMeterThresholdConfig get(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		return super.get(emsMeterThresholdConfig);
	}

	private void addCompanyFilter(EmsMeterThresholdConfig entity) {
		if (StringUtils.isBlank(entity.getCompanyCode())){
			Company company = EmsUserHelper.userCompany();
			entity.setCompanyCode(company.getCompanyCode());
		}
	}
	
	/**
	 * 查询分页数据
	 * @param emsMeterThresholdConfig 查询条件
	 * @return
	 */
	@Override
	public Page<EmsMeterThresholdConfig> findPage(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		return super.findPage(emsMeterThresholdConfig);
	}
	
	/**
	 * 查询列表数据
	 * @param emsMeterThresholdConfig
	 * @return
	 */
	@Override
	public List<EmsMeterThresholdConfig> findList(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		addCompanyFilter(emsMeterThresholdConfig);
		return super.findList(emsMeterThresholdConfig);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param emsMeterThresholdConfig
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		EmsMeterThresholdConfig stock = this.getById(emsMeterThresholdConfig);
		if(Objects.isNull(stock)){
			emsMeterThresholdConfig.setIsNewRecord(true);
		}
		Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
		emsMeterThresholdConfig.setCompanyCode(company.getCompanyCode());
		emsMeterThresholdConfig.setCompanyName(company.getCompanyName());
		super.save(emsMeterThresholdConfig);
	}

	/**
	 * 根据ID查询
	 * @param emsMeterThresholdConfig
	 * @return
	 */
	private EmsMeterThresholdConfig getById(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		return this.dao.getById(emsMeterThresholdConfig);
	}

	/**
	 * 更新状态
	 * @param emsMeterThresholdConfig
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		super.updateStatus(emsMeterThresholdConfig);
	}
	
	/**
	 * 删除数据
	 * @param emsMeterThresholdConfig
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmsMeterThresholdConfig emsMeterThresholdConfig) {
		super.delete(emsMeterThresholdConfig);
	}
	
}
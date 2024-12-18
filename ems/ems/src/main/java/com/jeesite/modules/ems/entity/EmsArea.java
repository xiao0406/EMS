package com.jeesite.modules.ems.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.entity.BaseEntity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.User;
import io.swagger.annotations.*;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 区域表Entity
 * @author 李鹏
 * @version 2023-06-12
 */
@Table(name="ems_area", alias="a", label="区域表信息", columns={
		@Column(name="area_code", attrName="areaCode", label="区域编码", isPK=true),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="area_name", attrName="areaName", label="区域名称", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
		@Column(name="company_code", attrName="companyCode", label="公司编码"),
		@Column(name="company_name", attrName="companyName", label="公司名称", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	},
	orderBy="a.tree_sorts, a.area_code"
)
@Data
@ApiModel(value = "EmsArea对象", description = "区域表Entity")
public class EmsArea extends TreeEntity<EmsArea> {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "区域编码")
	private String areaCode;
	@ApiModelProperty(value = "区域名称")
	private String areaName;
	@ApiModelProperty(value = "公司编码")
	private String companyCode;
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	@ApiModelProperty(value = "区域电表列表")
	private List<EmsAreaMeter> areaMeterList;

	public EmsArea() {
		this(null);
	}

	public EmsArea(String id){
		super(id);
	}

	@Override
	public EmsArea getParent() {
		return parent;
	}

	@Override
	public void setParent(EmsArea parent) {
		this.parent = parent;
	}

	@NotBlank(message="区域编码不能为空")
	@Size(min=0, max=32, message="区域编码长度不能超过 32 个字符")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@NotBlank(message="区域名称不能为空")
	@Size(min=0, max=100, message="区域名称长度不能超过 100 个字符")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
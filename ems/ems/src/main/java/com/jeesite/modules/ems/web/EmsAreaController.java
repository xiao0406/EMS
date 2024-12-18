package com.jeesite.modules.ems.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.service.EmsAreaService;
import com.jeesite.modules.ems.service.EmsMeterService;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 区域表Controller
 * @author 李鹏
 * @version 2023-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ems/emsArea")
@Api(value = "区域表接口", tags = "区域表")
public class EmsAreaController extends BaseController {

	@Resource
	private EmsAreaService emsAreaService;
	@Resource
	private EmsMeterService emsMeterService;

	/**
	 * 获取数据
	 */
//	@ModelAttribute
	public EmsArea get(String areaCode, boolean isNewRecord) {
		return emsAreaService.get(areaCode, isNewRecord);
	}
	
	/**
	 * 管理主页
	 */
	@RequiresPermissions("ems:emsArea:view")
	@RequestMapping(value = "index")
	public String index(EmsArea emsArea, Model model) {
		model.addAttribute("emsArea", emsArea);
		return "modules/ems/emsAreaIndex";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ems:emsArea:view")
	@RequestMapping(value = {"list", ""})
	@ApiOperation(value = "查询列表", notes = "查询列表")
	public String list(EmsArea emsArea, Model model) {
		model.addAttribute("emsArea", emsArea);
		return "modules/ems/emsAreaList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ems:emsArea:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	@ApiOperation(value = "查询列表数据", notes = "查询列表数据")
	public List<EmsArea> listData(EmsArea emsArea) {
		if (StringUtils.isBlank(emsArea.getParentCode())) {
			emsArea.setParentCode(EmsArea.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(emsArea.getAreaName())){
			emsArea.setParentCode(null);
		}
		if (StringUtils.isNotBlank(emsArea.getRemarks())){
			emsArea.setParentCode(null);
		}
		List<EmsArea> list = emsAreaService.findList(emsArea);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ems:emsArea:view")
	@RequestMapping(value = "form")
	@ApiOperation(value = "查看编辑表单", notes = "查看编辑表单")
	public String form(EmsArea emsArea, Model model) {
		// 创建并初始化下一个节点信息
		emsArea = createNextNode(emsArea);
		model.addAttribute("emsArea", emsArea);
		return "modules/ems/emsAreaForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("ems:emsArea:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	@ApiOperation(value = "创建并初始化下一个节点信息", notes = "创建并初始化下一个节点信息")
	public EmsArea createNextNode(EmsArea emsArea) {
		if (StringUtils.isNotBlank(emsArea.getParentCode())){
			emsArea.setParent(emsAreaService.get(emsArea.getParentCode()));
		}
		if (emsArea.getIsNewRecord()) {
			EmsArea where = new EmsArea();
			where.setParentCode(emsArea.getParentCode());
			EmsArea last = emsAreaService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				emsArea.setTreeSort(last.getTreeSort() + 30);
				emsArea.setAreaCode(IdGen.nextCode(last.getAreaCode()));
			}else if (emsArea.getParent() != null){
				emsArea.setAreaCode(emsArea.getParent() + "001");
			}
		}
		// 以下设置表单默认数据
		if (emsArea.getTreeSort() == null){
			emsArea.setTreeSort(EmsArea.DEFAULT_TREE_SORT);
		}
		return emsArea;
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ems:emsArea:edit")
	@PostMapping(value = "save")
	@ResponseBody
	@ApiOperation(value = "保存数据", notes = "保存数据")
	public String save(@Validated EmsArea emsArea) {
		emsAreaService.save(emsArea);
		return renderResult(Global.TRUE, text("保存区域表成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ems:emsArea:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	@ApiOperation(value = "删除数据", notes = "删除数据")
	public String delete(EmsArea emsArea) {
		emsAreaService.delete(emsArea);
		return renderResult(Global.TRUE, text("删除区域表成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param parentCode 设置父级编码返回一级
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("ems:emsArea:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	@ApiOperation(value = "获取树结构数据", notes = "获取树结构数据")
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, String isShowCode,String companyCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		EmsArea where = new EmsArea();
		where.setStatus(EmsArea.STATUS_NORMAL);
		if (StringUtils.isNotBlank(parentCode)){
			where.setParentCodes(parentCode);
		}
		// 设置公司code
		where.setCompanyCode(companyCode);
		List<EmsArea> list = emsAreaService.findList(where);
		for (int i=0; i<list.size(); i++){
			EmsArea e = list.get(i);
			// 过滤非正常的数据
			if (!EmsArea.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getId(), e.getAreaName()));
			map.put("isParent", !e.getIsTreeLeaf());
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 获取树结构数据(包含电表)
	 * @param excludeCode 排除的Code
	 * @param parentCode 设置父级编码返回一级
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequestMapping(value = "treeDataPlus")
	@ResponseBody
	@ApiOperation(value = "获取树结构数据", notes = "获取树结构数据")
	public List<Map<String, Object>> treeDataPlus(String excludeCode, String parentCode, String isShowCode,String companyCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		EmsArea where = new EmsArea();
		where.setStatus(EmsArea.STATUS_NORMAL);
		if (StringUtils.isNotBlank(parentCode)){
			where.setParentCodes(parentCode);
		}
		// 设置公司code
		where.setCompanyCode(companyCode);
		List<EmsArea> list = emsAreaService.findList(where);
		if(CollectionUtils.isEmpty(list)){
			return mapList;
		}
		for (int i=0; i<list.size(); i++){
			EmsArea e = list.get(i);
			// 过滤非正常的数据
			if (!EmsArea.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getId(), e.getAreaName()));
			map.put("isParent", !e.getIsTreeLeaf());
			map.put("leafType", "GROUP_Area");
			mapList.add(map);
			mapList.addAll(emsMeterService.getThisAreaMeterTree(e.getId(),companyCode));
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("ems:emsArea:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	@ApiOperation(value = "修复表结构相关数据", notes = "修复表结构相关数据")
	public String fixTreeData(EmsArea emsArea){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		emsAreaService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}
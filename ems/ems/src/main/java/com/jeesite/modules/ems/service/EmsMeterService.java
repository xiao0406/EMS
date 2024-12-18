package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.DictDataEntity;
import com.jeesite.common.entity.Page;
import com.jeesite.common.expr.BusinessException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ems.dao.EmsMeterDao;
import com.jeesite.modules.ems.entity.*;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserHelper;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 电表设备表Service
 *
 * @author 李鹏
 * @version 2023-06-06
 */
@Service
@Transactional(readOnly = true)
public class EmsMeterService extends CrudService<EmsMeterDao, EmsMeter> {

    @Resource
    private EmsAreaMeterService emsAreaMeterService;
    @Resource
    private EmsMeterOfficeService emsMeterOfficeService;
    @Autowired
    private OfficeService officeService;

    /**
     * 获取单条数据
     *
     * @param emsMeter
     * @return
     */
    @Override
    public EmsMeter get(EmsMeter emsMeter) {
        return super.get(emsMeter);
    }

    /**
     * 查询分页数据
     *
     * @param emsMeter 查询条件
     * @return
     */
    @Override
    public Page<EmsMeter> findPage(EmsMeter emsMeter) {
        return super.findPage(emsMeter);
    }

    /**
     * 查询列表数据
     *
     * @param emsMeter
     * @return
     */
    @Override
    public List<EmsMeter> findList(EmsMeter emsMeter) {
        addCompanyFilter(emsMeter);
        return super.findList(emsMeter);
    }

    private void addCompanyFilter(EmsMeter entity) {
        if (StringUtils.isBlank(entity.getCompanyCode())){
            Company company = EmsUserHelper.userCompany();
            entity.setCompanyCode(company.getCompanyCode());
        }
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsMeter
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsMeter emsMeter) {
        EmsMeter stock = this.dao.findByMeterCode(emsMeter);
        if (emsMeter.getIsNewRecord() && Objects.nonNull(stock)) {
            if(EmsArea.STATUS_NORMAL.equals(stock.getStatus())){
                throw new BusinessException(StringUtils.messageFormat("电表编号【{0}】已存在", emsMeter.getMeterCode()));
            }else{
                emsMeter.setIsNewRecord(false);
                emsMeter.setStatus(EmsArea.STATUS_NORMAL);
                this.updateStatus(emsMeter);
            }
        }
        Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
        emsMeter.setCompanyCode(company.getCompanyCode());
        emsMeter.setCompanyName(company.getCompanyName());
        super.save(emsMeter);

        //保存映射信息
        EmsAreaMeter emsAreaMeter = new EmsAreaMeter();
        emsAreaMeter.setIsNewRecord(true);
        emsAreaMeter.setAreaCode(emsMeter.getAreaCode());
        emsAreaMeter.setMeterCode(emsMeter.getMeterCode());
        emsAreaMeter.setElectricityMark(emsMeter.getElectricityMark());
        //删除原映射
        emsAreaMeterService.deleteOrm(emsAreaMeter);
        //保存新映射
        emsAreaMeterService.save(emsAreaMeter);
    }

    /**
     * 更新状态
     *
     * @param emsMeter
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsMeter emsMeter) {
        super.updateStatus(emsMeter);
    }

    /**
     * 删除数据
     *
     * @param emsMeter
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsMeter emsMeter) {
        this.dao.deleteBatch(emsMeter);
    }

    public Map<String, String> findMeterMap() {
        List<EmsMeter> meterList = this.findAllList(new EmsMeter());
        return meterList.stream().collect(Collectors.toMap(EmsMeter::getMeterCode, EmsMeter::getMeterName, (key1, key2) -> key2));
    }

    private List<EmsMeter> findAllList(EmsMeter emsMeter) {
        return this.dao.findAllList(emsMeter);
    }

    public List<EmsMeterOffice> meterOfficeList(EmsMeterOffice emsMeterOffice) {
        return this.dao.meterOfficeList(emsMeterOffice);
    }

    public List<Map<String, Object>> officeList(CompanyVo companyVo) {
        String companyCode = companyVo.getCompanyCode();
        if (StringUtils.isBlank(companyCode)){
            Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
            companyCode = company.getCompanyCode();
        }

        List<Map<String, Object>> mapList = ListUtils.newArrayList();
        Office where = new Office();
        where.setStatus(Office.STATUS_NORMAL);
        where.setParentCodes(companyCode);
        where.getSqlMap().getWhere().or("office_code", QueryType.EQ, companyCode);
        List<Office> list = officeService.findOfficeList(where);

        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            // 过滤非正常的数据
            if (!Office.STATUS_NORMAL.equals(e.getStatus())){
                continue;
            }
            if(e.getIsTreeLeaf()
                    && (Objects.isNull(e.getExtend())
                        || StringUtils.isBlank(e.getExtend().getExtendS8())
                        || !"0".equals(e.getExtend().getExtendS8()))
                ){
                continue;
            }
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentCode());
            String name = e.getOfficeName();
//            map.put("code", e.getViewCode());
            map.put("name", name);
//            map.put("title", e.getFullName());
            // 返回是否是父节点，如果需要加载用户，则全部都是父节点，来加载用户数据
            map.put("isParent", !e.getIsTreeLeaf());
            mapList.add(map);
        }
        return mapList;
    }

    @Transactional(readOnly = false)
    public void saveOfficeConf(EmsMeter emsMeter) {
        String meterCode = emsMeter.getMeterCode();
        List<EmsMeterOffice> meterOfficeList = emsMeter.getMeterOfficeList();
        //删除当前配置
        emsMeterOfficeService.deleteOrm(meterCode);
        //新增新配置
        emsMeterOfficeService.insertBatch(meterOfficeList);
    }

    public List<DictDataEntity> dictData(EmsMeter emsMeter) {
        List<EmsMeter> list = this.findList(emsMeter);
        ArrayList<DictDataEntity> rlt = new ArrayList<>();
        list.forEach(o -> {
            DictDataEntity de = new DictDataEntity();
            de.setDictLabel(o.getMeterName());
            de.setDictValue(o.getMeterCode());
            rlt.add(de);
        });
        return rlt;
    }

    public Map<String, List<EmsMeter>> findMeterCompanyMap() {
        List<EmsMeter> meterList = this.findAllList(new EmsMeter());
        return meterList.stream().collect(Collectors.groupingBy(EmsMeter::getCompanyCode));
    }

    @Transactional(readOnly = false)
    public void saveThreshold(EmsMeter emsMeter) {
        this.dao.saveThreshold(emsMeter);
    }

    public List<String> findCodes(EmsMeter emsMeter) {
        return this.dao.findCodes(emsMeter);
    }

    public Collection<? extends Map<String, Object>> getThisAreaMeterTree(String areaCode,String companyCode) {
        EmsMeter params = new EmsMeter();
        params.setAreaCode(areaCode);
        //设置公司code
        params.setCompanyCode(companyCode);
        List<EmsMeter> meters = this.findList(params);
        if(CollectionUtils.isEmpty(meters)){
            new ArrayList<>();
        }
        return meters.stream().map(o -> {
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("id", o.getMeterCode());
            map.put("pId", areaCode);
            map.put("name", o.getMeterName());
            map.put("isParent", false);
            map.put("leafType", "GROUP_Device");
            return map;
        }).collect(Collectors.toList());
    }

    public EmsMeter getByEntity(EmsMeter meter) {
        return this.dao.getByEntity(meter);
    }

    /**
     * 查询所有的设备编码
     * @return
     */
    public List<String> findAllMeterCode() {
        return this.dao.findAllMeterCode();
    }

    /**
     * 查询设备信息
     * @param loseList
     * @return
     */
    public List<EmsMeter> getMertInfoByCodeList(List<String> loseList) {
        return this.dao.getMertInfoByCodeList(loseList);
    }
}
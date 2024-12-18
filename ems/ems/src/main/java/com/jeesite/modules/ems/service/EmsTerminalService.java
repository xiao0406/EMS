package com.jeesite.modules.ems.service;

import com.cecec.api.redis.RedisKeyUtil;
import com.jeesite.common.entity.DataScope;
import com.jeesite.common.entity.Page;
import com.jeesite.common.expr.BusinessException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.constant.DefaultValues;
import com.jeesite.modules.ems.dao.EmsTerminalDao;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.entity.EmsMeter;
import com.jeesite.modules.ems.entity.EmsTerminal;
import com.jeesite.modules.ems.entity.EmsTerminalMeter;
import com.jeesite.modules.sys.entity.Company;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 终端表Service
 *
 * @author 李鹏
 * @version 2023-06-14
 */
@Service
@Transactional(readOnly = true)
public class EmsTerminalService extends CrudService<EmsTerminalDao, EmsTerminal> {

    @Resource
    private EmsTerminalMeterService emsTerminalMeterService;

    @Resource
    private RedisService redisService;

    /**
     * 获取单条数据
     *
     * @param emsTerminal
     * @return
     */
    @Override
    public EmsTerminal get(EmsTerminal emsTerminal) {
        return super.get(emsTerminal);
    }

    @Override
    public EmsTerminal get(String terminalCode, boolean isNewRecord) {
        EmsTerminal byCode = this.dao.findByCode(terminalCode);
        if (isNewRecord || Objects.isNull(byCode)) {
            EmsTerminal emsTerminal = new EmsTerminal();
            emsTerminal.setIsNewRecord(isNewRecord);
            return emsTerminal;
        }
        return byCode;
    }

    /**
     * 查询分页数据
     *
     * @param emsTerminal 查询条件
     * @return
     */
    @Override
    public Page<EmsTerminal> findPage(EmsTerminal emsTerminal) {
        Page<EmsTerminal> page = super.findPage(emsTerminal);
        List<EmsTerminal> data = page.getList();
        if(CollectionUtils.isEmpty(data)){
            return page;
        }
        // 缓存查询终端，如果离线则生成告警记录 dmp:terminalCode:861241058527613:status
        List<EmsTerminal> list = this.findAllTerminals(new EmsTerminal());
        List<String> terminalList = list.stream().map(en->en.getTerminalCode()).collect(Collectors.toList());
        List<String> terminalKeys = RedisKeyUtil.terminalStatus(terminalList);
        logger.info("终端列表:----->{}",terminalList);
        //从redis中查询是否存在缓存，若存在则，在线，若不存在，则不在线
        Map<String,String> offLineMap = new HashMap<>();
        for (String terminalKey : terminalKeys) {
            if (!redisService.hasKey(terminalKey)) {
                logger.info("添加map--->{}:{}",terminalKey.split(":")[2],1);
                offLineMap.put(terminalKey.split(":")[2], DefaultValues.BSTRING_ONE);
            }else {
                logger.info("添加map--->{}:{}",terminalKey.split(":")[2],0);
                offLineMap.put(terminalKey.split(":")[2],DefaultValues.STRING_ZERO);
            }
        }
        for(EmsTerminal terminal : data){
            logger.info("设置离线状态终端:----->{}",terminal.getTerminalCode()+":"+offLineMap.get(terminal.getTerminalCode()));
            terminal.setIsOffLine(offLineMap.get(terminal.getTerminalCode()));
        }
        page.setList(data);
        return page;
    }

    private void addCompanyFilter(EmsTerminal entity) {
        if (StringUtils.isBlank(entity.getCompanyCode())){
            Company company = EmsUserHelper.userCompany();
            entity.setCompanyCode(company.getCompanyCode());
        }
    }

    /**
     * 查询列表数据
     *
     * @param emsTerminal
     * @return
     */
    @Override
    public List<EmsTerminal> findList(EmsTerminal emsTerminal) {
        addCompanyFilter(emsTerminal);
        return super.findList(emsTerminal);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param emsTerminal
     */
    @Override
    @Transactional(readOnly = false)
    public void save(EmsTerminal emsTerminal) {
        EmsTerminal stock = this.getById(emsTerminal);
        if (emsTerminal.getIsNewRecord() && Objects.nonNull(stock)) {
            if (EmsArea.STATUS_NORMAL.equals(stock.getStatus())) {
                throw new BusinessException(StringUtils.messageFormat("终端编码【{0}】已存在", emsTerminal.getTerminalCode()));
            } else {
                emsTerminal.setIsNewRecord(false);
                emsTerminal.setStatus(EmsArea.STATUS_NORMAL);
                this.updateStatus(emsTerminal);
            }
        }
        Company company = EmsUserHelper.userCompany(true, "当前为租户管理员账号，非法业务操作");
        emsTerminal.setCompanyCode(company.getCompanyCode());
        emsTerminal.setCompanyName(company.getCompanyName());
        super.save(emsTerminal);

        String terminalCode = emsTerminal.getTerminalCode();
        List<String> meterCodeList = emsTerminal.getMeterCodeList();
        if (!CollectionUtils.isEmpty(meterCodeList)) {
            //保存映射信息
            ArrayList<EmsTerminalMeter> emsTerminalMeters = new ArrayList<>();
            meterCodeList.forEach(o -> {
                EmsTerminalMeter terminalMeter = new EmsTerminalMeter();
                terminalMeter.setIsNewRecord(true);
                terminalMeter.setTerminalCode(terminalCode);
                terminalMeter.setMeterCode(o);
                //添加
                emsTerminalMeters.add(terminalMeter);
            });
            //删除原映射
            emsTerminalMeterService.deleteOrm(terminalCode);
            //保存新映射
            emsTerminalMeterService.insertBatch(emsTerminalMeters);
        }
    }

    private EmsTerminal getById(EmsTerminal emsTerminal) {
        return this.dao.getById(emsTerminal);
    }

    /**
     * 更新状态
     *
     * @param emsTerminal
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(EmsTerminal emsTerminal) {
        super.updateStatus(emsTerminal);
    }

    /**
     * 删除数据
     *
     * @param emsTerminal
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(EmsTerminal emsTerminal) {
        this.dao.deleteBatch(emsTerminal);
    }

    public List<String> findCodes(EmsTerminal emsTerminal) {
        addCompanyFilter(emsTerminal);
        return this.dao.findCodes(emsTerminal);
    }

    public List<EmsTerminal> findAllTerminals(EmsTerminal terminal) {
        return super.findList(terminal);
    }
}
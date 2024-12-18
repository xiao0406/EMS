package com.cscec.dmp.http.imir.yh.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cscec.common.imir.vo.ComponentWeldingData;
import com.cscec.dmp.http.imir.yh.entity.YhCwProd;
import com.cscec.dmp.http.imir.yh.mapper.YhCwProdMapper;
import com.cscec.dmp.http.imir.yh.service.YhCwProdService;
import com.cscec.dmp.http.log.service.HttpProtocolDataService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
* @author duans
* @description 针对表【dcs_yh_cw_prod(部件焊接单元生产数据记录表)】的数据库操作Service实现
* @createDate 2023-08-30 11:12:17
*/
@Service
public class YhCwProdServiceImpl extends ServiceImpl<YhCwProdMapper, YhCwProd>
    implements YhCwProdService {

    @Resource
    private HttpProtocolDataService httpProtocolDataService;

    @Override
    @Async
    public void saveDate(ComponentWeldingData data) {
        YhCwProd cwProd = getData(data);
        boolean exists = baseMapper.exists(new QueryWrapper<YhCwProd>().lambda()
                .eq(YhCwProd::getDeviceCode, cwProd.getDeviceCode())
                .eq(YhCwProd::getRecordDate, cwProd.getRecordDate()));
        if(exists){
            // 更新数据
            baseMapper.update(cwProd, new UpdateWrapper<YhCwProd>().lambda()
                    .eq(YhCwProd::getDeviceCode, cwProd.getDeviceCode())
                    .eq(YhCwProd::getRecordDate, cwProd.getRecordDate()));
        }else {
            // 新增数据
            baseMapper.insert(cwProd);
        }
        httpProtocolDataService.saveLog(data);
    }

    public YhCwProd getData(ComponentWeldingData data){
        LocalDateTime now = LocalDateTime.now();
        YhCwProd yhCwProd = new YhCwProd();
        yhCwProd.setId(IdUtil.getSnowflake().nextIdStr());
        yhCwProd.setDeviceCode(data.getDeviceCode());
        yhCwProd.setDeviceName("部件焊接单元");
        yhCwProd.setRecordDate(now.toLocalDate());
        yhCwProd.setDayRunTime(data.getDayRunTime());
        yhCwProd.setNightRunTime(data.getNightRunTime());
        yhCwProd.setDayCapacity(data.getDayCapacity());
        yhCwProd.setNightCapacity(data.getNightCapacity());
        yhCwProd.setDayUtilizationRate(data.getDayUtilizationRate());
        yhCwProd.setNightUtilizationRate(data.getNightUtilizationRate());
        yhCwProd.setWireWeight(data.getWireWeight());
        yhCwProd.setWireLength(data.getWireLength());
        yhCwProd.setCreateBy("dmp");
        yhCwProd.setCreateDate(now);
        yhCwProd.setUpdateBy("dmp");
        yhCwProd.setUpdateDate(now);
        return yhCwProd;
    }
}





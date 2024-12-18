package com.cscec.dmp.http.imir.sx.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cscec.dmp.http.imir.sx.entity.SxGcProd;
import com.cscec.dmp.http.imir.sx.mapper.SxGcProdMapper;
import com.cscec.dmp.http.imir.sx.service.SxGcProdService;
import com.cscec.common.imir.vo.GrooveCuttingData;
import com.cscec.dmp.http.log.service.HttpProtocolDataService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
* @author duans
* @description 针对表【dcs_sx_gc_prod_copy1(坡口切割单元生产数据记录表)】的数据库操作Service实现
* @createDate 2023-08-30 09:38:02
*/
@Service
public class SxGcProdServiceImpl extends ServiceImpl<SxGcProdMapper, SxGcProd>
    implements SxGcProdService {

    @Resource
    private HttpProtocolDataService httpProtocolDataService;

    @Override
    @Async
    public void saveDate(GrooveCuttingData data) {
        SxGcProd gcProd = getData(data);
        boolean exists = baseMapper.exists(new QueryWrapper<SxGcProd>().lambda()
                .eq(SxGcProd::getDeviceCode, gcProd.getDeviceCode())
                .eq(SxGcProd::getRecordDate, gcProd.getRecordDate()));
        if(exists){
            // 更新数据
            baseMapper.update(gcProd, new UpdateWrapper<SxGcProd>().lambda()
                    .eq(SxGcProd::getDeviceCode, gcProd.getDeviceCode())
                    .eq(SxGcProd::getRecordDate, gcProd.getRecordDate()));
        }else {
            // 新增数据
            baseMapper.insert(gcProd);
        }
        httpProtocolDataService.saveLog(data);
    }

    /**
     * 封装存储对象
     * @param data 传递对象
     * @return 存储对象
     */
    public SxGcProd getData(GrooveCuttingData data){
        SxGcProd prod = new SxGcProd();
        prod.setId(IdUtil.getSnowflake().nextIdStr());
        prod.setDeviceCode(data.getDeviceNumber());
        prod.setRecordDate(data.getDate());
        prod.setDeviceName("坡口切割单元");
        prod.setYieldDay(data.getYieldDay());
        prod.setYieldNight(data.getYieldNight());
        prod.setRunTimeDay(data.getRunTimeDay());
        prod.setRunTimeNight(data.getRunTimeNight());
        prod.setUtilizationDay(data.getUtilizationDay());
        prod.setUtilizationNight(data.getUtilizationNight());
        prod.setMetersDay(data.getMetersDay());
        prod.setMetersNight(data.getMetersNight());
        prod.setDefectRateDay(data.getDefectRateDay());
        prod.setDefectRateNight(data.getDefectRateNight());
        prod.setCreateBy("dmp");
        prod.setCreateDate(LocalDateTime.now());
        prod.setUpdateBy("dmp");
        prod.setUpdateDate(LocalDateTime.now());
        return prod;
    }
}





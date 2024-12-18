package com.cscec.dmp.http.imir.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cscec.dmp.http.imir.sx.entity.SxGcProd;
import com.cscec.common.imir.vo.GrooveCuttingData;

/**
* @author duans
* @description 针对表【dcs_sx_gc_prod(坡口切割单元生产数据记录表)】的数据库操作Service
* @createDate 2023-08-30 09:38:02
*/
public interface SxGcProdService extends IService<SxGcProd> {

    void saveDate(GrooveCuttingData data);
}

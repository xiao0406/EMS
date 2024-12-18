package com.cscec.dmp.http.imir.yh.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cscec.common.imir.vo.ComponentWeldingData;
import com.cscec.dmp.http.imir.yh.entity.YhCwProd;

/**
* @author duans
* @description 针对表【dcs_yh_cw_prod(部件焊接单元生产数据记录表)】的数据库操作Service
* @createDate 2023-08-30 11:12:17
*/
public interface YhCwProdService extends IService<YhCwProd> {

    void saveDate(ComponentWeldingData data);
}

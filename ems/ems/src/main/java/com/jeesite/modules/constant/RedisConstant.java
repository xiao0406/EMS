package com.jeesite.modules.constant;

/**
 * 类说明
 * redis前缀管理类
 * redis key会以“模块编码 + key + 业务code”的形式生成
 *
 * @author 李鹏
 * @date 2022/4/28
 */
public enum RedisConstant {

    //基础数据清洗时刻表key
    _ETL_BASE_SCHEDULE_,
    //十五分钟数据汇总时刻表key
    _ETL_SCHEDULE_QUARTER_,
    //小时数据汇总时刻表key
    _ETL_SCHEDULE_HOUR_,
    //天数据汇总时刻表key
    _ETL_SCHEDULE_DAY_,
    //十五分钟区域数据汇总时刻表key
    _ETL_AREA_SCHEDULE_QUARTER_,
    //小时区域数据汇总时刻表key
    _ETL_AREA_SCHEDULE_HOUR_,
    //天区域数据汇总时刻表key
    _ETL_AREA_SCHEDULE_DAY_,

    //公司计量标识电表缓存
    _COMPANY_MARKED_METER_CACHE_,
    ;


}

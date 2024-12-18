 -- 删除索引DDL
 
 -- ems_alarm_event 报警事件记录表
 DROP INDEX idx_type_date_event_level ON ems_alarm_event;

 
 
  -- ems_area  区域表
 DROP INDEX idx_corp_area_comp_status ON ems_area;

 
 
   -- ems_area_meter  区域电表绑定
 DROP INDEX idx_area_code ON ems_area_meter;
	
	
 -- ems_electric_power_area_consumption 区域电耗表数据基础表  
 DROP INDEX idx_corp_area_date_type ON ems_electric_power_area_consumption;

 
 
 -- ems_electric_power_area_consumption_statistics 区域电耗表数据统计表  
DROP INDEX idx_corp_area_date_type ON ems_electric_power_area_consumption_statistics;



 -- ems_electric_power_consumption 电耗表数据基础表  
DROP INDEX idx_corp_device_date_type ON ems_electric_power_consumption;



 -- ems_electric_power_consumption_statistics 电耗表数据统计表  
DROP INDEX idx_corp_device_date_type ON ems_electric_power_consumption_statistics;



 -- ems_electricity_time_conf 用电分时配置表  
DROP INDEX idx_corp_company_code ON ems_electricity_time_conf;


 -- ems_meter 电表设备表  
DROP INDEX idx_corp_meter_company ON ems_meter;

 -- ems_meter_collected_data 电表采集数据  
DROP INDEX idx_corp_device_date_type ON ems_meter_collected_data;



 -- ems_meter_office 机构电表用电占比配置表  
DROP INDEX idx_meter_code ON ems_meter_office;


 -- ems_meter_threshold_config 电表阈值配置表  
DROP INDEX idx_corp_meter_company ON ems_meter_threshold_config;


 -- ems_meter_yield 电表产量表  
DROP INDEX idx_corp_meter_date ON ems_meter_yield;



 -- ems_office_topic 机构主题  
DROP INDEX idx_office_code ON ems_office_topic;


 -- ems_terminal 终端表  
DROP INDEX idx_corp_terminal ON ems_terminal;


 -- ems_terminal_meter 终端电表绑定表  
DROP INDEX idx_terminal_code ON ems_terminal_meter;


 -- ems_time_share_area_power_consumption 区域峰平谷电耗表数据基础表  
DROP INDEX idx_corp_area_date_type ON ems_time_share_area_power_consumption;



 -- ems_time_share_area_power_consumption_statistics 区域峰平谷电耗表数据统计表  
DROP INDEX idx_corp_area_date_type ON ems_time_share_area_power_consumption_statistics;



 -- ems_time_share_device_runtime 峰平谷设备运行时长表  
DROP INDEX idx_corp_device_date_type ON ems_time_share_device_runtime;



 -- ems_time_share_device_runtime_statistics 峰平谷设备运行时长统计表  
DROP INDEX idx_corp_device_date_type ON ems_time_share_device_runtime_statistics;



 -- ems_time_share_office_consumption 峰平谷部门电耗表数据基础表  
DROP INDEX idx_corp_office_date_type ON ems_time_share_office_consumption;



 -- ems_time_share_office_consumption_statistics 峰平谷部门电耗表数据统计表  
DROP INDEX idx_corp_office_date_type ON ems_time_share_office_consumption_statistics;



 -- ems_time_share_power_consumption 峰平谷电耗表数据基础表  
DROP INDEX idx_corp_device_date_type ON ems_time_share_power_consumption;



 -- ems_time_share_power_consumption_statistics 峰平谷电耗表数据统计表  
DROP INDEX idx_corp_device_date_type ON ems_time_share_power_consumption_statistics;





































	




 
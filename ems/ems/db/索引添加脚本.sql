 -- 添加索引DDL
 
 -- ems_alarm_event 报警事件记录表
 create index idx_type_date_event_level on ems_alarm_event(corp_code,data_type,data_date,event_type,event_level);
 
 
  -- ems_area  区域表

 create index idx_corp_area_comp_status on ems_area(corp_code,area_code,company_code,status);
 
 
   -- ems_area_meter  区域电表绑定
 create index idx_area_code on ems_area_meter(area_code);
 
	
 -- ems_electric_power_area_consumption 区域电耗表数据基础表  
 create index idx_corp_area_date_type on ems_electric_power_area_consumption(corp_code,area_code,data_date,data_type);

 
 
 -- ems_electric_power_area_consumption_statistics 区域电耗表数据统计表  
create index idx_corp_area_date_type on ems_electric_power_area_consumption_statistics(corp_code,area_code,data_date_key,data_type);



 -- ems_electric_power_consumption 电耗表数据基础表  
create index idx_corp_device_date_type on ems_electric_power_consumption(corp_code,device_id,data_date,data_type);



 -- ems_electric_power_consumption_statistics 电耗表数据统计表  
create index idx_corp_device_date_type on ems_electric_power_consumption_statistics(corp_code,device_id,data_date_key,data_type);



 -- ems_electricity_time_conf 用电分时配置表  
create index idx_corp_company_code on ems_electricity_time_conf(corp_code,company_code);


 -- ems_meter 电表设备表  
create index idx_corp_meter_company on ems_meter(corp_code,meter_code,company_code);


 -- ems_meter_collected_data 电表采集数据  
create index idx_corp_device_date_type on ems_meter_collected_data(corp_code,device_id,data_date,data_type);



 -- ems_meter_office 机构电表用电占比配置表  
create index idx_meter_code on ems_meter_office(meter_code);


 -- ems_meter_threshold_config 电表阈值配置表  
create index idx_corp_meter_company on ems_meter_threshold_config(corp_code,meter_code,company_code);


 -- ems_meter_yield 电表产量表  
create index idx_corp_meter_date on ems_meter_yield(corp_code,meter_code,data_month);



 -- ems_office_topic 机构主题  
create index idx_office_code on ems_office_topic(office_code);


 -- ems_terminal 终端表  
create index idx_corp_terminal on ems_terminal(corp_code,terminal_code);


 -- ems_terminal_meter 终端电表绑定表  
create index idx_terminal_code on ems_terminal_meter(terminal_code);


 -- ems_time_share_area_power_consumption 区域峰平谷电耗表数据基础表  
create index idx_corp_area_date_type on ems_time_share_area_power_consumption(corp_code,area_code,data_date,data_type);



 -- ems_time_share_area_power_consumption_statistics 区域峰平谷电耗表数据统计表  
create index idx_corp_area_date_type on ems_time_share_area_power_consumption_statistics(corp_code,area_code,data_date_key,data_type);



 -- ems_time_share_device_runtime 峰平谷设备运行时长表  
create index idx_corp_device_date_type on ems_time_share_device_runtime(corp_code,device_id,data_date,data_type);



 -- ems_time_share_device_runtime_statistics 峰平谷设备运行时长统计表  
create index idx_corp_device_date_type on ems_time_share_device_runtime_statistics(corp_code,device_id,data_date_key,data_type);



 -- ems_time_share_office_consumption 峰平谷部门电耗表数据基础表  
create index idx_corp_office_date_type on ems_time_share_office_consumption(corp_code,office_code,data_date,data_type);



 -- ems_time_share_office_consumption_statistics 峰平谷部门电耗表数据统计表  
create index idx_corp_office_date_type on ems_time_share_office_consumption_statistics(corp_code,office_code,data_date_key,data_type);



 -- ems_time_share_power_consumption 峰平谷电耗表数据基础表  
create index idx_corp_device_date_type on ems_time_share_power_consumption(corp_code,device_id,data_date,data_type);



 -- ems_time_share_power_consumption_statistics 峰平谷电耗表数据统计表  
create index idx_corp_device_date_type on ems_time_share_power_consumption_statistics(corp_code,device_id,data_date_key,data_type);






































	




 
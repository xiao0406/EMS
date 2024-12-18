package com.jeesite.modules.job.task.utils;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.enums.SysYesNoEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.NumberUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.ems.entity.EmsAreaMeter;
import com.jeesite.modules.ems.entity.EmsMeterCollectedData;
import com.jeesite.modules.job.entity.CalculateJobParam;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 区域计算辅助类
 */
@Slf4j
public class Calculatehelper {

    public static final Long ABORT_BIT_POS = -1l;
    //redis缓存过期时间
    public static final Long KEY_ETLSCHEDULE_OUT_TIME = 24 * 60 * 60 * 2L;
    public static final Long ONE_HOUR_TIME_MILLIS = 60 * 60 * 1000l;
    public static final int MINUTE_10_OFF_SET = -10;

    public static Long bitMapOffset(String busiKey, String plTime) {
        return DateUtils.parseDate(plTime).getTime() / 1000 - DateUtils.parseDate(busiKey).getTime() / 1000;
    }

    public static Date getPlTime(String busiKey, Long bitpos) {
        return new Date(DateUtils.parseDate(busiKey).getTime() + bitpos * 1000);
    }

    public static String getExecDate(String execDate) {
        return getExecDate4OS(execDate, 0);
    }

    public static String getExecDate_Def_OS(String execDate) {
        return getExecDate4OS(execDate, MINUTE_10_OFF_SET);
    }

    public static String getExecDate4OS(String execDate, Integer minuteOffSet) {
        if (StringUtils.isEmpty(execDate)) {
            if (Integer.compare(0, minuteOffSet) == 0) {
                execDate = DateUtils.getDate();
            } else {
                execDate = DateUtils.formatDate(DateUtils.calculateMinute(new Date(), minuteOffSet));
            }
        }
        return execDate;
    }

    /**
     * 获取区域统计编码
     *
     * @param emsArea
     * @return
     */
    public static List<String> getMeterCalculateCodeList(EmsArea emsArea) {
        List<String> markedList = getMeterMarkedList(emsArea);
        if (CollectionUtils.isEmpty(markedList)) {
            //如果当前区域没有计量标识的设备，继续下级查找
            markedList.addAll(getMetersMarkList(emsArea.getChildList()));
        }
        return markedList;
    }

    /**
     * 获取区域列表统计编码
     *
     * @param emsAreas
     * @return
     */
    public static List<String> getMetersMarkList(List<EmsArea> emsAreas) {
        List<String> markedList = ListUtils.newArrayList();
        if (!CollectionUtils.isEmpty(emsAreas)) {
            List<EmsArea> children = ListUtils.newArrayList();
            for (EmsArea emsArea : emsAreas) {
                markedList.addAll(getMeterMarkedList(emsArea));
                //收集下级区域列表
                List<EmsArea> childList = emsArea.getChildList();
                if (!CollectionUtils.isEmpty(childList)) {
                    children.addAll(childList);
                }
            }
            if (CollectionUtils.isEmpty(markedList)) {
                markedList.addAll(getMetersMarkList(children));
            }
        }
        return markedList;
    }

    /**
     * 获取当前区域统计编码
     *
     * @param emsArea
     * @return
     */
    private static List<String> getMeterMarkedList(EmsArea emsArea) {
        List<String> markedList = ListUtils.newArrayList();
        //过滤计量标识为 1 的电表，并返回电表编号
        List<EmsAreaMeter> areaMeterList = emsArea.getAreaMeterList();
        if (!CollectionUtils.isEmpty(areaMeterList)) {
            markedList = areaMeterList.stream()
                    .filter(o -> SysYesNoEnum._1.getCode().equals(o.getElectricityMark()))
                    .map(o -> o.getMeterCode())
                    .collect(Collectors.toList());
        }
        return markedList;
    }

    /**
     * 计算视在功率
     *
     * @param totalAp
     * @param totalRp
     * @return
     */
    public static Double apparentPower(Double totalAp, Double totalRp) {
        double sqrt = Math.sqrt(NumberUtils.add(Math.pow(totalAp, 2), Math.pow(totalRp, 2)));
        return BigDecimal.valueOf(sqrt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算三相不平衡度
     *
     * @param vals
     * @return
     */
    public static Double threePhaseUnbalance(Double... vals) {
        double tmp = 0l;
        for (Double aDouble : vals) {
            tmp = NumberUtils.add(tmp, aDouble);
        }
        //求得平均值
        double average = NumberUtils.div(tmp, vals.length, 2);
        ArrayList<Double> absList = new ArrayList<>();
        for (Double aDouble : vals) {
            absList.add(Math.abs(NumberUtils.sub(aDouble, average)));
        }
        //排序后取最大的
        absList.sort((o1, o2) -> {
            return o1 > o2 ? -1 : 1;
        });
        Double aDouble = absList.get(0);
        //三相不平衡度
        return NumberUtils.div(aDouble, average, 2);
    }

    /**
     * 取数据有效的最后一条记录
     *
     * @param collect
     * @return
     */
    public static EmsMeterCollectedData getLastValidData(List<EmsMeterCollectedData> collect) {
        List<EmsMeterCollectedData> filterRlt = collect.stream()
                .filter(o -> SysYesNoEnum._0.getCode().equals(o.getNotValid()))
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(filterRlt) ? null : filterRlt.get(0);
    }

    public static boolean isMissUnvalid(EmsMeterCollectedData entity) {
        return (SysYesNoEnum._1.getCode().equals(entity.getNotValid()) && Double.compare(0d, entity.getPositiveActiveEnergy()) == 0);
    }

    public static CalculateJobParam parseJobParam(String jobParam) {
        CalculateJobParam calculateJobParam = JSONObject.parseObject(jobParam, CalculateJobParam.class);
        if (Objects.isNull(calculateJobParam)) {
            calculateJobParam = new CalculateJobParam();
        }
        return calculateJobParam;
    }

    public static boolean executeCheck(String companyCode, CalculateJobParam calculateJobParam) {
        boolean flag;
        List<String> companyCodes = calculateJobParam.getCompanyCodes();
        if (CollectionUtils.isEmpty(companyCodes)) {
            //列表为空，则表示不指定，不做校验
            flag = true;
        } else {
            if (companyCodes.contains(companyCode)) {
                flag = true;
            } else {
                XxlJobHelper.log("公司编码=》{}，【不在】指定执行公司编号=》{}中，跳过数据处理", companyCode, companyCodes);
                log.info("公司编码=》{}，【不在】指定执行公司编号=》{}中，跳过数据处理", companyCode, companyCodes);
                flag = false;
            }
        }
        return flag;
    }
}

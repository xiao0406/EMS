package com.jeesite.modules.etl.clock;

import com.jeesite.common.constant.enums.TimeUnitEnum;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Slf4j
public class Clock {

    public List<String> pendulum(TimeUnitEnum tunit, int step) {
        List<String> list = new ArrayList<>();
        switch (tunit){
            case YMD_05:
                list = minutePendulum(step);
                break;
            case YMD_04:
                list = hourPendulum(step);
                break;
        }
        return list;
    }

    /**
     * 获取分钟时刻表
     *
     * @return
     */
    public List<String> minutePendulum(int step) {
        Display hour = new Display(24);
        Display minute = new Display(60);

        List<String> list = new ArrayList<>();
        //运行标志
        boolean running = true;
        do {
            //保留数字前面的0
            NumberFormat nf = new DecimalFormat("00");
            String moment = nf.format(hour.getValue()) + ":" + nf.format(minute.getValue()) + ":00";
            log.info("pendulum: {}", moment);
            list.add(moment);
            minute.increase(step);
            if (minute.getValue() == 0) {
                hour.increase();
            }

            if (hour.getValue() == 00 && minute.getValue() == 00) {
                running = false;
            }
        } while (running);

        return list;
    }

    /**
     * 获取小时时刻表
     *
     * @param step
     * @return
     */
    public List<String> hourPendulum(int step) {
        Display hour = new Display(24);

        List<String> list = new ArrayList<>();
        //运行标志
        boolean running = true;
        do {
            //保留数字前面的0
            NumberFormat nf = new DecimalFormat("00");
            String moment = nf.format(hour.getValue());
            log.info("pendulum: {}", moment);
            list.add(moment);
            hour.increase();

            if (hour.getValue() == 00) {
                running = false;
            }
        } while (running);

        return list;
    }

    public static void main(String[] args) {
        List<String> pendulum = new Clock().pendulum(TimeUnitEnum.YMD_04, 1);
    }
}

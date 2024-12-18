package com.cscec.common.imir.util;

import java.math.BigDecimal;

public class UnitConverter {

    public static BigDecimal secondToHour(BigDecimal second){
        return second.divide(new BigDecimal(3600),1, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal secondToHour(int second){
        return secondToHour(new BigDecimal(second));
    }
}

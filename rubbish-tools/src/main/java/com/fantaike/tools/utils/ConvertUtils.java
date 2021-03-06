package com.fantaike.tools.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by lenovo on 2017/9/26.
 */
public class ConvertUtils {

    public static BigDecimal objToBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            ret = new BigDecimal(value.toString());
        }
        return ret;
    }

    /**
     * @param obj 传入的小数
     * @return
     * @desc 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。
     * 2.传入的参数等于0，则直接返回字符串"0.00"
     * 3.大于1的小数，直接格式化返回字符串
     */
    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj);
        } else {
            return df.format(obj);
        }
    }
}

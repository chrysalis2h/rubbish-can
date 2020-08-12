/**
 * 软件著作权：长安新生（深圳）金融投资有限公司
 * <p>
 * 系统名称：马达贷
 */
package com.fantaike.tools.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * @author 郑翔
 */
public class CommonUtil {

    private static final Random random = new Random();

    /**
     * @Description: 方法过时，使用 RandomUtil
     * @Date: 2019\12\27 0027 15:23
     * @Author: MDD-PC
     * @Return
     * @Throws
     */
    @Deprecated
    public static String getRandomNumbers(int it) {
        String[] itArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        String str = "";
        for (int i = 0; i < it; i++) {
            str = new StringBuilder().append(str).append(itArray[getInt(0, itArray.length - 1)]).toString();
        }
        return str;
    }

    /**
     * 生成指定范围随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getInt(int min, int max) {
        int it = random.nextInt(max + 1);
        if (it < min) {
            it = getInt(min, max);
        }
        return it;
    }

    /**
     * @Description: 判断对象是否为null或者空
     * @Date: 2019\12\27 0027 15:20
     * @Author: from themis
     * @Return
     * @Throws
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        } else if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
}

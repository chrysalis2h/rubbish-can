package com.fantaike.tools.utils;

/**
 *   @ClassName: CheckNumber
 *   @Description: 推荐直接使用 NumberUtil
 *   @Author: HeJin
 *   @Date: 2019\12\27 0027 17:04
 *   @Version: v1.0 文件初始创建
 */
@Deprecated
public class CheckNumber {

    public static boolean checkNumber(String number) {
        String reg = "^(([0-9]|([1-9][0-9]{0,9}))((\\.[0-9]{1,5})?))$";
        return number.matches(reg);
    }


    /**
     * 验证输入手机号
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean checkMobile(String mobile) {
        String reg = "^[0-9]{11}$";
        System.out.println("手机" + mobile.matches(reg));
        return mobile.matches(reg);
    }

}

/**
 * 软件著作权：长安新生（深圳）金融投资有限公司
 * <p>
 * 系统名称：马达贷
 */
package com.fantaike.tools.http;

import cn.hutool.log.Log;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 *
 * @author 郑翔
 */
public class IPUtil {

    private static final Log logger = Log.get();

    /**
     * 获取登录IP
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * @param request
     * @return
     */
    public static String getLocale(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return getLocale(ip);
    }

    /**
     * @param ip
     * @return
     */
    public static String getLocale(String ip) {
        return IPDataHandler.findGeography(ip);
    }

    /**
     * 判断是否内网用户
     *
     * @param ip
     * @return
     */
    public static boolean isInner(String ip) {
        String innerIp = "((187\\.68\\.15\\.[1-7])|(10\\.235\\.91\\.193))";
        return ip.matches(innerIp);
        // return ip.indexOf("187.68.15.")>=0 || ip.equals("10.235.91.193");
    }

}

package com.fantaike.tools.log;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;


public class FtkLogger {

    private static Log log = LogFactory.get();

    /**
     * @param businessCode    业务码
     * @param businessName    业务名称
     * @param businessContent 业务内容
     * @param businessStatus  业务状态
     * @param params          自定义参数（格式key=value (String) ,eg: "key=value"）
     * @Description(功能描述) :  ftkInfo,组装JSON数据调用info
     * 打出json格式内容
     * @author(作者) ：  曹轩
     * @date (开发日期)          :  2017-12-5 下午2:10:35
     */
    public static void ftkInfo(String businessCode, String businessName, String businessContent,
                               String businessStatus, String... params) {
        JSONObject json = (JSONObject) TraceInfoObject.get("json");
        if (json == null) {
            json = new JSONObject();
            TraceInfoObject.put("json", json);
        } else {
            json.clear();
        }
        try {
            ftkCommonLog(json, businessCode, businessName, businessContent, businessStatus, params);
            log.info(json.toString());
        } catch (Exception e) {
            json.clear();
            json.put("businessCode", businessCode);
            json.put("businessName", businessName);
            json.put("businessStatus", false);
            if (e != null) {
                //错误异常信息
                json.put("exception", e);
            }
            log.info(json.toJSONString());
        }
    }

    /**
     * @param businessCode    业务码
     * @param businessName    业务名称
     * @param businessContent 业务内容
     * @param businessStatus  业务状态
     * @param e               异常对象e
     * @param params          自定义参数（格式key=value (String) ,eg: "key=value"）
     * @Description(功能描述) :  错误信息日志,组装JSON数据调用error
     * 打出json格式内容
     * @author(作者) ：  曹轩
     * @date (开发日期)          :  2017-12-5 下午3:16:09
     */
    public static void ftkError(String businessCode, String businessName, String businessContent,
                                String businessStatus, Throwable e, String... params) {
        JSONObject json = (JSONObject) TraceInfoObject.get("json");
        if (json == null) {
            json = new JSONObject();
            TraceInfoObject.put("json", json);
        } else {
            json.clear();
        }
        try {
            ftkCommonLog(json, businessCode, businessName, businessContent, businessStatus, params);
            if (e != null) {
                //错误异常信息
                json.put("exception", e);
            }
            log.error(json.toString());
        } catch (Exception e1) {
            json.clear();
            json.put("businessCode", businessCode);
            json.put("businessName", businessName);
            json.put("businessStatus", false);
            if (e != null) {
                json.put("exception", e);
            }
            if (e1 != null) {
                json.put("loggerException", e1);
            }
            log.error(json.toString());
        }
    }

    /**
     * @param json
     * @param businessCode
     * @param businessName
     * @param businessContent
     * @param businessStatus
     * @param params          void
     * @Description(功能描述) :  重构日志组装默认参数
     * @author(作者) ：  曹轩
     * @date (开发日期)          :  2017-12-15 上午9:12:49
     */
    private static void ftkCommonLog(JSONObject json, String businessCode, String businessName, String businessContent,
                                     String businessStatus, String... params) {
        //类、方法信息
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        String className = stacks[3].getClassName();
        String methodName = stacks[3].getMethodName();
        int lineNumber = stacks[3].getLineNumber();
        json.put("className", className);
        json.put("methodName", methodName);
        json.put("lineNumber", lineNumber);
        //业务信息
        json.put("businessCode", businessCode);
        json.put("businessName", businessName);
        json.put("businessContent", businessContent);
        json.put("businessStatus", businessStatus);
        //自定义参数
        if (params != null && params.length > 0) {
            String param_s[] = null;
            for (String param : params) {
                if (param.indexOf("=") > -1) {
                    param_s = param.split("=");
                    json.put(param_s[0].trim(), param_s[1].trim());
                } else {
                    continue;
                }
            }
        }
    }
}
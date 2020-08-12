package com.fantaike.tools.entity;

/**
 *  * @ClassName: BaseLogEntity
 *  * @Description: BaseLogEntity
 *  * @Author: HeJin
 *  * @Date: 2019\12\24 0024 16:53
 *  * @Version: v1.0 文件初始创建
 */
public class BaseLogRequset {
    /**
     * 请求路径
     */
    private String requestUrl;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 请求IP地址
     */
    private String remoteAddr;
    /**
     * 类名及方法名
     */
    private String classMethod;
    /**
     * 方法参数
     */
    private String methodArgs;

    public BaseLogRequset() {
    }

    public BaseLogRequset(String requestUrl, String methodName, String remoteAddr, String classMethod, String methodArgs) {
        this.requestUrl = requestUrl;
        this.methodName = methodName;
        this.remoteAddr = remoteAddr;
        this.classMethod = classMethod;
        this.methodArgs = methodArgs;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String methodArgs) {
        this.methodArgs = methodArgs;
    }
}

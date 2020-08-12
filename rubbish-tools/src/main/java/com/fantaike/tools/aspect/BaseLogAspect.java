package com.fantaike.tools.aspect;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.fantaike.tools.entity.BaseLogRequset;

/**
 *  * @ClassName: SysLogAspect
 *  * @Description: SysLogAspect
 *  * @Author: HeJin
 *  * @Date: 2019\12\20 0020 15:18
 *  * @Version: v1.0 文件初始创建
 */
public class BaseLogAspect {

    // 调用hutool-log
    private static final Log logger = LogFactory.get();

    private static ThreadLocal<Long> startTime = new ThreadLocal<>();

    public void doBefore(BaseLogRequset baseLogRequset) throws Throwable {
        logger.info("============================doBefore============================");
        startTime.set(System.currentTimeMillis());

        //记录请求内容
        logger.info("URL : " + baseLogRequset.getRequestUrl());
        logger.info("HTTP_METHOD : " + baseLogRequset.getMethodName());
        logger.info("IP : " + baseLogRequset.getRemoteAddr());
        logger.info("CLASS_METHOD : " + baseLogRequset.getClassMethod());
        logger.info("ARGS : " + baseLogRequset.getMethodArgs());
    }

    /*public Object doAround(ProceedingJoinPoint point) throws Throwable {
        logger.info("============================doAround-start============================");
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        logger.info("============================doAround-end============================");
        return result;
    }*/

    public void doAfter(BaseLogRequset baseLogRequset) throws Throwable {
        logger.info("============================doAfter============================");
    }

    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("============================doAfterReturning============================");
        // 处理完请求，返回内容
        logger.info("RESPONSE : [{}]", JSON.toJSONString(ret));
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

    public void doAfterThrowing(BaseLogRequset baseLogRequset, Throwable e) throws Throwable {
        logger.info("============================doAfterThrowing============================");
    }
}

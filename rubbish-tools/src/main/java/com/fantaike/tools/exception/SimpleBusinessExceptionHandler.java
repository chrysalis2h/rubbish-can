package com.fantaike.tools.exception;

import com.alibaba.fastjson.JSON;
import com.fantaike.tools.entity.CommonResult;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  * @ClassName: BusinessExceptionHandler
 *  * @Description: BusinessExceptionHandler
 *  * @Author: HeJin
 *  * @Date: 2019\12\18 0018 15:56
 *  * @Version: v1.0 文件初始创建
 */
public class SimpleBusinessExceptionHandler extends SimpleMappingExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                              Exception ex) {
        if (handler == null) {
            response.setStatus(404);
            return new ModelAndView();
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
        RestController restController = handlerMethod.getMethod().getDeclaringClass()
                .getAnnotation(RestController.class);
        if (responseBody != null || restController != null) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                String errMsg = "未知错误";
                PrintWriter writer = response.getWriter();
                if (ex instanceof BusinessException) {
                    errMsg = ex.getMessage();
                } else {
                    errMsg = ex.getMessage();
                    logger.error(ex);
                }
                writer.write(JSON.toJSONString(new CommonResult<String>().error(errMsg)));
                writer.flush();
            } catch (IOException e) {
                logger.error("", e);
            }
            return new ModelAndView();
        } else {
            return null;
        }
    }
}

package com.fantaike.tools.annotation;

import java.lang.annotation.*;

/**
 *  * @ClassName: SysLog
 *  * @Description: SysLog
 *  * @Author: HeJin
 *  * @Date: 2019\12\20 0020 14:59
 *  * @Version: v1.0 文件初始创建
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseLog {

    String value() default "";

}

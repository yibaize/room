package org.bql.utils.builder_clazz.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者： 白泽
 * 时间： 2017/11/2.
 * 描述：生成协议接口文件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Protocol {
    /**默认id为0*/
    String value() default "0";
}

package com.blankj.utilcode.dao;

/**
 * Created by leilei on 2017/7/31.
 */

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

/**
 * 自定义注解
 */
@Inherited  //可以被继承
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)  //可以通过反射读取注解
public @interface MyCustomAnnotation {
    String value();
}
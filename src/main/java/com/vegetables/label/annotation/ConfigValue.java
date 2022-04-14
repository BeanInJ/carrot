package com.vegetables.label.annotation;

/**
 * 标记需要从系统或配置文件注入值
 */
public @interface ConfigValue {
    // 入值为参数名
    String value();
}

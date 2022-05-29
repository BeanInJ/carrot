package com.easily.system.common;

public interface Getter<P,R> {
    /**
     * 直接获取
     */
    R get(P key);

    /**
     * 为null抛出异常
     */
    default R get(P key, boolean cannotNull) {
        R o = null;
        if (cannotNull) {
            o = get(key);
            if (o == null) {
                throw new RuntimeException("获取参数为null");
            }
        }
        return o;
    }

    /**
     * 返回指定类型
     */
    default <T> T get(P key, Class<T> type) {
        return type.cast(get(key));
    }

    /**
     * 返回指定类型，为null抛出异常
     */
    default <T> T get(P key, Class<T> type, boolean cannotNull) {
        T o = null;
        if (cannotNull) {
            o = get(key, type);
            if (o == null) {
                throw new RuntimeException("获取参数为null");
            }
        }
        return o;
    }

    /**
     * 为null时返回指定内容
     */
    default <T> T get(P key, DefaultValue<T> defaultValue) {
        T defaultT = defaultValue.get();
        R g = get(key);
        if (g == null) {
            return defaultT;
        } else {
            return (T) g;
        }
    }
}

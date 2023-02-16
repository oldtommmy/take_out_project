package com.tommychan.takeout.common;

/**
 * 基于ThreadLocal的封装类
 * 用于保存和获取当前用户的 id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

}

package com.blankj.utilcode.util;

/**
 * 单例的助手类延迟初始化
 * Singleton helper class for lazily initialization.
 */
public abstract class SingletonUtils<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonUtils.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}

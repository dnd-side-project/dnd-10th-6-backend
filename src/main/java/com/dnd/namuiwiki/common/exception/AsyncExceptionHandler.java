package com.dnd.namuiwiki.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("AsyncException message={}, declaringClass={}, methodName={}", ex.getMessage(), method.getDeclaringClass(), method.getName(), ex);

        for (Object param : params) {
            log.error("Parameter value - {}, declaringClass={}, methodName={}", param, method.getDeclaringClass(), method.getName());
        }
    }

}

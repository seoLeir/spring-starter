package com.seoLeir.spring.bpp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TransactionBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, Class<?>> transactionBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Transaction.class)){
            transactionBeans.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = transactionBeans.get(beanName);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        log.info("Open transaction");
                        try {
                            return method.invoke(bean, args);
                        } catch (Exception e) {
                            log.info("Rollback transaction");
                            throw e;
                        } finally {
                            System.out.println("Close transaction");
                        }
                    });
        }
        return bean;
    }
}

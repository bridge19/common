package com.shengxun.common.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryContext implements ApplicationContextAware{

    private static final BeanFactoryContext insance = new BeanFactoryContext();
    
    private ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        insance.context = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName){
        return (T) insance.context.getBean(beanName);
    }
}

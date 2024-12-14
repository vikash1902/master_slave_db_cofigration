package com.learn.dbconfig.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Order(1)
public class DataSourceAspect {

    @Before("@annotation(transactional)")
    public void switchDataSource(Transactional transactional) {
        System.err.println("---------> " + transactional.readOnly());
        if(transactional.readOnly()){
            DynamicDataSourceHolder.putDataSource(DataSourceType.SLAVE);
        }else{
            DynamicDataSourceHolder.putDataSource(DataSourceType.MASTER);
        }

    }
}


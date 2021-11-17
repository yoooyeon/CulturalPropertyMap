package com.yooyeon.culturalpropertymap.config;


import com.yooyeon.culturalpropertymap.aop.TimeTraceAop;
import com.yooyeon.culturalpropertymap.heritage.repository.HeritageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final HeritageRepository heritageRepository;


    @Autowired
    public SpringConfig(HeritageRepository heritageRepository) {

        this.heritageRepository = heritageRepository;
    }


    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }



}

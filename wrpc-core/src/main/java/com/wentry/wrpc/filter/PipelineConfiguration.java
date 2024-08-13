package com.wentry.wrpc.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: tangwc
 */
@Configuration
public class PipelineConfiguration {

    @Bean
    @ConditionalOnMissingBean(Pipeline.class)
    public Pipeline pipeline(){
        return new Pipeline();
    }

}

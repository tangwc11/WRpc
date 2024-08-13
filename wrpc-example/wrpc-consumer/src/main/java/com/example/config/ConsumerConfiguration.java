package com.example.config;

import com.example.config.filters.LogInOutBoundFilter;
import com.example.config.filters.MetricsOutBoundFilter;
import com.wentry.wrpc.filter.Pipeline;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: tangwc
 */
@Configuration
public class ConsumerConfiguration {

    @Bean
    public Pipeline pipeline(){
        return new Pipeline()
                .addLast(new MetricsOutBoundFilter())
                .addLast(new LogInOutBoundFilter())
                ;
    }
}

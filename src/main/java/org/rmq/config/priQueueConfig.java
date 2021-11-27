package org.rmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
//优先级队列配置类
@Configuration
public class priQueueConfig {
    private static final String EXCHANGE = "priority-exchange";

    public static final String QUEUE = "priority-queue";

    private static final String ROUTING_KEY = "priority.queue";
    @Bean
    DirectExchange priExchange(){
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    Queue priQueue(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-max-priority",10);//设置最大的优先级数量
        return new Queue(QUEUE,true,false,false,map);
    }
    @Bean
    public Binding priQueueBindpriExchange(
            @Qualifier("priQueue") Queue priQueue,
            @Qualifier("priExchange") DirectExchange priExchange
    ){
        return BindingBuilder.bind(priQueue).to(priExchange).with(ROUTING_KEY);
    }

}

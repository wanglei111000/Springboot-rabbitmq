package org.rmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {
    private static final String DELAYED_EXCHANGE = "DELAYED.EXCHANGE";
    private static final String DELAYED_QUEUE = "DELAYED.QUEUE";
    private static final String DELAYED_ROUTING_KEY = "DELAYED.ROUTINGKEY";
    private static final String DELAYED_EXCHANGE_TYPE = "x-delayed-message";

    @Bean("delayedExchange")
    public CustomExchange delayedExchange(){
        /**
         * name  交换机名称
         * type  交换机类型
         * durable  是否持久化交换机
         * autoDelete 是否自动删除交换机
         * arguments  其他参数
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct"); //延迟类型
        //x-delayed-message  延迟消息
        return new CustomExchange(DELAYED_EXCHANGE,DELAYED_EXCHANGE_TYPE,true,false,arguments);
    }

    @Bean("delayedQueue")
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE);
    }

    @Bean
    public Binding delayedQueueBindingTodelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange){
            return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}

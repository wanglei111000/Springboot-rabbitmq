package org.rmq.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TTLQueueConfig {
    //普通交换机
    private static final String X_EXCHANGE = "X";
    //死信交换机
    private static final String DEAD_LETTER_EXCHANGE = "Y";

    //普通队列
    private static final String QUEUE_A = "QA";
    private static final String QUEUE_B = "QB";
    private static final String QUEUE_C = "QC";
    //死信队列
    private static final String DEAD_LETTER_QUEUE = "QD";

    //普通RoutingKey
    private static final String NOMAL_ROUTINGKEY_A = "XA";
    private static final String NOMAL_ROUTINGKEY_B = "XB";
    private static final String NOMAL_ROUTINGKEY_C = "XC";
    //死信RoutingKey
    private static final String DEAD_ROUTINGKEY= "YD";


    //声明普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
       return  new DirectExchange(X_EXCHANGE);
    }

    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return  new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    //声明两个普通队列
    @Bean("queueA")
    public Queue queueA(){
        //準備給正常隊列的參數
        Map<String,Object> arguments = new HashMap<>(3);
        //设置不能正常消费时的私信交换机的参数
        arguments.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        //设置死信交换机的RoutingKey
        arguments.put("x-dead-letter-routing-key",DEAD_ROUTINGKEY);
        arguments.put("x-message-ttl",10000); //设置过期时间 单位为ms (毫秒)
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        //準備給正常隊列的參數
        Map<String,Object> arguments = new HashMap<>(3);
        //设置不能正常消费时的私信交换机的参数
        arguments.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        //设置死信交换机的RoutingKey
        arguments.put("x-dead-letter-routing-key",DEAD_ROUTINGKEY);
        arguments.put("x-message-ttl",20000); //设置过期时间 单位为ms (毫秒)
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }


    @Bean("queueC")
    public Queue queueC(){
        //準備給正常隊列的參數
        Map<String,Object> arguments = new HashMap<>(3);
        //设置不能正常消费时的私信交换机的参数
        arguments.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        //设置死信交换机的RoutingKey
        arguments.put("x-dead-letter-routing-key",DEAD_ROUTINGKEY);
        //arguments.put("x-message-ttl",20000); //设置过期时间 单位为ms (毫秒)
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //声明死新队列
    @Bean("deadQueueD")
    public Queue deadQueueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }


    //绑定普通交换机
    @Bean
    public Binding queueAbindingToxExchange(@Qualifier("queueA") Queue queueA,
                                            @Qualifier("xExchange") DirectExchange xExchange){
      return BindingBuilder.bind(queueA).to(xExchange).with(NOMAL_ROUTINGKEY_A);
    }

    @Bean
    public Binding queueBbindingToxExchange(@Qualifier("queueB") Queue queueB,
                                            @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with(NOMAL_ROUTINGKEY_B);
    }

    @Bean
    public Binding queueCBindingToxExchange(@Qualifier("queueC") Queue queueC,
                                            @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with(NOMAL_ROUTINGKEY_C);
    }

    //绑定死信交换机
    @Bean
    public Binding deadQueueDbindingToYExchange(@Qualifier("deadQueueD") Queue deadQueueD,
                                            @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(deadQueueD).to(yExchange).with(DEAD_ROUTINGKEY);
    }

}

package org.rmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {

    private static final String CONFIRM_EXCHANGE = "CONFIRM.EXCHANGE";
    private static final String CONFIRM_QUEUE = "CONFIRM.QUEUE";
    private static final String CONFIRM_ROUTING_KEY = "CONFIRM.ROUTINGKEY";


    // 备份交换机 备份被队列
    private static final String BACKUP_EXCHANGE = "BACKUP.EXCHANGE";
    private static final String BACKUP_QUEUE = "BACKUP.QUEUE";
    private static final String WARNING_QUEUE = "WARNING.QUEUE";

//    @Bean("confirmExchange")
//    public DirectExchange confirmExchange(){
//        return  new DirectExchange(CONFIRM_EXCHANGE);
//    }

    //建立普通交换机在无法消费数据是将消息转发给备份交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return (DirectExchange) ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE).build();
    }


    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding confirmBind(@Qualifier("confirmQueue") Queue confirmQueue,
                               @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //声明备份交换机 备份被队列
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    //绑定 备份交换机和备份队列
    @Bean
    public Binding bcackupQueueBindToBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                   @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    @Bean
    public Binding warningQueueBindToBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                   @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

}

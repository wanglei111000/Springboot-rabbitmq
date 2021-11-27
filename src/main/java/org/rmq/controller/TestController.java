package org.rmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class TestController {
    //普通交换机
    private static final String X_EXCHANGE = "X";
    //普通RoutingKey
    private static final String NOMAL_ROUTINGKEY_A = "XA";
    private static final String NOMAL_ROUTINGKEY_B = "XB";
    private static final String NOMAL_ROUTINGKEY_C = "XC";

    //############################################
    private static final String DELAYED_EXCHANGE = "DELAYED.EXCHANGE";
    private static final String DELAYED_ROUTING_KEY = "DELAYED.ROUTINGKEY";
    //############################################


    //############################################
    private static final String CONFIRM_EXCHANGE = "CONFIRM.EXCHANGE";
    private static final String CONFIRM_ROUTING_KEY = "CONFIRM.ROUTINGKEY";
    //############################################

    //############################################
    private static final String EXCHANGE = "priority-exchange";
    public static final String QUEUE = "priority-queue";
    private static final String ROUTING_KEY = "priority.queue";
    //############################################

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ResponseBody
    @GetMapping("/sendmsg/{message}")
    public void sendMsg(@PathVariable("message") String message){
        log.info("当前时间:{},发送消息{}给两个ttl队列",new Date().toString(),message);
        rabbitTemplate.convertAndSend(X_EXCHANGE,NOMAL_ROUTINGKEY_A,"消息来自ttl为10秒的队列:"+message);
        rabbitTemplate.convertAndSend(X_EXCHANGE,NOMAL_ROUTINGKEY_B,"消息来自ttl为20秒的队列:"+message);
    }


    @ResponseBody
    @GetMapping("/sendmsgttl/{message}/{ttltime}")
    public void sendMsg(@PathVariable("message") String message,@PathVariable("ttltime") String ttltime){
        log.info("当前时间:{},发送消息{}给一个ttl队列,过期时间是{}ms",new Date().toString(),message,ttltime);
        rabbitTemplate.convertAndSend(X_EXCHANGE,NOMAL_ROUTINGKEY_C,message,msg->{
            msg.getMessageProperties().setExpiration(ttltime);
            return msg;
        });
    }

    @ResponseBody
    @GetMapping("/sendDelayedmsg/{message}/{delaredTime}")
    public void sendMsg(@PathVariable("message") String message,@PathVariable("delaredTime") Integer delaredTime){
        log.info("当前时间:{},发送消息{}给一个延迟队列,时间是{}ms",new Date().toString(),message,delaredTime);
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE,DELAYED_ROUTING_KEY,message,msg->{
            msg.getMessageProperties().setDelay(delaredTime);
            return msg;
        });
    }


    @ResponseBody
    @GetMapping("/sendConfirmmsg/{message}")
    public void sendConfirmMsg(@PathVariable("message") String message){
        /**
        exchange
        routingKey
        message
        CorrelationData correlationData  放入确认回调的信息
       **/
        String id = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(id);
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,CONFIRM_ROUTING_KEY,message,correlationData);
        System.out.println("发送了编号为:"+id+"消息"+"内容是:"+message);

        String id2 = UUID.randomUUID().toString();
        CorrelationData correlationData2 = new CorrelationData(id2);
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,CONFIRM_ROUTING_KEY+"1234",message,correlationData2);
        System.out.println("发送了编号为:"+id2+"消息"+"内容是:"+message+"key是:"+CONFIRM_ROUTING_KEY+"1234");
    }


    //需要注释掉 MyCallback  ，，因为MyCallback 里面 重写了 rabbitTemplate 的 callback 函数
    @ResponseBody
    @GetMapping("/sendprimsg/{message}")
    public void sendPriMsg(@PathVariable("message") String message){
        for(int i=10;i>1;i--){
            int finalI = i;
            rabbitTemplate.convertAndSend(EXCHANGE,ROUTING_KEY,"queue:"+i, msg -> {
                msg.getMessageProperties().setPriority(finalI);  //设置优先级
                return msg;
            });
        }
    }

}

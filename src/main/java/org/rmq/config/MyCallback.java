//package org.rmq.config;
//  // 需要验证 确认回调 或者消息回退的时候 放开注释
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Slf4j
//@Component
//public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @PostConstruct
//    public void init(){
//        rabbitTemplate.setConfirmCallback(this);
//        rabbitTemplate.setReturnCallback(this);
//    }
//
//   /**
//    * correlationData  消息确认内容
//    * ack 是否收到确认
//    * cause 收到或未收到确认的原因
//    * **/
//
//
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if(ack){
//            String id = correlationData.getId();
//            log.info("确认收到消息,"+"编号:"+id);
//        }else{
//            String id = correlationData.getId();
//            log.info("未能确认收到编号为:"+id+"的消息,失败原因是:"+cause);
//        }
//    }
//
//    @Override
//    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        log.info("消息{}无法路由被交换机{}回退,原因是{},路由key:{}",
//                message.getBody(),exchange,replyText,routingKey);
//    }
//}
//
//

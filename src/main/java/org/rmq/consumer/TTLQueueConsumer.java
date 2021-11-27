package org.rmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TTLQueueConsumer {
    //死信队列
    private static final String DEAD_LETTER_QUEUE = "QD";
    @RabbitListener(queues = DEAD_LETTER_QUEUE)
    public void recieveTTLInfo(Message message, Channel channel){
      String msg = new String(message.getBody());
      log.info("当前时间{},收到死信队列的消息{}",new Date().toString(),msg);
    }
}

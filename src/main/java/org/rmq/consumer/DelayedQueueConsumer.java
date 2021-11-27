package org.rmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class DelayedQueueConsumer {
    private static final String DELAYED_QUEUE = "DELAYED.QUEUE";
    @RabbitListener(queues = DELAYED_QUEUE)
    public void recieveDelayedInfo(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间{},收到延迟队列的消息{}",new Date().toString(),msg);
    }
}

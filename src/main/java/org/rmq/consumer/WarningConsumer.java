package org.rmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningConsumer {
    private static final String WARNING_QUEUE = "WARNING.QUEUE";
    @RabbitListener (queues = WARNING_QUEUE)
    public void recieveWarningMsg(Message message){
        log.info("warning get message"+new String(message.getBody()));
    }
}

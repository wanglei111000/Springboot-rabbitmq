package org.rmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmConsumer {
    private static final String CONFIRM_QUEUE = "CONFIRM.QUEUE";

    @RabbitListener(queues = CONFIRM_QUEUE)
    public void recieveConfirmMsg(Message message){
        System.out.println("ConfirmConsumer 接受到消息:"+new String(message.getBody()));
    }

}

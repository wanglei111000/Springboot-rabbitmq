package org.rmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PriConsumer {
    @RabbitListener(queues ="priority-queue")
    public void hand(String msg){
       log.info("接受到了一个消息:"+msg);
    }

}

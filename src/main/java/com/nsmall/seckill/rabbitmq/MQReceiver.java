package com.nsmall.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @ClassName MQReceiver
 * @Description TODO
 * @Author Sky
 * @Date 2019/6/3 16:09
 * @Version 1.0
 **/
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){
        log.info("receive message:"+message);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info(" topic  queue1 message:"+message);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info(" topic  queue2 message:"+message);
    }

    @RabbitListener(queues=MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
        log.info(" header  queue message:"+new String(message));
    }

}

package com.daoge.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//邮件队列
@Component
public class FanoutSmsConsumer {
	@RabbitListener(queues = "fanout_sms_queue")
	public void process(String msg) {
		System.out.println("短信消费者获取生产者消息msg:" + msg);
	}
}

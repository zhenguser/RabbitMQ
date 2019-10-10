package com.daoge.msg.email;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "new_fanout_email_queue")
public class EmailConsumer {

	@RabbitHandler
	public void process(String msg) {
		System.out.println("�ʼ������߻�ȡ��������Ϣmsg:" + msg);
	}

}

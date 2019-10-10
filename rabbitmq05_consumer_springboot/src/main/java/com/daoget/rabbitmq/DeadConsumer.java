package com.daoget.rabbitmq;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

//死信对接
@Component
public class DeadConsumer {

	@RabbitListener(queues = "dead_queue")
	public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
		String messageId = message.getMessageProperties().getMessageId();
		String msg = new String(message.getBody(), "UTF-8");
		System.out.println("死信邮件消费者获取生产者消息msg:" + msg + ",消息id:" + messageId);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

	}

}

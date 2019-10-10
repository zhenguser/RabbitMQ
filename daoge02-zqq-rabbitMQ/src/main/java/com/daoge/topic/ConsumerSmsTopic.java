package com.daoge.topic;

import com.daoge.utils.MQConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 邮件消费者
public class ConsumerSmsTopic {
	private static final String SMS_QUEUE = "sms_queue_topic";
	// 交换机名称
	private static final String DESTINATION_NAME = "my_topic_estination";

	public static void main(String[] args) throws IOException, TimeoutException {
		System.out.println("短信消费者启动");
		// 1. 建立mq连接
		Connection connection = MQConnectionUtils.newConnection();
		// 2.创建通道
		Channel channel = connection.createChannel();
		// 3.消费声明队列
		channel.queueDeclare(SMS_QUEUE, false, false, false, null);
		// 4.消费者队列绑定交换机 绑定路由件 路由键 绑定邮件和短信
		//channel.queueBind(SMS_QUEUE, DESTINATION_NAME, "email");
		channel.queueBind(SMS_QUEUE, DESTINATION_NAME, "log.*");
		// 5.消费监听消息
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body, "UTF-8");
				System.out.println("短信消费者获取生产消息:" + msg);
			}
		};
		channel.basicConsume(SMS_QUEUE, true, defaultConsumer);

	}

}

package com.daoge.producer1;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.daoge.MQConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Customer {
	private static final String QUEUE_NAME = "test_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		System.out.println("002");
		// 1.获取连接
		Connection newConnection = MQConnectionUtils.newConnection();
		// 2.获取通道
		final Channel channel = newConnection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msgString = new String(body, "UTF-8");
				System.out.println("消费者获取消息:" + msgString);
				// try {
				// // 手动应答签收
				// channel.basicAck(envelope.getDeliveryTag(), false);
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
			}
		};
		// 3.监听队列
		channel.basicConsume(QUEUE_NAME, false, defaultConsumer);

	}

}

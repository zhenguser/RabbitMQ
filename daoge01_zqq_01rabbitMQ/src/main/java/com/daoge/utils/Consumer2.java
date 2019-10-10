package com.daoge.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Consumer2 {
	// 队列名称
	private static final String QUEUE_NAME = "my-queue_2333";

	public static void main(String[] args) throws IOException, TimeoutException {
		System.out.println("消费者启动....02");
		// 1.创建一个新的连接
		Connection connection = MQConnectionUtils.newConnection();
		// 2.创建通道
		final Channel channel = connection.createChannel();
		// 3.消费者关联队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

			// 监听获取消息
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body, "UTF-8");
				System.out.println("消费者获取生产者消息:" + msg);
				try {
					// 模拟应答等待时间
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					// 手动应答 模式 告诉给队列服务器 已经处理成功
					 //channel.basicAck(envelope.getDeliveryTag(), false);
				}

			}

		};
		// 4.设置应答模式 如果为true情况下 表示为自动应答模式 false 表示为手动应答
		channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
		// // 关闭通道和连接
		// channel.close();
		// connection.close();
	}
}

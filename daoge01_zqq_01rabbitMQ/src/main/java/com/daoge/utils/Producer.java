package com.daoge.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

// 简单队列生产者
public class Producer {

	// 队列名称
	private static final String QUEUE_NAME = "my-queue_2333";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 1.创建一个新的连接
		Connection connection = MQConnectionUtils.newConnection();
		// 2.创建通道
		Channel channel = connection.createChannel();
		// 3.创建一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		for (int i = 1; i <= 10; i++) {
			// 4.创建msg
			String msg = "queue_" + i;
			System.out.println("生产者投递消息内容:" + msg);
			// 5.生产者发送消息者
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}

		// 关闭通道和连接
		channel.close();
		connection.close();
	}

}

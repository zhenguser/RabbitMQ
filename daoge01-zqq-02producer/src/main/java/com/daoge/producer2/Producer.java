package com.daoge.producer2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.daoge.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer {
	private static final String QUEUE_NAME = "test_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 1.获取连接
		Connection newConnection = MQConnectionUtils.newConnection();
		// 2.创建通道
		Channel channel = newConnection.createChannel();
		// 3.创建队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);// 保证一次只分发一次 限制发送给同一个消费者 不得超过一条消息
		for (int i = 1; i <= 50; i++) {
			String msg = "test_yushengjun" + i;
			System.out.println("生产者发送消息:" + msg);
			// 4.发送消息
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		channel.close();
		newConnection.close();
	}

}

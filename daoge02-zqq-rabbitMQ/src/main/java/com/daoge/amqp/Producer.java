package com.daoge.amqp;

import com.daoge.utils.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
		try {
			channel.txSelect();//开启事务
			String msg = "queue_daoge" ;
			System.out.println("生产者投递消息内容:" + msg);
			// 5.生产者发送消息者
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			int i = 1 / 0;
			channel.txCommit();//提交事务
		}catch (Exception e){
			System.out.println("生产者消息事物已经回滚");
			channel.txRollback();// 事物回滚

		}finally {
			// 关闭通道和连接
			channel.close();
			connection.close();

		}

	}

}

package com.daoge.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MQConnectionUtils {

	// 创建新的MQ连接
	public static Connection newConnection() throws IOException, TimeoutException {
		// 1.创建新的MQ连接
		ConnectionFactory factory = new ConnectionFactory();
		// 2.设置连接地址
		factory.setHost("127.0.0.1");
		// 3.设置用户名称
		factory.setUsername("admin");
		// 4.设置用户密码
		factory.setPassword("root");
		// 5.设置amqp协议端口号
		factory.setPort(5672);
		// 6.设置VirtualHost地址
		factory.setVirtualHost("/daoge");
		Connection connection = factory.newConnection();
		return connection;
	}

}

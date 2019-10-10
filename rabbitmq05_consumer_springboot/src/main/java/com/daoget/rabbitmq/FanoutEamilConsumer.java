package com.daoget.rabbitmq;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;

//邮件队列
@Component
public class FanoutEamilConsumer {

	// rabbitmq 默认情况下 如果消费者程序出现异常的情况下，会自动实现补偿机制
	// 补偿（重试机制） 队列服务器 发送补偿请求
	// 如果消费端 程序业务逻辑出现异常消息会消费成功吗？
	// @RabbitListener(queues = "fanout_email_queue")
	// public void process(String msg) throws Exception {
	// System.out.println("邮件消费者获取生产者消息msg:" + msg);
	// JSONObject jsonObject = JSONObject.parseObject(msg);
	// String email = jsonObject.getString("email");
	// String emailUrl = "http://127.0.0.1:8083/sendEmail?email=" + email;
	// System.out.println("邮件消费者开始调用第三方邮件服务器,emailUrl:" + emailUrl);
	// JSONObject result = HttpClientUtils.httpGet(emailUrl);
	// // 如果调用第三方邮件接口无法访问，如何实现自动重试.
	// if (result == null) {
	// throw new Exception("调用第三方邮件服务器接口失败!");
	// }
	// System.out.println("邮件消费者结束调用第三方邮件服务器成功,result:" + result + "程序执行结束");
	//
	// }
	// @RabbitListener 底层 使用Aop进行拦截，如果程序没有抛出异常，自动提交事务
	// 如果Aop使用异常通知拦截 获取异常信息的话，自动实现补偿机制 ，该消息会缓存到rabbitmq服务器端进行存放，一直重试到不抛异常为准。

	// 修改重试机制策略 一般默认情况下 间隔5秒重试一次

	// MQ重试机制需要注意的问题
	// MQ消费者幂等性问题如何解决：使用全局ID

	// @RabbitListener(queues = "fanout_email_queue")
	// public void process(Message message, @Headers Map<String, Object>
	// headers, Channel channel) throws Exception {
	// String messageId = message.getMessageProperties().getMessageId();
	// String msg = new String(message.getBody(), "UTF-8");
	// System.out.println("邮件消费者获取生产者消息msg:" + msg + ",消息id:" + messageId);
	// // 重试机制都是间隔性
	//
	// JSONObject jsonObject = JSONObject.parseObject(msg);
	// String email = jsonObject.getString("email");
	// String emailUrl = "http://127.0.0.1:8083/sendEmail?email=" + email;
	// System.out.println("邮件消费者开始调用第三方邮件服务器,emailUrl:" + emailUrl);
	// JSONObject result = HttpClientUtils.httpGet(emailUrl);
	// // 如果调用第三方邮件接口无法访问，如何实现自动重试.
	// if (result == null) {
	// throw new Exception("调用第三方邮件服务器接口失败!");
	// }
	// System.out.println("邮件消费者结束调用第三方邮件服务器成功,result:" + result + "程序执行结束");
	// // 手动ack
	// Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
	// // 手动签收
	// channel.basicAck(deliveryTag, false);
	//
	// }
	// 默认是自动应答模式
	@RabbitListener(queues = "fanout_email_queue")
	public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
		String messageId = message.getMessageProperties().getMessageId();
		String msg = new String(message.getBody(), "UTF-8");
		System.out.println("邮件消费者获取生产者消息msg:" + msg + ",消息id:" + messageId);
		JSONObject jsonObject = JSONObject.parseObject(msg);
		Integer timestamp = jsonObject.getInteger("timestamp");
		try {
			int result = 1 / timestamp;
			System.out.println("result:" + result);
			// 通知mq服务器删除该消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			e.printStackTrace();
			// // 丢弃该消息给死信队列
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
		}

	}

}

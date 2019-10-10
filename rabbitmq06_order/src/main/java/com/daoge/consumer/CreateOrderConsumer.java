package com.daoge.consumer;

import com.alibaba.fastjson.JSONObject;
import com.daoge.entity.OrderEntity;
import com.daoge.mapper.OrderMapper;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 补单消费者
 * 
 * @author 余胜军
 *
 */
@Component
public class CreateOrderConsumer {
	@Autowired
	private OrderMapper orderMapper;

	@RabbitListener(queues = "order_create_queue")
	public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
		String messageId = message.getMessageProperties().getMessageId();
		String msg = new String(message.getBody(), "UTF-8");
		System.out.println("补单消费检查订单是否创建" + msg + ",消息id:" + messageId);
		JSONObject jsonObject = JSONObject.parseObject(msg);
		String orderId = jsonObject.getString("orderId");
		if (StringUtils.isEmpty(orderId)) {
			// 手动签收消息,通知mq服务器端删除该消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		OrderEntity orderEntityResul = orderMapper.findOrderId(orderId);
		if (orderEntityResul != null) {
			System.out.println("订单已经存在!");
			// 手动签收消息,通知mq服务器端删除该消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		System.out.println("订单号码:" + orderId + ",不存在。开始进行补单！");
		// 使用补偿机制
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setName("蚂蚁课堂永久会员充值");
		orderEntity.setOrderCreatetime(new Date());
		// 价格是300元
		orderEntity.setOrderMoney(300d);
		// 状态为 未支付
		orderEntity.setOrderState(0);
		Long commodityId = 30l;
		// 商品id
		orderEntity.setCommodityId(commodityId);
		orderEntity.setOrderId(orderId);

		// ##################################################
		// 1.先下单，创建订单 (往订单数据库中插入一条数据)
		int orderResult = orderMapper.addOrder(orderEntity);
		System.out.println("orderResult:" + orderResult);
		if (orderResult >= 0) {
			// 手动签收消息,通知mq服务器端删除该消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		// 重试进行补偿 多次失败 使用日志记录 采用定时job检查或者 人工补偿

	}
}

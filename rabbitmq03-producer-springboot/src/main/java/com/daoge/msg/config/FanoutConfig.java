package com.daoge.msg.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 
 * 发布订阅模式 配置的交换机类型为 fanout
 * 
 * @author 余胜军
 *
 */
// 注入到spring容器
@Component
public class FanoutConfig {

	// 邮件队列
	private String FANOUT_EMAIL_QUEUE = "new_fanout_email_queue";

	// 短信队列
	private String FANOUT_SMS_QUEUE = "new_fanout_sms_queue";

	// 交换机名称
	private String EXCHANGE_NAME = "fanoutExchange";

	// 1.定义邮件队列
	@Bean
	public Queue fanoutEmailQueue() {
		return new Queue(FANOUT_EMAIL_QUEUE);
	}
	// <bean id="fanoutEmailQueue" class="Queue"

	// 2.定义短信队列
	@Bean
	public Queue fanoutSmsQueue() {
		return new Queue(FANOUT_SMS_QUEUE);
	}

	// 3.定义交换机
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

	// 思考下：topic
	// 3.邮件队列和交换机进行绑定 参数名称和定义队列和交换机方法名称一致
	@Bean
	Binding bindingExchangeEmail(Queue fanoutEmailQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanoutEmailQueue).to(fanoutExchange);
	}

	@Bean
	Binding bindingExchangeSms(Queue fanoutSmsQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanoutSmsQueue).to(fanoutExchange);
	}

}

package com.daoge.msg.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 
 * ��������ģʽ ���õĽ���������Ϊ fanout
 * 
 * @author ��ʤ��
 *
 */
// ע�뵽spring����
@Component
public class FanoutConfig {

	// �ʼ�����
	private String FANOUT_EMAIL_QUEUE = "new_fanout_email_queue";

	// ���Ŷ���
	private String FANOUT_SMS_QUEUE = "new_fanout_sms_queue";

	// ����������
	private String EXCHANGE_NAME = "fanoutExchange";

	// 1.�����ʼ�����
	@Bean
	public Queue fanoutEmailQueue() {
		return new Queue(FANOUT_EMAIL_QUEUE);
	}
	// <bean id="fanoutEmailQueue" class="Queue"

	// 2.������Ŷ���
	@Bean
	public Queue fanoutSmsQueue() {
		return new Queue(FANOUT_SMS_QUEUE);
	}

	// 3.���彻����
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

	// ˼���£�topic
	// 3.�ʼ����кͽ��������а� �������ƺͶ�����кͽ�������������һ��
	@Bean
	Binding bindingExchangeEmail(Queue fanoutEmailQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanoutEmailQueue).to(fanoutExchange);
	}

	@Bean
	Binding bindingExchangeSms(Queue fanoutSmsQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanoutSmsQueue).to(fanoutExchange);
	}

}

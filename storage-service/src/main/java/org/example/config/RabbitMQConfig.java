package org.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_APPROVAL_QUEUE = "order.approval.request";
    public static final String ORDER_APPROVED_QUEUE = "order.approved";
    public static final String ORDER_REJECTED_QUEUE = "order.rejected";
    public static final String EXCHANGE = "order.exchange";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue orderApprovalQueue() {
        return new Queue(ORDER_APPROVAL_QUEUE, true);
    }

    @Bean
    public Queue orderApprovedQueue() {
        return new Queue(ORDER_APPROVED_QUEUE, true);
    }

    @Bean
    public Queue orderRejectedQueue() {
        return new Queue(ORDER_REJECTED_QUEUE, true);
    }

    @Bean
    public Binding bindingApproval() {
        return BindingBuilder.bind(orderApprovalQueue()).to(exchange()).with(ORDER_APPROVAL_QUEUE);
    }

    @Bean
    public Binding bindingApproved() {
        return BindingBuilder.bind(orderApprovedQueue()).to(exchange()).with(ORDER_APPROVED_QUEUE);
    }

    @Bean
    public Binding bindingRejected() {
        return BindingBuilder.bind(orderRejectedQueue()).to(exchange()).with(ORDER_REJECTED_QUEUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
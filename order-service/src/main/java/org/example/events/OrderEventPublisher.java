package org.example.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataAccess.models.OrderCustom;
import org.example.dataAccess.models.OrderInStock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static org.example.config.RabbitMQConfig.ORDER_APPROVAL_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "order.exchange";

    public void publishOrderSentForApproval(OrderInStock order) {
        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                order.getId(),
                "IN_STOCK",
                order.getCarId(),
                null,
                null,
                UUID.randomUUID().toString()
        );
        rabbitTemplate.convertAndSend(EXCHANGE, ORDER_APPROVAL_QUEUE, event);
        log.info("Published OrderSentForApproval for order: {}", order.getId());
    }

    public void publishOrderSentForApproval(OrderCustom order) {
        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                order.getId(),
                "CUSTOM",
                null,
                order.getCarModel().getId(),
                new ArrayList<>(order.getSelectedComponentIds().values()),
                UUID.randomUUID().toString()
        );
        rabbitTemplate.convertAndSend(EXCHANGE, ORDER_APPROVAL_QUEUE, event);
        log.info("Published OrderSentForApproval for order: {}", order.getId());
    }
}

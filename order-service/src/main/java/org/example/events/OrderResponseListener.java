package org.example.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataAccess.models.*;
import org.example.dataAccess.repositories.IOrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.example.config.RabbitMQConfig.ORDER_APPROVED_QUEUE;
import static org.example.config.RabbitMQConfig.ORDER_REJECTED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderResponseListener {

    private final IOrderRepository orderRepository;

    @RabbitListener(queues = ORDER_APPROVED_QUEUE)
    @Transactional
    public void handleOrderApproved(OrderApprovedEvent event) {
        log.info("Received OrderApproved for order: {}", event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));

        if (order instanceof OrderInStock inStock) {
            inStock.setStatus(OrderStatusInStock.READY);
        } else if (order instanceof OrderCustom custom) {
            custom.setStatus(OrderStatusCustom.READY);
        }

        orderRepository.save(order);
        log.info("Order {} status updated to READY", event.getOrderId());
    }

    @RabbitListener(queues = ORDER_REJECTED_QUEUE)
    @Transactional
    public void handleOrderRejected(OrderRejectedEvent event) {
        log.info("Received OrderRejected for order: {}, reason: {}", event.getOrderId(), event.getReason());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));

        if (order instanceof OrderInStock inStock) {
            inStock.setStatus(OrderStatusInStock.CANCELLED);
        } else if (order instanceof OrderCustom custom) {
            custom.setStatus(OrderStatusCustom.CANCELLED);
        }

        orderRepository.save(order);
        log.info("Order {} status updated to CANCELLED", event.getOrderId());
    }
}

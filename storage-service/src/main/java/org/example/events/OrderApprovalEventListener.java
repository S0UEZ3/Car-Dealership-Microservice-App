package org.example.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.services.AssemblyOrderService;
import org.example.application.services.CarStockService;
import org.example.application.services.ComponentStockService;
import org.example.dataAccess.models.AssemblyOrder;
import org.example.dataAccess.models.AssemblyOrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static org.example.config.RabbitMQConfig.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderApprovalEventListener {

    private final CarStockService carStockService;
    private final ComponentStockService componentStockService;
    private final AssemblyOrderService assemblyOrderService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = ORDER_APPROVAL_QUEUE)
    public void handleOrderSentForApproval(OrderSentForApprovalEvent event) {
        log.info("Received OrderSentForApproval: {}", event);

        boolean approved;
        String rejectReason = null;

        try {
            if ("IN_STOCK".equals(event.getOrderType())) {
                approved = carStockService.isCarInStock(event.getCarId());
                if (approved) {
                    carStockService.reserveCar(event.getCarId());
                } else {
                    rejectReason = "Car not in stock: " + event.getCarId();
                }
            } else {
                approved = event.getComponentIds().stream()
                        .allMatch(compId -> componentStockService.isPartAvailable(compId, 1));
                if (!approved) {
                    rejectReason = "Not all components available";
                }
            }

            AssemblyOrder assemblyOrder = new AssemblyOrder();
            assemblyOrder.setSourceOrderId(event.getOrderId());
            assemblyOrder.setSourceOrderType(event.getOrderType());
            assemblyOrder.setCarId(event.getCarId());
            assemblyOrder.setCarModelId(event.getCarModelId());
            if (event.getComponentIds() != null) {
                assemblyOrder.setRequiredComponentIds(event.getComponentIds().toString());
            }
            assemblyOrder.setStatus(approved ? AssemblyOrderStatus.ASSEMBLED : AssemblyOrderStatus.FAIL);
            assemblyOrderService.create(assemblyOrder);

            if (approved) {
                rabbitTemplate.convertAndSend(EXCHANGE, ORDER_APPROVED_QUEUE,
                        new OrderApprovedEvent(event.getOrderId(), event.getTraceId()));
            } else {
                rabbitTemplate.convertAndSend(EXCHANGE, ORDER_REJECTED_QUEUE,
                        new OrderRejectedEvent(event.getOrderId(), rejectReason, event.getTraceId()));
            }

        } catch (Exception e) {
            log.error("Error processing order approval", e);
            rabbitTemplate.convertAndSend(EXCHANGE, ORDER_REJECTED_QUEUE,
                    new OrderRejectedEvent(event.getOrderId(), "Internal error: " + e.getMessage(), event.getTraceId()));
        }
    }
}

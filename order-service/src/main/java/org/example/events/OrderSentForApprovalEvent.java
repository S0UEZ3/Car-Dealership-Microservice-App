package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSentForApprovalEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID orderId;
    private String orderType;
    private UUID carId;
    private UUID carModelId;
    private List<UUID> componentIds;
    private String traceId;
}
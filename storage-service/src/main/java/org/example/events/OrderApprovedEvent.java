package org.example.events;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderApprovedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID orderId;
    private String traceId;
}

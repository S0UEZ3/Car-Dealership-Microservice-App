package org.example.presentation.dto.objects;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private UUID componentId;
    private Integer quantity;
}

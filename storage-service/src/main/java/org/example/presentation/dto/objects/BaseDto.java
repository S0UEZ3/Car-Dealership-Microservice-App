package org.example.presentation.dto.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean removed;
}
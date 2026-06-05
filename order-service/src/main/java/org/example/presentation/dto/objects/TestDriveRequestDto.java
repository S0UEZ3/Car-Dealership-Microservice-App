package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.dataAccess.models.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Test drive request data transfer object")
@EqualsAndHashCode(callSuper = true)
public class TestDriveRequestDto extends BaseDto {
    @Schema(description = "User requesting test drive")
    private UserDto user;

    @Schema(description = "Car ID for test drive")
    private UUID carId;

    @Schema(description = "Scheduled test drive start time")
    private LocalDateTime testDriveStartDateTime;

    @Schema(description = "Request status")
    private TestDriveStatus testDriveStatus;
}

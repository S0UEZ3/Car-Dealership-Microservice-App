package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.dataAccess.models.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User data transfer object")
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {
    @Schema(description = "User full name", example = "John Doe")
    private String name;

    @Schema(description = "User email", example = "john@example.com")
    private String email;

    @Schema(description = "User phone", example = "+1234567890")
    private String phone;

    @Schema(description = "User role")
    private Role role;
}

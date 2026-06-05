package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_drive_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestDriveRequest extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "car_id")
    private UUID carId;

    private LocalDateTime testDriveStartDateTime;

    @Enumerated(EnumType.STRING)
    private TestDriveStatus testDriveStatus;
}

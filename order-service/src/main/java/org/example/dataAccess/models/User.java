package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    private String name;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;
}

package edu.university.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "administrators")
public class Administrator extends Person {

    @Column(name = "admin_code", nullable = false, length = 30)
    private String adminCode;

    @Column(name = "department", length = 120)
    private String department;

    @Column(name = "position", length = 120)
    private String position;
}

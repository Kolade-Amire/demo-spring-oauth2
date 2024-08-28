package com.kolade.demo_spring_oauth2.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@RequiredArgsConstructor
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private Role role;

}

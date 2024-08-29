package com.kolade.demo_spring_oauth2.authentication.token;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    private Integer userId;
    @Column(unique = true, nullable = false)
    private String token;
    private String tokenType = TokenType.BEARER.name();
    private boolean isExpired;
    private boolean isRevoked;

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", type='" + tokenType + '\'' +
                ", isExpired='" + isExpired + '\'' +
                ", isRevoked='" + isRevoked + '\'' +
                '}';
    }


}


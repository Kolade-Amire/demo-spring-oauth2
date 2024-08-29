package com.kolade.demo_spring_oauth2.authentication.token;



import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    private String userId;
    private String token;
    private String tokenType = OAuth2AccessToken.TokenType.BEARER.getValue();
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


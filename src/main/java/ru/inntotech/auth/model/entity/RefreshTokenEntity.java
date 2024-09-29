package ru.inntotech.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Setter
@Getter
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    private UUID value;
    private Duration refreshTokenExpiration;
}

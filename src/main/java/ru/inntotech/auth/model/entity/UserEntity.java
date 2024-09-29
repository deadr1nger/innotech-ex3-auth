package ru.inntotech.auth.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.inntotech.auth.model.enumeration.RoleType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@Table(name = "`user`")
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    @Email
    private String email;
    private String password;
    @ElementCollection
    private Set<RoleType> roles = new HashSet<>();



}

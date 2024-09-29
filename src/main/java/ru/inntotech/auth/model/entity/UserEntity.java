package ru.inntotech.auth.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<RoleType> roles = new HashSet<>();


}

package ru.inntotech.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.inntotech.auth.model.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    public Optional<UserEntity> findByUsername(String username);


}

package ru.inntotech.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.inntotech.auth.model.entity.RefreshTokenEntity;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    public Optional<RefreshTokenEntity> findByValue(UUID value);
}

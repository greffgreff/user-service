package io.rently.userservice.interfaces;

import io.rently.userservice.dtos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("FROM users WHERE provider = :provider AND provider_id = :providerId")
    Optional<User> findByProviderInfo(@Param("provider") String provider, @Param("providerId") String providerId);


    @Query("FROM users WHERE id = :id")
    Optional<User> findById(@Param("id") String id);

    @Transactional
    @Modifying
    @Query("DELETE FROM users WHERE id = :id")
    void deleteById(@Param("id") String id);
}

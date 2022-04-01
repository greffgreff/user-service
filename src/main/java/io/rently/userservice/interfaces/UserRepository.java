package io.rently.userservice.interfaces;

import io.rently.userservice.dtos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("FROM users WHERE provider = :provider AND provider_id = :providerId")
    User findByProviderInfo(@Param("provider") String provider, @Param("providerId") String providerId);
}

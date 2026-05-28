package com.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronic.store.entities.RefreshToken;
import com.electronic.store.entities.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUser(User user);
}

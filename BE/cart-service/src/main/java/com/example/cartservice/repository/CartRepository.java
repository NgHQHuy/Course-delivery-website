package com.example.cartservice.repository;

import com.example.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteByUserId(Long id);

    Optional<Cart> findByUserId(Long userId);
}

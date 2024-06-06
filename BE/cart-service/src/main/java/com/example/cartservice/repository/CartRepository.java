package com.example.cartservice.repository;

import com.example.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteAllByUserId(Long id);

    List<Cart> findAllByUserId(Long id);
}

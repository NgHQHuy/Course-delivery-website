package com.example.cartservice.service;

import com.example.cartservice.entity.Cart;
import com.example.cartservice.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;

    public Cart save(Cart c) {
        return cartRepository.save(c);
    }

    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    public void delete(Long id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllById(Long id) {
        cartRepository.deleteAllByUserId(id);
    }

    public List<Cart> getAllByUID(Long id) {
        return cartRepository.findAllByUserId(id);
    }
}

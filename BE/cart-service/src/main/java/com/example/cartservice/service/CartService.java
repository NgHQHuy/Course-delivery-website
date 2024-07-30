package com.example.cartservice.service;

import com.example.cartservice.dto.CartRequest;
import com.example.cartservice.entity.Cart;
import com.example.cartservice.entity.CartItem;
import com.example.cartservice.exception.SearchNotFoundException;
import com.example.cartservice.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;

    public Cart save(Cart c) {
        return cartRepository.save(c);
    }

    public Optional<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    public void deleteCourseFromCart(CartRequest request) {
        Optional<Cart> cartOptional = findByUserId(request.getUserId());
        if (cartOptional.isEmpty()) throw new SearchNotFoundException("Không tìm thấy giỏ hàng");
        Cart cart = cartOptional.get();

        for (CartItem item : cart.getItems()) {
            if (item.getCourseId().equals(request.getCourseId())) {
                cart.getItems().remove(item);
                break;
            }
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteAllById(Long id) {
        cartRepository.deleteByUserId(id);
    }

}

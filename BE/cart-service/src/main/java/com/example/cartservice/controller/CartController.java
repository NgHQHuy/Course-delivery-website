package com.example.cartservice.controller;

import com.example.cartservice.dto.CartRequest;
import com.example.cartservice.dto.BaseResponse;
import com.example.cartservice.dto.CourseDto;
import com.example.cartservice.dto.UserDto;
import com.example.cartservice.entity.Cart;
import com.example.cartservice.entity.CartItem;
import com.example.cartservice.exception.CourseAlreadyInCartException;
import com.example.cartservice.exception.SearchNotFoundException;
import com.example.cartservice.service.CartService;
import com.example.cartservice.service.CourseService;
import com.example.cartservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Thêm khóa học vào giỏ hàng")
    @PostMapping("add")
    public ResponseEntity<Cart> addToCart(@Valid @RequestBody CartRequest request) {
        CourseDto courseDto = courseService.getCourse(request.getCourseId());
        if (courseDto == null) throw new SearchNotFoundException("Course is not found");

        UserDto userDto = userService.getUser(request.getUserId());
        if (userDto == null) throw new SearchNotFoundException("User is not found");

        Cart cart;
        Optional<Cart> cartOptional = cartService.findByUserId(request.getUserId());
        cart = cartOptional.orElseGet(Cart::new);

        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getCourseId().equals(courseDto.getId())) throw new CourseAlreadyInCartException("Course is already in cart");
        }

        CartItem item = new CartItem();
        cart.setUserId(request.getUserId());
        item.setCourseId(courseDto.getId());
        item.setPrice(courseDto.getPrice());
        cart.getItems().add(item);
        double updatedTotal = cart.getTotal() + courseDto.getPrice();
        cart.setTotal(updatedTotal);

        Cart savedCart = cartService.save(cart);
        return ResponseEntity.ok(savedCart);
    }

    @Operation(summary = "Lấy thông tin về tất cả các món có trong giỏ")
    @GetMapping("{userId}")
    public ResponseEntity<Set<CartItem>> getAll(@PathVariable Long userId) {
        Optional<Cart> cartOptional = cartService.findByUserId(userId);
        if (cartOptional.isEmpty()) throw new SearchNotFoundException("Không tìm thấy giỏ hàng");
        Cart cart = cartOptional.get();
        return ResponseEntity.ok(cart.getItems());
    }

    @Operation(summary = "Gỡ một món khỏi giỏ")
    @PostMapping("delete")
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody CartRequest request) {
        cartService.deleteCourseFromCart(request);
        return new ResponseEntity<>(new BaseResponse("Success"), HttpStatus.OK);
    }

    @Operation(summary = "Xóa tất cả khỏi giỏ hàng của tài khoản")
    @DeleteMapping("user/{uid}")
    public ResponseEntity<BaseResponse> deleteAllByUID(@Parameter(description = "ID tài khoản")
            @PathVariable Long uid) {
        cartService.deleteAllById(uid);
        return new ResponseEntity<>(new BaseResponse("Success"), HttpStatus.OK);
    }

    @Operation(summary = "Lấy tất cả thông tin về giỏ hàng của tài khoản")
    @GetMapping("user/{uid}")
    public ResponseEntity<Cart> getAllCartByUserID(@Parameter(description = "ID tài khoản")
            @PathVariable Long uid) {
        Optional<Cart> cart = cartService.findByUserId(uid);
        if (cart.isEmpty()) throw new SearchNotFoundException("Không tìm thấy giỏ hàng");
        return ResponseEntity.ok(cart.get());
    }

//    @GetMapping("test/{courseId}")
//    public ResponseEntity<?> test(@PathVariable Long courseId) {
//        CourseDto dto = courseService.getCourse(courseId);
//        System.out.println(dto);
//        return ResponseEntity.status(200).build();
//    }
}

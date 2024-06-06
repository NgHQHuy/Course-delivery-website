package com.example.cartservice.controller;

import com.example.cartservice.dto.BaseResponse;
import com.example.cartservice.dto.ResponseWithMessage;
import com.example.cartservice.entity.Cart;
import com.example.cartservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Operation(summary = "Thêm/sửa thông tin giỏ hàng")
    @PostMapping
    public ResponseEntity<Cart> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin về món đồ được thêm vào giỏ. Bổ sung id định danh cho từng mục trong giỏ hàng để thay đổi. Nhập sai id hoặc bỏ qua id sẽ thêm mới vào CSDL.")
            @RequestBody Cart cart) {
        Cart savedCart = cartService.save(cart);
        return ResponseEntity.ok(savedCart);
    }

    @Operation(summary = "Lấy thông tin về tất cả các món có trong giỏ")
    @GetMapping
    public ResponseEntity<List<Cart>> getAll() {
        List<Cart> carts = cartService.getAll();
        return ResponseEntity.ok(carts);
    }

    @Operation(summary = "Gỡ một món khỏi giỏ")
    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@Parameter(description = "id cho từng mục trong giỏ hàng được thêm vào")
            @PathVariable Long id) {
        cartService.delete(id);
        return new ResponseEntity<>(new ResponseWithMessage(0, "Success"), HttpStatus.OK);
    }

    @Operation(summary = "Xóa tất cả khỏi giỏ hàng của tài khoản")
    @DeleteMapping("user/{uid}")
    public ResponseEntity<BaseResponse> deleteAllByUID(@Parameter(description = "ID tài khoản")
            @PathVariable Long uid) {
        cartService.deleteAllById(uid);
        return new ResponseEntity<>(new ResponseWithMessage(0, "Success"), HttpStatus.OK);
    }

    @Operation(summary = "Lấy tất cả thông tin về giỏ hàng của tài khoản")
    @GetMapping("user/{uid}")
    public ResponseEntity<List<Cart>> getAllCartByUserID(@Parameter(description = "ID tài khoản")
            @PathVariable Long uid) {
        List<Cart> carts = cartService.getAllByUID(uid);
        return ResponseEntity.ok(carts);
    }
}

package com.example.dreamshop.dto;

import com.example.dreamshop.model.Cart;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> items;
    private CartDto cart;

}

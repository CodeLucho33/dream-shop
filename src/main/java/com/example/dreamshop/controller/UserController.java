package com.example.dreamshop.controller;

import com.example.dreamshop.dto.UserDto;
import com.example.dreamshop.exceptions.AlreadyExistsException;
import com.example.dreamshop.exceptions.ResourceNotFoundException;
import com.example.dreamshop.model.User;
import com.example.dreamshop.request.CreateUserRequest;
import com.example.dreamshop.request.UserUpdateRequest;
import com.example.dreamshop.response.ApiResponse;
import com.example.dreamshop.service.user.IUserService;
import com.example.dreamshop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;


    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
               return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestParam CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Create Use Success", userDto));
        } catch (AlreadyExistsException e) {
           return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
@PutMapping("/{userId}/update")
public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
    try {
        User user = userService.updateUser(request, userId);
        UserDto userDto = userService.convertUserToUserDto(user);
        return ResponseEntity.ok(new ApiResponse("Update Use Success", userDto));
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
    }
}

@DeleteMapping("/{userId}/delete")
public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long userId) {
    try {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
    } catch (ResourceNotFoundException e) {

    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));    }
}
}

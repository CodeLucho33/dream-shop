package com.example.dreamshop.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//This class is used to response to front
@AllArgsConstructor
@Data
public class ApiResponse {
   private String message;
   private Object data;

}

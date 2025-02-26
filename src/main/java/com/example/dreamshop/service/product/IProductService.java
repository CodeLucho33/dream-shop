package com.example.dreamshop.service.product;

import com.example.dreamshop.dto.ProductDto;
import com.example.dreamshop.model.Product;
import com.example.dreamshop.request.AddProductRequest;
import com.example.dreamshop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    //Methodts to comunicate with DB
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto convertToDto(Product product);

}

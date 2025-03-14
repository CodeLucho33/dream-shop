package com.example.dreamshop.service.product;

import com.example.dreamshop.dto.ImageDto;
import com.example.dreamshop.dto.ProductDto;
import com.example.dreamshop.exceptions.AlreadyExistsException;
import com.example.dreamshop.exceptions.ProductNotFoundException;
import com.example.dreamshop.exceptions.ResourceNotFoundException;
import com.example.dreamshop.model.Category;
import com.example.dreamshop.model.Image;
import com.example.dreamshop.model.Product;
import com.example.dreamshop.repository.CategoryRepository;
import com.example.dreamshop.repository.ImageRepository;
import com.example.dreamshop.repository.ProductRepository;
import com.example.dreamshop.request.AddProductRequest;
import com.example.dreamshop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found in the DB
        // If Yes, set it as the new product category
        // If No, the save it as a new category
        // The set as the new product category.


        if (productExists(request.getBrand(), request.getName())) {
            throw new AlreadyExistsException(request.getBrand()+ "Already exists this product");
        }

        Category category = categoryRepository.findByName(request.getCategory().getName());
        if (category == null) {
            category = new Category(request.getCategory().getName());
            category = categoryRepository.save(category);
        }
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }
    private Product createProduct(AddProductRequest request, Category category) {
     return new Product(
             request.getName(),
             request.getBrand(),
             request.getPrice(),
             request.getInventory(),
             request.getDescription(),
             category
     );

    }
    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name,brand);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
         productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        ()-> {
                    throw new ProductNotFoundException("Product not found!")
                            ;});
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
      return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
     return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}

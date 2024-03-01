package com.inn.productservice.service;

import com.inn.productservice.dto.ProductDto;
import com.inn.productservice.model.Product;
import com.inn.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .compound(productDto.getCompound())
                .price(productDto.getPrice())
                .build();

        productRepository.save(product);

    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductDto).toList();
    }

    private ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .compound(product.getCompound())
                .price(product.getPrice())
                .build();
    }

    public ProductDto getByName(String name){

        if (productRepository.findByName(name) != null) {
            Product product = productRepository.findByName(name);
            return mapToProductDto(product);
        } else {
            throw new RuntimeException("No such candies");
        }
    }

    public List<ProductDto> getByDescriptionContainingWord(String word){
        if (productRepository.findByDescriptionContainingWord(word) != null) {
            List<Product> products = productRepository.findByDescriptionContainingWord(word);
            return products.stream().map(this::mapToProductDto).toList();
        } else {
            throw new RuntimeException("Compound not found");
        }
    }
}


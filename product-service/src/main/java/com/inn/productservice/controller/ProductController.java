package com.inn.productservice.controller;

import com.inn.productservice.dto.ProductDto;
import com.inn.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public void createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/candy-name/{name}")
    public ProductDto getProductByName(@PathVariable String name){
        return productService.getByName(name);
    }

    @GetMapping("/{word}")
    public List<ProductDto> getProductsByCompound(@PathVariable String word) {
        return productService.getByDescriptionContainingWord(word);
    }
}

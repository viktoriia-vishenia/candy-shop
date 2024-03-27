package com.inn.productservice.controller;

import com.inn.productservice.dto.ProductDto;
import com.inn.productservice.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public void createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
    }

    @GetMapping("/candy-name/{name}")
    public ProductDto getProductByName(@PathVariable String name){
        return productService.getByName(name);
    }

    @GetMapping("/get/{word}")
    public List<ProductDto> getProductsByCompound(@PathVariable String word) {
        return productService.getByDescriptionContainingWord(word);
    }

    @GetMapping("/logout")
    private String performLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        String redirectUrl = "http://localhost:8080/realms/candy-shop-realm/protocol/openid-connect/logout";
        response.sendRedirect(redirectUrl);

        return "You are logout ";
    }
}


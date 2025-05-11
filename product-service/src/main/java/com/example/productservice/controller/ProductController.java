package com.example.productservice.controller;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDTO createProduct(@ModelAttribute ProductDTO productDTO) {
    return productService.createProduct(productDTO);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts();
  }
}

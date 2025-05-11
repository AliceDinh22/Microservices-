package com.example.productservice.service;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
  private final CloudinaryService cloudinaryService;

  public ProductDTO createProduct(ProductDTO productDTO) {
    List<String> images = new ArrayList<>();

    if (productDTO.getFiles() != null) {
      for (MultipartFile files : productDTO.getFiles()) {
        try {
          String url = cloudinaryService.uploadFile(files, "microservices");
          if (url == null) {
            throw new RuntimeException("Could not upload!");
          }

          images.add(url);

        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select a photo!");
    }

    Product product = Product.builder()
        .skuCode(productDTO.getSkuCode())
        .name(productDTO.getName())
        .description(productDTO.getDescription())
        .price(productDTO.getPrice())
        .images(images).build();

    productRepository.save(product);
    log.info("Product created successfully!");

    ProductDTO dto = new ProductDTO(product.getId(),
        product.getSkuCode(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getImages(),
        null);

    return dto;
  }

  public List<ProductDTO> getAllProducts() {
    return productRepository.findAll()
        .stream()
        .map(product -> new ProductDTO(product.getId(),
            product.getSkuCode(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getImages(),
            null))
        .toList();
  }
}

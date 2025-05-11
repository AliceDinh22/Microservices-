package com.example.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

  private String id;
  private String skuCode;
  private String name;
  private String description;
  private BigDecimal price;
  private List<String> images;
  @JsonIgnore
  private List<MultipartFile> files;
}

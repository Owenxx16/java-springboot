package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.model.Products;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data // toString = hiển thị thông tin chi tiết của 1 đối tượng
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's id must be > 0")
    private Long products_id;

    @JsonProperty("image_url")
    @Size(min = 5, max = 300 , message = "image must be between 5 and 200 char")
    private String image_url;
}

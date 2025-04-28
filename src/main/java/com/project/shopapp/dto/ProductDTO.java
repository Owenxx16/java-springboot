package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data // toString = hiển thị thông tin chi tiết của 1 đối tượng
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 50000000,message = "Price must be less than or equal to 50,000,000")
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id") // thay vì trong POSTMAN là categoryId thì sẽ thành category_id
    private int categoryId;
    private MultipartFile file;
}

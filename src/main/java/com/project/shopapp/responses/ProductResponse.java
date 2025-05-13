package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

//@Data // toString = hiển thị thông tin chi tiết của 1 đối tượng
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id") // thay vì trong POSTMAN là categoryId thì sẽ thành category_id
    private Long categoryId;
}

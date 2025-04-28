package com.project.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data // toString = hiển thị thông tin chi tiết của 1 đối tượng
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotEmpty(message = "Not empty man")
    private String name;
}

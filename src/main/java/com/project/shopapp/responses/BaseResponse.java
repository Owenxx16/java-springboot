package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor

public class BaseResponse {

    @JsonProperty("created_at")
    private LocalDateTime created_at;

    @JsonProperty("updated_at")
    private LocalDateTime updated_at;

}

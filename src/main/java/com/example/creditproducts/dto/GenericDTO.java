package com.example.creditproducts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenericDTO {
    @Schema(description = "Идентификатор записи", accessMode = Schema.AccessMode.READ_ONLY)
    protected Long id;

    public Long getId(){ return id; }
}

package com.example.restbank.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
public abstract class GenericDTO {
    @Schema(description = "Идентификатор записи", accessMode = Schema.AccessMode.READ_ONLY)
    protected Long id;
}


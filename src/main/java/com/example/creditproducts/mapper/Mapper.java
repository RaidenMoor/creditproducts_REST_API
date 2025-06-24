package com.example.restbank.mapper;

import com.example.restbank.dto.GenericDTO;
import com.example.restbank.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDTO> {

    E toEntity(D dto);

    D toDTO(E entity);

    List<D> toDTOs(List<E> entities);
}
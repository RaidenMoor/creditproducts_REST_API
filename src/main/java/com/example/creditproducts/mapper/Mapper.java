package com.example.creditproducts.mapper;



import com.example.creditproducts.dto.GenericDTO;
import com.example.creditproducts.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDTO> {

    E toEntity(D dto);

    D toDTO(E entity);

    List<D> toDTOs(List<E> entities);
}
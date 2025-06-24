package com.example.restbank.service;

import com.example.restbank.dto.GenericDTO;
import com.example.restbank.mapper.Mapper;
import com.example.restbank.model.GenericModel;
import com.example.restbank.repository.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class GenericService <E extends GenericModel, D extends GenericDTO> {
    protected GenericRepository<E> repository;
    protected Mapper<E, D> mapper;

    public List<D> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    public Page<D> getAll(Pageable pageable) {
        Page<E> page = repository.findAll(pageable);
        List<D> result = mapper.toDTOs(page.getContent());
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    public D getById(Long id) {
        return mapper.toDTO(repository.findById(id).orElse(null));
    }

    public D create(D DTO) {
        if (DTO == null) return null;
        E entity = mapper.toEntity(DTO);
        Long entityId = entity.getId();
        if (entityId != null && repository.existsById(entityId)) {
            return null;
        }
        return mapper.toDTO(repository.save(entity));
    }

    public D update(D DTO) {
        E entity = mapper.toEntity(DTO);
        Long id = entity.getId();
        if (id != null) {
            E existingEntity = repository.findById(id).orElse(null);
            if (existingEntity != null) {
                return mapper.toDTO(repository.save(entity));
            }
        }
        return null;
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}

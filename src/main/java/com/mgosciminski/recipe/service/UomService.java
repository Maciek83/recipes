package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import javassist.NotFoundException;

import java.util.Optional;

public interface UomService {

    Iterable<UnitOfMeasure> findAll();
    Optional<UnitOfMeasure> findByDescription(String description);
    Optional<UnitOfMeasure> findById(Long id);
    UnitOfMeasure findByIdPresentOrException(Long id) throws NotFoundException;
    UnitOfMeasure save(UnitOfMeasure unitOfMeasure);
    void delete(UnitOfMeasure unitOfMeasure);
    void delete(Long id);
    UnitOfMeasure edit(UnitOfMeasure unitOfMeasureDto) throws NotFoundException;


}

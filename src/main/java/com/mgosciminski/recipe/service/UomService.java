package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;

import java.util.Optional;

public interface UomService {

    Iterable<UnitOfMeasure> findAll();
    Optional<UnitOfMeasure> findByUom(String uom);
    Optional<UnitOfMeasure> findById(Long id);
    UnitOfMeasure save(UnitOfMeasure unitOfMeasure);
    UnitOfMeasure save(UnitOfMeasureDto unitOfMeasureDto);
    void delete(UnitOfMeasure unitOfMeasure);
    void delete(Long id);

}

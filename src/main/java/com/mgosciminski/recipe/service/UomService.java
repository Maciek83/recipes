package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;

import java.util.Set;

public interface UomService {

    Iterable<UnitOfMeasure> findAll();
    UnitOfMeasure save(UnitOfMeasureDto unitOfMeasureDto);
    void delete(UnitOfMeasure unitOfMeasure);
    void delete(Long id);

}
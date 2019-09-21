package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import javassist.NotFoundException;

import java.util.Optional;

public interface UomService {

    Iterable<UnitOfMeasureDto> findAll();
    Optional<UnitOfMeasure> findByUom(String uom);
    Optional<UnitOfMeasure> findById(Long id);
    UnitOfMeasureDto findDtoById(Long id) throws NotFoundException;
    UnitOfMeasure save(UnitOfMeasure unitOfMeasure);
    UnitOfMeasure save(UnitOfMeasureDto unitOfMeasureDto);
    void delete(UnitOfMeasure unitOfMeasure);
    void delete(Long id);
    UnitOfMeasure edit(UnitOfMeasureDto unitOfMeasureDto) throws NotFoundException;
    UnitOfMeasureDto convertToDto(UnitOfMeasure unitOfMeasure);

}

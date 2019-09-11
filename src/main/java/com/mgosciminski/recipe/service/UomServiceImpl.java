package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.IngredientDtoToIngredient;
import com.mgosciminski.recipe.converter.UomDToToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.repository.UomRepository;
import org.springframework.stereotype.Service;

@Service
public class UomServiceImpl implements UomService {

    private final UomRepository uomRepository;
    private final UomDToToUnitOfMeasure converter;

    public UomServiceImpl(UomRepository uomRepository,
                          UomDToToUnitOfMeasure converter) {
        this.uomRepository = uomRepository;
        this.converter = converter;
    }

    @Override
    public Iterable<UnitOfMeasure> findAll() {
        return uomRepository.findAll();
    }

    @Override
    public UnitOfMeasure save(UnitOfMeasureDto unitOfMeasureDto) {

        return uomRepository.save(converter.convert(unitOfMeasureDto));
    }

    @Override
    public void delete(UnitOfMeasure unitOfMeasure) {
        uomRepository.delete(unitOfMeasure);
    }

    @Override
    public void delete(Long id) {
        uomRepository.deleteById(id);
    }

}

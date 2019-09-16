package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.UomDtoToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.repository.UomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UomServiceImpl implements UomService {

    private final UomRepository uomRepository;
    private final UomDtoToUnitOfMeasure converter;

    public UomServiceImpl(UomRepository uomRepository,
                          UomDtoToUnitOfMeasure converter) {
        this.uomRepository = uomRepository;
        this.converter = converter;
    }

    @Override
    public Iterable<UnitOfMeasure> findAll() {
        return uomRepository.findAll();
    }

    @Override
    public Optional<UnitOfMeasure> findByUom(String uom) {
        return uomRepository.findByUom(uom);
    }

    @Override
    public UnitOfMeasure save(UnitOfMeasure unitOfMeasure) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findByUom(unitOfMeasure.getUom());

        return optionalUnitOfMeasure.orElseGet(() -> uomRepository.save(unitOfMeasure));

    }

    @Override
    public UnitOfMeasure save(UnitOfMeasureDto unitOfMeasureDto) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findByUom(unitOfMeasureDto.getUom());

        return optionalUnitOfMeasure.orElse(uomRepository.save(converter.convert(unitOfMeasureDto)));
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

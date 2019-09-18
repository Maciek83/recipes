package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.UnitOfMeasureToUomDto;
import com.mgosciminski.recipe.converter.UomDtoToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.repository.UomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UomServiceImpl implements UomService {

    private final UomRepository uomRepository;
    private final UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;
    private final UnitOfMeasureToUomDto unitOfMeasureToUomDto;

    private UnitOfMeasure nullObject;

    public UomServiceImpl(UomRepository uomRepository,
                          UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure,
                          UnitOfMeasureToUomDto unitOfMeasureToUomDto) {
        this.uomRepository = uomRepository;
        this.uomDtoToUnitOfMeasure = uomDtoToUnitOfMeasure;
        this.unitOfMeasureToUomDto = unitOfMeasureToUomDto;
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
    public Optional<UnitOfMeasure> findById(Long id) {
        return uomRepository.findById(id);
    }

    @Override
    public UnitOfMeasure save(UnitOfMeasure unitOfMeasure) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findByUom(unitOfMeasure.getUom());

        return optionalUnitOfMeasure.orElseGet(() -> uomRepository.save(unitOfMeasure));
    }

    @Override
    public UnitOfMeasure save(UnitOfMeasureDto unitOfMeasureDto) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findByUom(unitOfMeasureDto.getUom());

        return optionalUnitOfMeasure.orElseGet(() -> uomRepository.save(uomDtoToUnitOfMeasure.convert(unitOfMeasureDto)));
    }

    @Override
    public void delete(UnitOfMeasure unitOfMeasure) {
        uomRepository.delete(unitOfMeasure);
    }

    @Override
    public void delete(Long id) {
        uomRepository.deleteById(id);
    }

    @Override
    public UnitOfMeasure edit(UnitOfMeasureDto unitOfMeasureDto) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findById(unitOfMeasureDto.getId());

        if(optionalUnitOfMeasure.isPresent())
        {
            UnitOfMeasure unitOfMeasure = optionalUnitOfMeasure.get();
            unitOfMeasure.setUom(unitOfMeasure.getUom());
            return save(unitOfMeasure);
        }
        else
        {
            nullObject = new UnitOfMeasure();
            nullObject.setUom("bad");
            nullObject.setId(-1L);

            return nullObject;
        }
    }

    @Override
    public UnitOfMeasureDto convertToDto(UnitOfMeasure unitOfMeasure) {
        return unitOfMeasureToUomDto.convert(unitOfMeasure);
    }

}

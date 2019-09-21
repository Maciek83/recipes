package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.UnitOfMeasureToUomDto;
import com.mgosciminski.recipe.converter.UomDtoToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.repository.UomRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UomServiceImpl implements UomService {

    private final UomRepository uomRepository;
    private final UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;
    private final UnitOfMeasureToUomDto unitOfMeasureToUomDto;

    private final String NOT_FOUND = "can't find this id";

    public UomServiceImpl(UomRepository uomRepository,
                          UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure,
                          UnitOfMeasureToUomDto unitOfMeasureToUomDto) {
        this.uomRepository = uomRepository;
        this.uomDtoToUnitOfMeasure = uomDtoToUnitOfMeasure;
        this.unitOfMeasureToUomDto = unitOfMeasureToUomDto;
    }

    @Override
    public Iterable<UnitOfMeasureDto> findAll() {
        List<UnitOfMeasureDto> unitOfMeasureDtos = new LinkedList<>();
        uomRepository.findAll().forEach(unitOfMeasure -> unitOfMeasureDtos.add(convertToDto(unitOfMeasure)));
        return unitOfMeasureDtos;
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
    public UnitOfMeasure findByIdPresentOrException(Long id) throws NotFoundException {
        return findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND));
    }

    @Override
    public UnitOfMeasureDto findDtoById(Long id) throws NotFoundException {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findById(id);

        return optionalUnitOfMeasure
                .map(this::convertToDto)
                .orElseThrow(()->new NotFoundException(NOT_FOUND));
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
    public UnitOfMeasure edit(UnitOfMeasureDto unitOfMeasureDto) throws NotFoundException {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findById(unitOfMeasureDto.getId());

        return optionalUnitOfMeasure
                .map(unitOfMeasure -> {
                    unitOfMeasure.setUom(unitOfMeasureDto.getUom());
                    return save(unitOfMeasure); })
                .orElseThrow(()->new NotFoundException(NOT_FOUND));
    }

    @Override
    public UnitOfMeasureDto convertToDto(UnitOfMeasure unitOfMeasure) {
        return unitOfMeasureToUomDto.convert(unitOfMeasure);
    }

}

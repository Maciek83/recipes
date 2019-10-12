package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.repository.UomRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UomServiceImpl implements UomService {

    private final UomRepository uomRepository;

    private final String NOT_FOUND = "can't find this id";

    public UomServiceImpl(UomRepository uomRepository) {
        this.uomRepository = uomRepository;
    }

    @Override
    public Iterable<UnitOfMeasure> findAll() {
        return uomRepository.findAll();
    }

    @Override
    public Optional<UnitOfMeasure> findByDescription(String description) {
        return uomRepository.findByDescription(description);
    }

    @Override
    public Optional<UnitOfMeasure> findById(Long id) {
        return uomRepository.findById(id);
    }

    @Override
    public UnitOfMeasure findByIdPresentOrException(Long id) throws NotFoundException {
        return findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public UnitOfMeasure save(UnitOfMeasure unitOfMeasure) {

        Optional<UnitOfMeasure> optionalUnitOfMeasure = findByDescription(unitOfMeasure.getDescription());

        return optionalUnitOfMeasure.orElseGet(() -> uomRepository.save(unitOfMeasure));
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
    public UnitOfMeasure edit(UnitOfMeasure unitOfMeasure) throws NotFoundException {

        return save(unitOfMeasure);
    }

}

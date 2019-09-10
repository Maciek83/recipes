package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.IngredientDtoToIngredient;
import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.repository.IngredientRepository;

import java.util.Optional;

public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repository;
    private final IngredientDtoToIngredient converter;

    private final Ingredient nullObject = new Ingredient();
    private final String BAD = "bad";


    public IngredientServiceImpl(IngredientRepository repository, IngredientDtoToIngredient converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return repository.findAll();
    }

    @Override
    public Ingredient save(IngredientDto ingredientDto) {

        return repository.save(converter.convert(ingredientDto));
    }

    @Override
    public Ingredient findById(Long id) {

        Optional<Ingredient> optionalIngredient = repository.findById(id);

        nullObject.setDescription(BAD);

        return optionalIngredient.orElse(nullObject);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Ingredient ingredient) {
        repository.delete(ingredient);
    }
}

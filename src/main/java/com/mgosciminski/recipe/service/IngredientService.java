package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;

import java.util.Optional;

public interface IngredientService {

    Iterable<Ingredient> findAll();
    Ingredient save(IngredientDto ingredientDto);
    Ingredient findById(Long id);
    void deleteById(Long id);
    void delete(Ingredient ingredient);

}

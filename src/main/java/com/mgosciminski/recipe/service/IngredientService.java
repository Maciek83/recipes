package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;



public interface IngredientService {

    Iterable<Ingredient> findAll();
    Ingredient save(IngredientDto ingredientDto);
    Ingredient save(Ingredient ingredient);
    Ingredient findById(Long id);
    void deleteById(Long id);
    void delete(Ingredient ingredient);

}

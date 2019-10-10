package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Ingredient;


public interface IngredientService {

    Iterable<Ingredient> findAll();
    Ingredient save(Ingredient ingredient);
    Ingredient findById(Long id);
    void deleteById(Long id);
    void delete(Ingredient ingredient);

}

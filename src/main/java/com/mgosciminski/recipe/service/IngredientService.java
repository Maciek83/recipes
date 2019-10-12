package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Ingredient;
import javassist.NotFoundException;


public interface IngredientService {

    Iterable<Ingredient> findAll();
    Ingredient save(Ingredient ingredient);
    Ingredient findById(Long id) throws NotFoundException;
    void deleteById(Long id);
    void delete(Ingredient ingredient);

}

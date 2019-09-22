package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.RecipeDto;
import javassist.NotFoundException;

import java.util.Optional;

public interface RecipeService {

    Recipe save(RecipeDto recipeDto);
    Recipe save(Recipe recipe);
    Iterable<Recipe> findAll();
    Iterable<RecipeDto> findAllDto();
    Recipe findById(Long id) throws NotFoundException;
    RecipeDto findDtoById(Long id) throws NotFoundException;
    void delete(Recipe recipe);
    void deleteById(Long id);
    Recipe edit(RecipeDto recipeDto) throws NotFoundException;
    RecipeDto convertRecipeToRecipeDto(Recipe recipe);

}

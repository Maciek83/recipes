package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.*;
import com.mgosciminski.recipe.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeService recipeService;

    public DataLoader(RecipeService recipeService) {

        this.recipeService = recipeService;
    }

    @Override
    public void run(String... args) throws Exception {

        Set<IngredientDto> ingredientDtos = new HashSet<>();
        ingredientDtos.add(new IngredientDto());
        Set<CategoryDto> categoryDtos = new HashSet<>();
        categoryDtos.add(new CategoryDto());
        categoryDtos.add(new CategoryDto());

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setDifficulty("EASY");
        NoteDto noteDto = new NoteDto();
        recipeDto.setNoteDto(noteDto);
        recipeDto.setIngredientDtos(ingredientDtos);
        recipeDto.setCategoryDtos(categoryDtos);

        recipeService.save(recipeDto);

        System.out.println("data loaded");

    }
}

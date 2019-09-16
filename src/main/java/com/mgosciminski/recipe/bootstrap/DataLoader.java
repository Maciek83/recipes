package com.mgosciminski.recipe.bootstrap;

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

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setUom("kilos");


        Set<IngredientDto> ingredientDtos = new HashSet<>();
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("aa");
        ingredientDto.setUnitOfMeasureDto(unitOfMeasureDto);
        ingredientDtos.add(ingredientDto);
        Set<CategoryDto> categoryDtos = new HashSet<>();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("name");
        categoryDtos.add(categoryDto);


        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setDifficulty("EASY");
        NoteDto noteDto = new NoteDto();
        recipeDto.setNoteDto(noteDto);
        recipeDto.setIngredientDtos(ingredientDtos);
        recipeDto.setCategoryDtos(categoryDtos);

        recipeService.save(recipeDto);

        UnitOfMeasureDto unitOfMeasureDto1 = new UnitOfMeasureDto();
        unitOfMeasureDto1.setUom("kilos");


        Set<IngredientDto> ingredientDtos1 = new HashSet<>();
        IngredientDto ingredientDto3 = new IngredientDto();
        ingredientDto3.setDescription("aadfdfb");
        ingredientDto3.setUnitOfMeasureDto(unitOfMeasureDto1);
        ingredientDtos1.add(ingredientDto3);
        Set<CategoryDto> categoryDtos1 = new HashSet<>();
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setName("name");
        categoryDtos1.add(categoryDto1);

        RecipeDto recipeDto1 = new RecipeDto();
        recipeDto1.setDifficulty("EASY");
        NoteDto noteDto1 = new NoteDto();
        recipeDto1.setNoteDto(noteDto1);
        recipeDto1.setIngredientDtos(ingredientDtos1);
        recipeDto1.setCategoryDtos(categoryDtos1);

        recipeService.save(recipeDto1);


        System.out.println("data loaded");

    }
}

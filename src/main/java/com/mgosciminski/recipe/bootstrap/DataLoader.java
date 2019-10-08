package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.*;
import com.mgosciminski.recipe.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeService recipeService;


    public DataLoader(RecipeService recipeService) {

        this.recipeService = recipeService;

    }

    @Override
    public void run(String... args) throws Exception {

        Recipe recipe = new Recipe();
        recipe.setDifficulty(Difficulty.HARD);
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom("uom");
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("cosik");
        ingredient.setUnitOfMeasure(unitOfMeasure);
        recipe.getIngredients().add(ingredient);
        Note note = new Note();
        note.setNotes("notes");
        recipe.setNotes(note);
        Category category = new Category();
        category.setName("kategory");
        recipe.getCategories().add(category);


        recipeService.save(recipe);


        System.out.println("data loaded");

    }
}

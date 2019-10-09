package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeService recipeService;
    private final UomService uomService;


    public DataLoader(RecipeService recipeService, UomService uomService) {

        this.recipeService = recipeService;

        this.uomService = uomService;
    }

    @Override
    public void run(String... args) throws Exception {

        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setUom("blelbuszka");
        uomService.save(unitOfMeasure1);

        Recipe recipe = new Recipe();
        recipe.setDescription("bobobobo");
        recipe.setPrepTime(3);
        recipe.setCookTime(100);
        recipe.setServings(12);
        recipe.setSource("dddd");
        recipe.setUrl("fdss");
        recipe.setDirections("dssdfs");
        recipe.setDifficulty(Difficulty.HARD);
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom("uom");
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("cosik");
        ingredient.setRecipe(recipe);
        ingredient.setUnitOfMeasure(unitOfMeasure);
        recipe.getIngredients().add(ingredient);
        Note note = new Note();
        note.setNotes("notes");
        recipe.setNotes(note);
        note.setRecipe(recipe);
        Category category = new Category();
        category.setName("kategory");
        recipe.getCategories().add(category);

        recipeService.save(recipe);

        System.out.println("data loaded");

    }
}

package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.service.IngredientService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}/delete")
    public String deleteIngredient(@PathVariable String id) throws NotFoundException {
        Ingredient ingredient = ingredientService.findById(Long.valueOf(id));
        Long recipeId = ingredient.getRecipe().getId();
        ingredientService.delete(ingredient);

        return "redirect:/recipe/"+ recipeId +"/ingredients";
    }

}

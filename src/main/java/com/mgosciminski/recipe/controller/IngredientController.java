package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.service.IngredientService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;
    private final String Error404 = "404error";
    private final String NOT_FOUND = "can't find this id";

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}/delete")
    public String deleteIngredient(@PathVariable String id) throws NotFoundException {

        try {
            Long.valueOf(id);
        }
        catch (NumberFormatException e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        Ingredient ingredient = ingredientService.findById(Long.valueOf(id));
        Long recipeId = ingredient.getRecipe().getId();
        ingredientService.delete(ingredient);

        return "redirect:/recipe/"+ recipeId +"/ingredients";
    }

    @ExceptionHandler({NotFoundException.class,NumberFormatException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }

}

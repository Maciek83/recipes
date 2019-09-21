package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/recipe")
public class RecipeController
{
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public String showRecipes(Model model)
    {
        model.addAttribute("recipes", recipeService.findAllDto());

        return "recipe/index";
    }


}

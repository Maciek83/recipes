package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.service.RecipeService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/recipe")
public class RecipeController
{
    private final RecipeService recipeService;
    private final String NOT_FOUND = "can't find this id";
    private final String Error404 = "404error";
    private final String REC_FORM = "recipe/new/index";

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public String showRecipes(Model model)
    {
        model.addAttribute("recipes", recipeService.findAllDto());

        return "recipe/index";
    }

    @GetMapping("/new")
    public String gotoFormAddNew(Model model)
    {
        model.addAttribute("recipe", new RecipeDto());

        return REC_FORM;
    }

    @PostMapping("/new")
    public String addNewRecipe(@Valid @ModelAttribute RecipeDto recipeDto, BindingResult bindingResult) throws Exception
    {
        if (bindingResult.hasErrors())
        {
            return REC_FORM;
        }
        else
        {
            recipeService.save(recipeDto);

            return "redirect:/recipe";
        }
    }

    @GetMapping
    @RequestMapping("/{id}/show")
    public String showRecipeById(@PathVariable String id, Model model) throws NotFoundException {

        try { Long.valueOf(id); }
        catch (NumberFormatException e) { throw new NotFoundException(NOT_FOUND); }

        model.addAttribute("recipe",recipeService.findDtoById(Long.valueOf(id)));

        return "recipe/show/index";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }


}

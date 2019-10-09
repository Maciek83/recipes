package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.model.RecipeDtoDisplay;
import com.mgosciminski.recipe.service.IngredientService;
import com.mgosciminski.recipe.service.RecipeService;
import com.mgosciminski.recipe.service.UomService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/recipe")
public class RecipeController
{
    private final RecipeService recipeService;
    private final UomService uomService;
    private final IngredientService ingredientService;
    private final String NOT_FOUND = "can't find this id";
    private final String Error404 = "404error";
    private final String REC_FORM = "recipe/new/index";

    public RecipeController(RecipeService recipeService, UomService uomService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.uomService = uomService;
        this.ingredientService = ingredientService;
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

    @GetMapping("/{id}/ingredients")
    public String showIngredients(@PathVariable String id, Model model) throws NotFoundException
    {
        try { Long.valueOf(id); }
        catch (NumberFormatException e) { throw new NotFoundException(NOT_FOUND); }

        List<IngredientDto> ingredientDtos = recipeService.findDtoById(Long.valueOf(id)).getIngredients();

        model.addAttribute("ingredients", ingredientDtos);

        return "recipe/editIngredients/index";
    }

    @GetMapping("/{id}/ingredient/add")
    public String goToFormAddIngredient(@PathVariable String id, Model model)
    {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setRecipeId(Long.valueOf(id));
        model.addAttribute("ingredient",ingredientDto);
        model.addAttribute("uoms",uomService.findAllDto());

        return "ingredient/form/index";
    }

    @PostMapping("/{id}/ingredient/add")
    public String addIngredient(Model model,@PathVariable String id, @Valid @ModelAttribute("ingredient") IngredientDto ingredientDto, BindingResult bindingResult) throws NotFoundException {

        if(bindingResult.hasErrors())
        {
            model.addAttribute("uoms",uomService.findAllDto());
            return "ingredient/form/index";
        }

        Recipe recipe = recipeService.findById(Long.valueOf(id));
        Ingredient ingredientSaved = ingredientService.save(ingredientDto);
        ingredientSaved.setRecipe(recipe);
        recipe.getIngredients().add(ingredientSaved);
        recipeService.save(recipe);

        return "redirect:/recipe/"+id;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String showRecipeById(@PathVariable String id, Model model) throws NotFoundException {

        try { Long.valueOf(id); }
        catch (NumberFormatException e) { throw new NotFoundException(NOT_FOUND); }

        RecipeDtoDisplay recipeDtoDisplay = recipeService.findDtoById(Long.valueOf(id));

        model.addAttribute("recipe",recipeDtoDisplay);

        return "recipe/show/index";
    }

    @ExceptionHandler({NotFoundException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }


}

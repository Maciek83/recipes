package com.mgosciminski.recipe.controller;


import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.service.CategoryService;
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
import java.util.Set;


@Controller
@RequestMapping("/recipe")
public class RecipeController
{
    private final RecipeService recipeService;
    private final UomService uomService;
    private final IngredientService ingredientService;
    private final CategoryService categoryService;
    private final String NOT_FOUND = "can't find this id";
    private final String Error404 = "404error";
    private final String REC_FORM = "recipe/new/index";

    public RecipeController(RecipeService recipeService, UomService uomService, IngredientService ingredientService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.uomService = uomService;
        this.ingredientService = ingredientService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showRecipes(Model model)
    {
        model.addAttribute("recipes", recipeService.findAll());

        return "recipe/index";
    }

    @GetMapping("/new")
    public String gotoFormAddNew(Model model)
    {
        model.addAttribute("recipe", new Recipe());

        return REC_FORM;
    }

    @PostMapping("/new")
    public String addNewRecipe(@Valid @ModelAttribute Recipe recipeDto, BindingResult bindingResult) throws Exception
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

        Set<Ingredient> ingredients = recipeService.findById(Long.valueOf(id)).getIngredients();

        model.addAttribute("ingredients", ingredients);

        return "recipe/editIngredients/index";
    }

    @GetMapping("/{id}/ingredient/add")
    public String goToFormAddIngredient(@PathVariable String id, Model model) throws NotFoundException
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setRecipe(recipeService.findById(Long.valueOf(id)));
        model.addAttribute("ingredient",ingredient);
        model.addAttribute("uoms",uomService.findAll());

        return "ingredient/form/index";
    }

    @PostMapping("/{id}/ingredient/add")
    public String addIngredient(Model model,@PathVariable String id, @Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult) throws NotFoundException {

        if(bindingResult.hasErrors())
        {
            model.addAttribute("uoms",uomService.findAll());
            return "ingredient/form/index";
        }

        Recipe recipe = recipeService.findById(Long.valueOf(id));
        Ingredient ingredientSaved = ingredientService.save(ingredient);
        ingredientSaved.setRecipe(recipe);
        recipe.getIngredients().add(ingredientSaved);
        recipeService.save(recipe);

        return "redirect:/recipe/"+id;
    }

    @GetMapping("/{id}/categories")
    public String editCategoriesForm(Model model,@PathVariable String id) throws NotFoundException {

        model.addAttribute("recipe",recipeService.findById(Long.valueOf(id)));
        model.addAttribute("recipeCat",recipeService.findById(Long.valueOf(id)).getCategories());
        model.addAttribute("allcategories",categoryService.findAll());

        return "recipe/editCategories/index";
    }

    @PostMapping("/{id}/categories")
    public String editCategories(@PathVariable String id, @ModelAttribute Recipe recipe ) {

        recipeService.save(recipe);

        return "redirect:/recipe/"+id;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String showRecipeById(@PathVariable String id, Model model) throws NotFoundException {

        try { Long.valueOf(id); }
        catch (NumberFormatException e) { throw new NotFoundException(NOT_FOUND); }

        Recipe recipeDtoDisplay = recipeService.findById(Long.valueOf(id));

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

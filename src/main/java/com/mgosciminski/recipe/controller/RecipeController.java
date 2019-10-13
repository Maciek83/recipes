package com.mgosciminski.recipe.controller;


import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.service.*;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final UomService uomService;
    private final IngredientService ingredientService;
    private final CategoryService categoryService;
    private final NoteService noteService;
    private final String NOT_FOUND = "can't find this id";
    private final String Error404 = "404error";
    private final String REC_FORM = "recipe/new/index";

    public RecipeController(RecipeService recipeService, UomService uomService, IngredientService ingredientService, CategoryService categoryService, NoteService noteService) {
        this.recipeService = recipeService;
        this.uomService = uomService;
        this.ingredientService = ingredientService;
        this.categoryService = categoryService;
        this.noteService = noteService;
    }

    @GetMapping
    public String showRecipes(Model model) {

        model.addAttribute("recipes", recipeService.findAll());

        return "recipe/index";
    }

    @GetMapping("/add")
    public String gotoFormAddNew(Model model) {

        model.addAttribute("diff", Difficulty.values());
        model.addAttribute("recipe", new Recipe());
        return REC_FORM;
    }

    @PostMapping("/add")
    public String addNewRecipe(@Valid @ModelAttribute Recipe recipe, BindingResult bindingResult, Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("diff", Difficulty.values());
            return REC_FORM;
        }
        else {
            recipeService.save(recipe);

            return "redirect:/recipe";
        }
    }

    @GetMapping("/{id}/ingredients")
    public String showIngredients(@PathVariable String id, Model model) throws NotFoundException {

        try {
            Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new NotFoundException(NOT_FOUND);
        }

        Recipe recipe = recipeService.findById(Long.valueOf(id));
        Set<Ingredient> ingredients = recipe.getIngredients();

        List<Ingredient> ingredientList = List.copyOf(ingredients);

        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredients", ingredientList);

        return "recipe/editIngredients/index";
    }

    @GetMapping("/{id}/ingredient/add")
    public String goToFormAddIngredient(@PathVariable String id, Model model) throws Exception {

        Recipe recipe = null;

        try {

            recipe = recipeService.findById(Long.valueOf(id));
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        Ingredient ingredient = new Ingredient();
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("uoms", uomService.findAll());

        return "ingredient/form/index";
    }

    @PostMapping("/{id}/ingredient/add")
    public String addIngredient(Model model, @PathVariable String id, @Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult) throws NotFoundException {

        try{
            Long.valueOf(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        if (bindingResult.hasErrors()) {

            model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
            model.addAttribute("uoms", uomService.findAll());

            return "ingredient/form/index";
        }

        Ingredient newIngredient = new Ingredient();
        newIngredient.setDescription(ingredient.getDescription());
        newIngredient.setUnitOfMeasure(ingredient.getUnitOfMeasure());

        Recipe recipe = recipeService.findById(Long.valueOf(id));
        newIngredient.setRecipe(recipe);

        Ingredient ingredientSaved = ingredientService.save(newIngredient);
        recipe.getIngredients().add(ingredientSaved);
        recipeService.save(recipe);

        return "redirect:/recipe/" + id;
    }

    @GetMapping("/{id}/categories")
    public String editCategoriesForm(Model model, @PathVariable String id) throws NotFoundException {

        Recipe recipe = null;

        try{
            recipe = recipeService.findById(Long.valueOf(id));
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }


        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeCat", recipe.getCategories());
        model.addAttribute("allcategories", categoryService.findAll());

        return "recipe/editCategories/index";
    }

    @PostMapping("/{id}/categories")
    public String editCategories(@PathVariable String id, @ModelAttribute Recipe recipe) throws Exception {

        Recipe recipeFromDb = recipeService.findById(Long.valueOf(id));

        recipeFromDb.setCategories(recipe.getCategories());
        recipeService.save(recipeFromDb);

        return "redirect:/recipe/" + id;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String showRecipeById(@PathVariable String id, Model model) throws NotFoundException {

        try {
            Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new NotFoundException(NOT_FOUND);
        }

        Recipe recipe = recipeService.findById(Long.valueOf(id));

        model.addAttribute("recipe", recipe);

        return "recipe/show/index";
    }

    @GetMapping("/{id}/note")
    public String gotoEditNoteForm(@PathVariable String id, Model model) throws NotFoundException {

        try {
            Long.valueOf(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        Recipe recipe = recipeService.findById(Long.valueOf(id));

        if (recipe.getNotes() == null) {
            Note note = new Note();
            note.setDescription("Add description");
            note.setRecipe(recipe);
            recipe.setNotes(note);
            model.addAttribute("recipe",recipe);
            model.addAttribute("note", recipe.getNotes());
        } else {
            model.addAttribute("recipe",recipe);
            model.addAttribute("note", recipe.getNotes());
        }

        return "recipe/editNote/index";
    }

    @PostMapping("/{id}/note")
    public String addNotePost(@PathVariable String id, @Valid @ModelAttribute("note") Note note, BindingResult bindingResult, Model model) throws NotFoundException {

        try{
            Long.valueOf(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        Recipe recipeFromDb = recipeService.findById(Long.valueOf(id));

        if(bindingResult.hasErrors())
        {
            model.addAttribute("recipe",recipeFromDb);
            return "recipe/editNote/index";
        }

        note.setRecipe(recipeFromDb);
        Note noteFromDb = noteService.save(note);

        recipeFromDb.setNotes(noteFromDb);

        recipeService.save(recipeFromDb);

        return "redirect:/recipe/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editRecipeGoToForm(@PathVariable String id,Model model) throws NotFoundException {

        try
        {
            Long.valueOf(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        model.addAttribute("recipe",recipeService.findById(Long.valueOf(id)));
        model.addAttribute("diff", Difficulty.values());

        return "recipe/edit/index";
    }

    @PostMapping("/{id}/edit")
    public String editRecipeSubmit(@PathVariable String id, @Valid @ModelAttribute Recipe recipe,BindingResult bindingResult, Model model) throws NotFoundException {

        try
        {
            Long.valueOf(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }


        if (bindingResult.hasErrors())
        {
            model.addAttribute("diff", Difficulty.values());
            return "recipe/edit/index";
        }

        recipeService.edit(recipe);

        return "redirect:/recipe/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id) throws NotFoundException {
        try
        {
            Long.valueOf(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException(NOT_FOUND);
        }

        recipeService.deleteById(Long.valueOf(id));

        return "redirect:/recipe";
    }

    @ExceptionHandler({NotFoundException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }


}

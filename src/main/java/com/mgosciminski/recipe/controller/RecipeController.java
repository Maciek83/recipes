package com.mgosciminski.recipe.controller;


import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
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

    @GetMapping("/new")
    public String gotoFormAddNew(Model model) {
        model.addAttribute("recipe", new Recipe());

        return REC_FORM;
    }

    @PostMapping("/new")
    public String addNewRecipe(@Valid @ModelAttribute Recipe recipeDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return REC_FORM;
        } else {
            recipeService.save(recipeDto);

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

        Set<Ingredient> ingredients = recipeService.findById(Long.valueOf(id)).getIngredients();

        List<Ingredient> ingredientList = List.copyOf(ingredients);

        model.addAttribute("ingredients", ingredientList);

        return "recipe/editIngredients/index";
    }

    @GetMapping("/{id}/ingredient/add")
    public String goToFormAddIngredient(@PathVariable String id, Model model) throws NotFoundException {
        Ingredient ingredient = new Ingredient();
        ingredient.setUnitOfMeasure(new UnitOfMeasure());
        ingredient.setRecipe(recipeService.findById(Long.valueOf(id)));
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("uoms", uomService.findAll());

        return "ingredient/form/index";
    }

    @PostMapping("/{id}/ingredient/add")
    public String addIngredient(Model model, @PathVariable String id, @Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult) throws NotFoundException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
            model.addAttribute("uoms", uomService.findAll());
            System.out.println("oop");
            return "ingredient/form/index";
        }

        Recipe recipe = recipeService.findById(Long.valueOf(id));
        Ingredient ingredientSaved = ingredientService.save(ingredient);
        ingredientSaved.setRecipe(recipe);
        recipe.getIngredients().add(ingredientSaved);
        recipeService.save(recipe);

        return "redirect:/recipe/" + id;
    }

    @GetMapping("/{id}/categories")
    public String editCategoriesForm(Model model, @PathVariable String id) throws NotFoundException {

        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        model.addAttribute("recipeCat", recipeService.findById(Long.valueOf(id)).getCategories());
        model.addAttribute("allcategories", categoryService.findAll());

        return "recipe/editCategories/index";
    }

    @PostMapping("/{id}/categories")
    public String editCategories(@PathVariable String id, @ModelAttribute Recipe recipe) {

        recipeService.save(recipe);

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

        Recipe recipeDtoDisplay = recipeService.findById(Long.valueOf(id));

        model.addAttribute("recipe", recipeDtoDisplay);

        return "recipe/show/index";
    }

    @GetMapping("/{id}/note")
    public String gotoEditOrAddNoteForm(@PathVariable String id, Model model) throws NotFoundException {

        Recipe recipe = recipeService.findById(Long.valueOf(id));

        if (recipe.getNotes() == null) {
            Note note = new Note();
            note.setDescription("Add description");
            note.setRecipe(recipe);
            recipe.setNotes(note);
            model.addAttribute("recipe", recipe);
        } else {

            model.addAttribute("recipe", recipe);
        }

        return "recipe/editNote/index";
    }

    @PostMapping("/{id}/note")
    public String addNotePost(@PathVariable String id, @Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult) throws NotFoundException {

        if(bindingResult.hasErrors())
        {
            return "recipe/editNote/index";
        }

        Recipe recipeFromDb = recipeService.findById(Long.valueOf(id));

        Note note = recipe.getNotes();
        note.setRecipe(recipeFromDb);
        Note noteFromDb = noteService.save(note);

        recipeFromDb.setNotes(noteFromDb);

        recipeService.save(recipeFromDb);

        return "redirect:/recipe/" + id;
    }

    @ExceptionHandler({NotFoundException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }


}

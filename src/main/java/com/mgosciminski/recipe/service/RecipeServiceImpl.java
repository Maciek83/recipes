package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.RecipeDtoToRecipe;
import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final RecipeDtoToRecipe recipeDtoToRecipe;
    private final NoteService noteService;
    private final IngredientService ingredientService;
    private final CategoryService categoryService;

    private Recipe nullObject = new Recipe();
    private final Long BAD = -1L;

    public RecipeServiceImpl(RecipeRepository repository, RecipeDtoToRecipe recipeDtoToRecipe, NoteService noteService, IngredientService ingredientService, CategoryService categoryService) {
        this.repository = repository;
        this.recipeDtoToRecipe = recipeDtoToRecipe;
        this.noteService = noteService;
        this.ingredientService = ingredientService;
        this.categoryService = categoryService;
    }

    @Override
    public Recipe save(RecipeDto recipeDto) {

        Recipe recipe = recipeDtoToRecipe.convert(recipeDto);

        noteService.save(recipe.getNotes());

        Set<Ingredient> ingredients = recipe.getIngredients();
        ingredients.forEach(ingredientService::save);

        Set<Category> categories = recipe.getCategories();
        categories.forEach(categoryService::save);

        return repository.save(recipe);
    }

    @Override
    public Recipe save(Recipe recipe) {
        return repository.save(recipe);
    }

    @Override
    public Iterable<Recipe> findAll() {
        return repository.findAll();
    }

    @Override
    public Recipe findById(Long id) {

        Optional<Recipe> optionalRecipe = repository.findById(id);

        if (optionalRecipe.isPresent())
        {
            return optionalRecipe.get();
        }
        else
        {
            nullObject.setId(BAD);
            return nullObject;
        }
    }

    @Override
    public void delete(Recipe recipe) {

        repository.delete(recipe);
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }

    @Override
    public Recipe edit(RecipeDto recipeDto) {

        Recipe recipe = findById(recipeDto.getId());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPrepTime(recipeDto.getPrepTime());
        recipe.setCookTime(recipeDto.getCookTime());
        recipe.setServings(recipeDto.getServings());
        recipe.setSource(recipeDto.getSource());
        recipe.setUrl(recipeDto.getUrl());
        recipe.setDirections(recipeDto.getDirections());
        recipe.setDifficulty(Difficulty.valueOf(recipeDto.getDifficulty()));

        Note note = noteService.findById(recipeDto.getNoteDto().getId());
        note.setNotes(recipeDto.getNoteDto().getNotes());
        noteService.save(note);

        Set<IngredientDto> ingredientDtos = recipeDto.getIngredientDtos();
        ingredientDtos.forEach(ingredientDto -> {
            Ingredient ingredient = ingredientService.findById(ingredientDto.getId());
            ingredient.setDescription(ingredientDto.getDescription());
            ingredient.setAmount(ingredientDto.getAmount());
            ingredientService.save(ingredient);
        });

        Set<CategoryDto> categoryDtos = recipeDto.getCategoryDtos();
        categoryDtos.forEach(categoryDto -> {
            Category category = categoryService.findById(categoryDto.getId());
            category.setName(categoryDto.getName());
            categoryService.save(category);
        });

        return save(recipe);
    }
}

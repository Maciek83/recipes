package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.RecipeDtoToRecipe;
import com.mgosciminski.recipe.converter.RecipeToRecipeDto;
import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final RecipeDtoToRecipe recipeDtoToRecipe;
    private final NoteService noteService;
    private final IngredientService ingredientService;
    private final CategoryService categoryService;
    private final UomService uomService;
    private final RecipeToRecipeDto recipeToRecipeDto;

    private Recipe nullObject = new Recipe();
    private final Long BAD = -1L;

    public RecipeServiceImpl(RecipeRepository repository,
                             RecipeDtoToRecipe recipeDtoToRecipe,
                             NoteService noteService,
                             IngredientService ingredientService,
                             CategoryService categoryService,
                             UomService uomService,
                             RecipeToRecipeDto recipeToRecipeDto) {
        this.repository = repository;
        this.recipeDtoToRecipe = recipeDtoToRecipe;
        this.noteService = noteService;
        this.ingredientService = ingredientService;
        this.categoryService = categoryService;
        this.uomService = uomService;
        this.recipeToRecipeDto = recipeToRecipeDto;
    }

    @Override
    public Recipe save(RecipeDto recipeDto) {

        Recipe recipe = recipeDtoToRecipe.convert(recipeDto);

        Note note = recipe.getNotes();
        recipe.setNotes(null);
        Note savedNote = noteService.save(note);

        Set<Ingredient> ingredients = recipe.getIngredients();
        recipe.setIngredients(null);
        Set<Ingredient> ingredientForRecipe = new HashSet<>();

        ingredients.forEach(ingredient -> {
            UnitOfMeasure unitOfMeasure = ingredient.getUnitOfMeasure();
            Set<Ingredient> ingredientSaved = unitOfMeasure.getIngredients();
            unitOfMeasure.setIngredients(null);
            ingredient.setUnitOfMeasure(null);
            ingredient.setRecipe(null);
            UnitOfMeasure savedUom = uomService.save(unitOfMeasure);
            Ingredient savedIngredient = ingredientService.save(ingredient);
            savedIngredient.setUnitOfMeasure(savedUom);
            savedUom.setIngredients(ingredientSaved);
            ingredientSaved.add(savedIngredient);
            unitOfMeasure.setIngredients(ingredientSaved);
            ingredientForRecipe.add(savedIngredient);
        });

        Recipe savedRecipe = save(recipe);
        savedRecipe.setNotes(savedNote);
        savedNote.setRecipe(savedRecipe);
        ingredientForRecipe.forEach(ingredient -> ingredient.setRecipe(recipe));
        recipe.setIngredients(ingredientForRecipe);

        return save(recipe);
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
    public Iterable<RecipeDto> findAllDto() {
        LinkedList<RecipeDto> recipeDtos = new LinkedList<>();
        findAll().forEach(recipe -> recipeDtos.add(convertRecipeToRecipeDto(recipe)));
        return recipeDtos;
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
            Optional<Category> categoryOptional = categoryService.findById(categoryDto.getId());
            if(categoryOptional.isPresent()) {
                categoryOptional.get().setName(categoryDto.getName());
                categoryService.save(categoryOptional.get());
            }
        });

        return save(recipe);
    }

    @Override
    public RecipeDto convertRecipeToRecipeDto(Recipe recipe) {
        return recipeToRecipeDto.convert(recipe);
    }
}

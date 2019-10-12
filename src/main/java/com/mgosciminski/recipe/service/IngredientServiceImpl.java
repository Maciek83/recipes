package com.mgosciminski.recipe.service;


import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.repository.IngredientRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final String NOT_FOUND = "can't find this id";


    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient findById(Long id) throws NotFoundException {

        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);

        return optionalIngredient.orElseThrow(()-> new NotFoundException(NOT_FOUND));
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public void delete(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
    }


}

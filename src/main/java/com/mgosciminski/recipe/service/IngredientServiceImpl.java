package com.mgosciminski.recipe.service;


import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    private final Ingredient nullObject = new Ingredient();
    private final String BAD = "bad";


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
    public Ingredient findById(Long id) {

        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);

        if (optionalIngredient.isPresent()) {
            return optionalIngredient.get();
        } else {
            nullObject.setDescription(BAD);
            return nullObject;
        }
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

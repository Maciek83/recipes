package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.IngredientDtoToIngredient;
import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.repository.IngredientRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientDtoToIngredient ingredientConverter;
    private final UomService uomService;

    private final Ingredient nullObject = new Ingredient();
    private final String BAD = "bad";


    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 IngredientDtoToIngredient ingredientConverter,
                                 UomService uomService) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = ingredientConverter;
        this.uomService = uomService;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient save(IngredientDto ingredientDto) throws NotFoundException {

        UnitOfMeasure savedUom = uomService.save(uomService.findByUom(ingredientDto.getUnitOfMeasure()).orElseThrow(()-> new NotFoundException("not found")));
        Ingredient ingredient = ingredientConverter.convert(ingredientDto);

        if (ingredient != null)
            ingredient.setUnitOfMeasure(savedUom);

        savedUom.getIngredients().add(ingredient);

        return ingredientRepository.save(ingredient);
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

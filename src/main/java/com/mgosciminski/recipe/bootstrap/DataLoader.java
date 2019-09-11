package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.IngredientService;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UomService uomService;
    private final IngredientService ingredientService;

    public DataLoader(UomService uomService, IngredientService ingredientService) {
        this.uomService = uomService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void run(String... args) throws Exception {

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setUom("kilos");

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("cola");
        ingredientDto.setUnitOfMeasureDto(unitOfMeasureDto);

        ingredientService.save(ingredientDto);

        System.out.println("ingredient loaded");

    }
}

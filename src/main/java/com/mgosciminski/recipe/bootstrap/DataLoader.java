package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.NoteDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.CategoryService;
import com.mgosciminski.recipe.service.IngredientService;
import com.mgosciminski.recipe.service.NoteService;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UomService uomService;
    private final IngredientService ingredientService;
    private final NoteService noteService;
    private final CategoryService categoryService;

    public DataLoader(UomService uomService, IngredientService ingredientService, NoteService noteService, CategoryService categoryService) {
        this.uomService = uomService;
        this.ingredientService = ingredientService;
        this.noteService = noteService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setUom("kilos");

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("cola");
        ingredientDto.setUnitOfMeasureDto(unitOfMeasureDto);

        ingredientService.save(ingredientDto);

        NoteDto noteDto = new NoteDto();
        noteDto.setNotes("myNotes");
        noteService.save(noteDto);

        Category category = new Category();
        category.setName("departName");

        categoryService.save(category);

        System.out.println("data loaded");

    }
}

package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.NoteDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.IngredientService;
import com.mgosciminski.recipe.service.NoteService;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    private final UomService uomService;
    private final IngredientService ingredientService;
    private final NoteService noteService;

    public DataLoader(UomService uomService, IngredientService ingredientService, NoteService noteService) {
        this.uomService = uomService;
        this.ingredientService = ingredientService;
        this.noteService = noteService;
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

        System.out.println("data loaded");

    }
}

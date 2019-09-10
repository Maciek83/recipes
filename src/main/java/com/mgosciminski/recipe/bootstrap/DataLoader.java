package com.mgosciminski.recipe.bootstrap;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UomService uomService;

    public DataLoader(UomService uomService) {
        this.uomService = uomService;
    }

    @Override
    public void run(String... args) throws Exception {

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setUom("kilos");
        uomService.save(unitOfMeasureDto);

        System.out.println("Loaded data");

    }
}

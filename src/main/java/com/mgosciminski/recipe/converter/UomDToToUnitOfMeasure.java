package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UomDToToUnitOfMeasure implements Converter<UnitOfMeasureDto, UnitOfMeasure> {

    //todo add test
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureDto unitOfMeasureDto) {

        if(unitOfMeasureDto == null)
        {return null;}

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom(unitOfMeasureDto.getUom());

        return unitOfMeasure;

    }
}

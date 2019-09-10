package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUomDto implements Converter<UnitOfMeasure, UnitOfMeasureDto> {

    @Nullable
    @Override
    public synchronized UnitOfMeasureDto convert(UnitOfMeasure unitOfMeasure) {

        if (unitOfMeasure == null) return null;

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setUom(unitOfMeasure.getUom());

        return unitOfMeasureDto;
    }
}

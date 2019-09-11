package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static org.junit.Assert.*;

@Component
public class UomDtoToUnitOfMeasureTest {

    private UomDtoToUnitOfMeasure converter;

    @Before
    public void setUp() throws Exception {
        converter = new UomDtoToUnitOfMeasure();
    }

    @Test
    public void convertNull() throws Exception
    {
       UnitOfMeasure unitOfMeasure = converter.convert(null);

       assertNull(unitOfMeasure);
    }


    @Test
    public void convert() throws Exception
    {

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setUom("uom");

        UnitOfMeasure converted = converter.convert(unitOfMeasureDto);

        assertNotNull(converted);
        assertEquals(unitOfMeasureDto.getUom(),converted.getUom());
    }
}
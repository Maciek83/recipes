package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class UnitOfMeasureToUomDtoTest {

    public static final Long ID_VALUE = 1L;
    public static final String UOM = "uom";


    UnitOfMeasureToUomDto converter;

    @Before
    public void setUp() throws Exception {

        converter = new UnitOfMeasureToUomDto();
    }

    @Test
    public void nullObject() throws Exception
    {
        assertNull(converter.convert(null));
    }

    @Test
    public void convert() throws Exception
    {

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom(UOM);


        UnitOfMeasureDto unitOfMeasureDto = converter.convert(unitOfMeasure);

        assertEquals(unitOfMeasureDto.getUom(),UOM);
    }
}
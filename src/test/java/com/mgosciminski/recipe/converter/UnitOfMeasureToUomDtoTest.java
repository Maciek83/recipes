package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class UnitOfMeasureToUomDtoTest {


    private static final String UOM = "uom";

    private UnitOfMeasureToUomDto converter;

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
        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom(UOM);

        //when
        UnitOfMeasureDto unitOfMeasureDto = converter.convert(unitOfMeasure);

        //then
        assertNotNull(unitOfMeasureDto);
        assertEquals(unitOfMeasureDto.getUom(),UOM);
    }
}
package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.UomDToToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.repository.UomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UomServiceImplTest {

    @Mock
    private UomRepository uomRepository;
    @Mock
    private UomDToToUnitOfMeasure converter;

    @InjectMocks
    private UomServiceImpl uomService;


    private Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        unitOfMeasures.add(new UnitOfMeasure());
        unitOfMeasures.add(new UnitOfMeasure());
    }

    @Test
    public void findAll() throws Exception
    {
        //when
        when(uomRepository.findAll()).thenReturn(unitOfMeasures);
        Iterable<UnitOfMeasure> result = uomService.findAll();

        //then
        assertNotNull(result);
        assertEquals(result,unitOfMeasures);

        verify(uomRepository,times(1)).findAll();
    }

    @Test
    public void save() throws Exception
    {

        //given
        UnitOfMeasureDto ofMeasureDto = new UnitOfMeasureDto();
        UnitOfMeasure ofMeasureToSave = new UnitOfMeasure();

        //when
        when(uomRepository.save(any())).thenReturn(ofMeasureToSave);

        UnitOfMeasure returned = uomService.save(ofMeasureDto);

        //then
        assertNotNull(returned);
        assertEquals(returned,ofMeasureToSave);

        verify(uomRepository).save(any());
    }

    @Test
    public void delete() {
        //when
        uomService.delete(new UnitOfMeasure());

        //then
        verify(uomRepository,times(1)).delete(any());
    }

    @Test
    public void deleteById() {
        //when
        uomService.delete(1L);

        //then
        verify(uomRepository,times(1)).deleteById(anyLong());
    }
}
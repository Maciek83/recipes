package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.UomDtoToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.repository.UomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UomServiceImplTest {

    @Mock
    private UomRepository uomRepository;
    @Mock
    private UomDtoToUnitOfMeasure converter;

    @InjectMocks
    private UomServiceImpl uomService;

    private UomServiceImpl uomServiceSpy;


    private Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        unitOfMeasures.add(new UnitOfMeasure());
        unitOfMeasures.add(new UnitOfMeasure());

        uomServiceSpy = Mockito.spy(uomService);
    }

    @Test
    public void findByUom() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasureOptional = Optional.of(new UnitOfMeasure());

        //when
        when(uomRepository.findByUom(anyString())).thenReturn(unitOfMeasureOptional);
        Optional<UnitOfMeasure> unitOfMeasure = uomService.findByUom("aa");

        //then
        assertNotNull(unitOfMeasure);
        assertEquals(unitOfMeasureOptional,unitOfMeasure);

        verify(uomRepository).findByUom(anyString());
    }

    @Test
    public void findByUomEmpty() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasureOptional = Optional.empty();

        //when
        when(uomRepository.findByUom(anyString())).thenReturn(unitOfMeasureOptional);
        Optional<UnitOfMeasure> unitOfMeasure = uomService.findByUom("aa");

        //then
        assertEquals(unitOfMeasure,Optional.empty());

        verify(uomRepository).findByUom(anyString());
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
    public void saveWhenExist() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasure = Optional.of(new UnitOfMeasure());

        //when
        doReturn(unitOfMeasure).when(uomServiceSpy).findByUom(anyString());
        Optional<UnitOfMeasure> result = uomServiceSpy.findByUom("aa");

        //then
        assertEquals(unitOfMeasure,result);

        verifyZeroInteractions(uomRepository);
    }

    @Test
    public void saveWhenDontExist() throws Exception
    {

        //given
        UnitOfMeasureDto ofMeasureDto = new UnitOfMeasureDto();
        ofMeasureDto.setUom("aa");
        UnitOfMeasure ofMeasureToSave = new UnitOfMeasure();

        //when
        doReturn(Optional.empty()).when(uomServiceSpy).findByUom(anyString());
        when(uomRepository.save(any())).thenReturn(ofMeasureToSave);

        UnitOfMeasure returned = uomServiceSpy.save(ofMeasureDto);

        //then
        assertNotNull(returned);
        assertEquals(returned,ofMeasureToSave);

        verify(uomServiceSpy).findByUom(anyString());
        verify(uomRepository).save(any());
    }

    @Test
    public void delete() throws Exception {
        //when
        uomService.delete(new UnitOfMeasure());

        //then
        verify(uomRepository,times(1)).delete(any());
    }

    @Test
    public void deleteById() throws Exception {
        //when
        uomService.delete(1L);

        //then
        verify(uomRepository,times(1)).deleteById(anyLong());
    }
}
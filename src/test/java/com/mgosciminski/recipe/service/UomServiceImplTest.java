package com.mgosciminski.recipe.service;


import com.mgosciminski.recipe.domain.UnitOfMeasure;

import com.mgosciminski.recipe.repository.UomRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @InjectMocks
    private UomServiceImpl uomService;

    private UomServiceImpl uomServiceSpy;

    private Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String NOT_FOUND = "can't find this id";

    @Before
    public void setUp() throws Exception {
        unitOfMeasures.add(new UnitOfMeasure());
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("bobo");
        unitOfMeasure.setId(1L);
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure.setDescription("bobo2");
        unitOfMeasure.setId(2L);
        unitOfMeasures.add(unitOfMeasure);
        unitOfMeasures.add(unitOfMeasure2);

        uomServiceSpy = Mockito.spy(uomService);
    }

    @Test
    public void findByDescription() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasureOptional = Optional.of(new UnitOfMeasure());
        when(uomRepository.findByDescription(anyString())).thenReturn(unitOfMeasureOptional);

        //when
        Optional<UnitOfMeasure> unitOfMeasure = uomService.findByDescription("aa");

        //then
        assertNotNull(unitOfMeasure);
        assertEquals(unitOfMeasureOptional,unitOfMeasure);

        verify(uomRepository).findByDescription(anyString());
    }

    @Test
    public void findByDescriptionEmpty() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasureOptional = Optional.empty();
        when(uomRepository.findByDescription(anyString())).thenReturn(unitOfMeasureOptional);

        //when
        Optional<UnitOfMeasure> unitOfMeasure = uomService.findByDescription("aa");

        //then
        assertEquals(unitOfMeasure,Optional.empty());

        verify(uomRepository).findByDescription(anyString());
    }

    @Test
    public void findAll() throws Exception
    {
        //given
        when(uomRepository.findAll()).thenReturn(unitOfMeasures);

        //when
        Set<UnitOfMeasure> result = (Set<UnitOfMeasure>) uomRepository.findAll();

        //then
        assertNotNull(result);
        assertEquals(result,unitOfMeasures);
        assertEquals(result.size(),2);
        verify(uomRepository).findAll();

    }

    @Test
    public void saveWhenExist() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasure = Optional.of(new UnitOfMeasure());
        doReturn(unitOfMeasure).when(uomServiceSpy).findByDescription(anyString());

        //when
        Optional<UnitOfMeasure> result = uomServiceSpy.findByDescription("aa");

        //then
        assertEquals(unitOfMeasure,result);

        verifyZeroInteractions(uomRepository);
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

    @Test
    public void findById() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasureOptional = Optional.of(new UnitOfMeasure());
        when(uomRepository.findById(anyLong())).thenReturn(unitOfMeasureOptional);

        //when
        Optional<UnitOfMeasure> result = uomService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(result,unitOfMeasureOptional);
    }

    @Test
    public void findByIdPresent() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasure = Optional.of(new UnitOfMeasure());
        doReturn(unitOfMeasure).when(uomServiceSpy).findById(anyLong());

        //when
        UnitOfMeasure result = uomServiceSpy.findByIdPresentOrException(1L);

        //then
        assertNotNull(result);
        verify(uomServiceSpy).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findByIdThrowException() throws Exception
    {
        //given
        doReturn(Optional.empty()).when(uomServiceSpy).findById(anyLong());

        //when
        UnitOfMeasure result = uomServiceSpy.findByIdPresentOrException(1L);

        //then
        verify(uomServiceSpy).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }

    @Test
    public void edit() throws Exception
    {
        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        doReturn(unitOfMeasure).when(uomServiceSpy).save(any());

        //when
        UnitOfMeasure result = uomServiceSpy.edit(new UnitOfMeasure());

        //then
        assertEquals(result,unitOfMeasure);
        verify(uomServiceSpy).edit(any());

    }
}
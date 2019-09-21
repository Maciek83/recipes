package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.UnitOfMeasureToUomDto;
import com.mgosciminski.recipe.converter.UomDtoToUnitOfMeasure;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UomServiceImplTest {

    @Mock
    private UomRepository uomRepository;
    @Mock
    private UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;
    @Mock
    private UnitOfMeasureToUomDto unitOfMeasureToUomDto;

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
        unitOfMeasure.setUom("bobo");
        unitOfMeasure.setId(1L);
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure.setUom("bobo2");
        unitOfMeasure.setId(2L);
        unitOfMeasures.add(unitOfMeasure);
        unitOfMeasures.add(unitOfMeasure2);

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
        //given
        when(uomRepository.findAll()).thenReturn(unitOfMeasures);
        doReturn(new UnitOfMeasureDto()).when(uomServiceSpy).convertToDto(any(UnitOfMeasure.class));

        //when
        List<UnitOfMeasureDto> result = (List<UnitOfMeasureDto>) uomServiceSpy.findAllDto();


        //then
        assertNotNull(result);
        assertEquals(result.size(),unitOfMeasures.size());
        verify(uomRepository,times(1)).findAll();
        verify(uomServiceSpy,times(2)).convertToDto(any());
    }

    @Test
    public void saveWhenExist() throws Exception
    {
        //given
        Optional<UnitOfMeasure> unitOfMeasure = Optional.of(new UnitOfMeasure());
        doReturn(unitOfMeasure).when(uomServiceSpy).findByUom(anyString());

        //when
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

    @Test(expected = NotFoundException.class)
    public void findByIdDtoNotPresent() throws Exception
    {
        //given
        doReturn(Optional.empty()).when(uomServiceSpy).findById(anyLong());

        //when
        uomServiceSpy.findDtoById(1L);

        //then
        verify(uomServiceSpy).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }

    @Test
    public void findByIdDtoPresent() throws Exception
    {
        //given
        doReturn(Optional.of(new UnitOfMeasure())).when(uomServiceSpy).findById(anyLong());
        doReturn(new UnitOfMeasureDto()).when(uomServiceSpy).convertToDto(new UnitOfMeasure());
        //when
        UnitOfMeasureDto unitOfMeasureDto = uomServiceSpy.findDtoById(1L);

        //then
        assertNotNull(unitOfMeasureDto);
        verify(uomServiceSpy).findById(anyLong());
        verify(uomServiceSpy).convertToDto(any());
    }

    @Test
    public void convertToDto() throws Exception
    {
        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        unitOfMeasure.setUom("item");
        when(unitOfMeasureToUomDto.convert(any())).thenReturn(new UnitOfMeasureDto());

        //when
        UnitOfMeasureDto result = uomService.convertToDto(unitOfMeasure);

        //then
        assertNotNull(result);

        verify(unitOfMeasureToUomDto).convert(any());

    }

    @Test
    public void editPresent() throws Exception
    {
        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setId(1L);
        doReturn(Optional.of(unitOfMeasure)).when(uomServiceSpy).findById(anyLong());
        doReturn(unitOfMeasure).when(uomServiceSpy).save(any(UnitOfMeasure.class));

        //when
        UnitOfMeasure result = uomServiceSpy.edit(unitOfMeasureDto);

        //then
        assertNotNull(result);
        assertEquals(result,unitOfMeasure);

        verify(uomServiceSpy).findById(anyLong());
        verify(uomServiceSpy).save(any(UnitOfMeasure.class));
    }

    @Test(expected = NotFoundException.class)
    public void editNotPresent() throws Exception
    {
        //given
        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setId(1L);
        doReturn(Optional.empty()).when(uomServiceSpy).findById(anyLong());

        //when
        uomServiceSpy.edit(unitOfMeasureDto);

        //then
        verify(uomServiceSpy).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);

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
}
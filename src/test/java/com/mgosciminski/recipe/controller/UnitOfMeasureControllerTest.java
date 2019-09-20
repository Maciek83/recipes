package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.UomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class UnitOfMeasureControllerTest {

    @Mock
    UomService uomService;

    @Mock
    Model model;

    @InjectMocks
    UnitOfMeasureController unitOfMeasureController;

    private MockMvc mockMvc;

    private final String UOM_FORM = "uom/form/index.html";

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(unitOfMeasureController)
                .build();
    }

    @Test
    public void testMockMCV() throws Exception {


        mockMvc.perform(get("/uom"))
                .andExpect(status().isOk())
                .andExpect(view().name("uom/index"));
    }

    @Test
    public void showUom() throws Exception {
        //given
        Set<UnitOfMeasureDto> unitOfMeasureDtos = new HashSet<>();
        unitOfMeasureDtos.add(new UnitOfMeasureDto());

        UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
        unitOfMeasureDto.setId(1L);
        unitOfMeasureDto.setUom("kilos");
        unitOfMeasureDtos.add(unitOfMeasureDto);

        //when
        when(uomService.findAll()).thenReturn(unitOfMeasureDtos);
        ArgumentCaptor<Set<UnitOfMeasureDto>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        String viewName = unitOfMeasureController.showUom(model);

        //then
        assertEquals(viewName, "uom/index");
        verify(uomService).findAll();
        verify(model).addAttribute(eq("uoms"), argumentCaptor.capture());
        Set<UnitOfMeasureDto> unitOfMeasureSet = argumentCaptor.getValue();
        assertEquals(2, unitOfMeasureDtos.size());
        assertEquals(unitOfMeasureDtos, argumentCaptor.getValue());
    }

    @Test
    public void goToForm() throws Exception {
        mockMvc.perform(get("/uom/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM));

        ArgumentCaptor<UnitOfMeasureDto> unitOfMeasureToUomDtoArgumentCaptor = ArgumentCaptor.forClass(UnitOfMeasureDto.class);
        String value = unitOfMeasureController.showForm(model);

        assertNotNull(value);
        assertEquals(value, UOM_FORM);
        verify(model).addAttribute(eq("uom"), unitOfMeasureToUomDtoArgumentCaptor.capture());

    }

    @Test
    public void addNewUomErrors() throws Exception
    {
        mockMvc.perform(post("/uom/new")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM));
    }

    @Test
    public void addNewUomOkIdNull() throws Exception {
        mockMvc.perform(post("/uom/new")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("uom","unit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/uom"));
    }

    @Test
    public void addNewUomIdNotNull()throws Exception
    {
        //given
        when(uomService.findById(anyLong())).thenReturn(Optional.of(new UnitOfMeasure()));
        when(uomService.save(any(UnitOfMeasure.class))).thenReturn(new UnitOfMeasure());

        //when
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("uom","unit")
                .param("id","1")
                )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/uom"));

        //then
        verify(uomService).findById(anyLong());
        verify(uomService).save(any(UnitOfMeasure.class));
    }

    @Test
    public void addNewUomIdNotNullButOptionalNull()throws Exception
    {
        //given
        when(uomService.findById(anyLong())).thenReturn(Optional.empty());

        //when
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("uom","unit")
                .param("id","1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/uom"));

        //then
        verify(uomService).findById(anyLong());
        verify(uomService,times(0)).save(any(UnitOfMeasure.class));
    }

    @Test
    public void addNewUomEmptyData() throws Exception{
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("uom",""))
            .andExpect(status().isOk());
    }

    @Test
    public void editExisting() throws Exception
    {
        //given
        when(uomService.findById(anyLong())).thenReturn(Optional.of(new UnitOfMeasure()));
        when(uomService.convertToDto(any())).thenReturn(new UnitOfMeasureDto());

        //when
        mockMvc.perform(get("/uom/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM))
                .andExpect(model().attributeExists("uom"));

        //then
        verify(uomService).findById(anyLong());
        verify(uomService).convertToDto(any());
    }

    @Test
    public void editNull() throws Exception
    {
        //given
        when(uomService.findById(anyLong())).thenReturn(Optional.empty());

        //when
        mockMvc.perform(get("/uom/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM))
                .andExpect(model().attributeExists("uom"))
                .andExpect(model().attribute("uom",hasProperty("uom",is("imBad"))));


        //then
        verify(uomService).findById(anyLong());
        verify(uomService,times(0)).convertToDto(any());
    }

}
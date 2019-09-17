package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.converter.UnitOfMeasureToUomDto;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        unitOfMeasures.add(new UnitOfMeasure());

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        unitOfMeasure.setUom("kilos");
        unitOfMeasures.add(unitOfMeasure);

        //when
        when(uomService.findAll()).thenReturn(unitOfMeasures);
        ArgumentCaptor<Set<UnitOfMeasure>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        String viewName = unitOfMeasureController.showUom(model);

        //then
        assertEquals(viewName, "uom/index");
        verify(uomService).findAll();
        verify(model).addAttribute(eq("uoms"), argumentCaptor.capture());
        Set<UnitOfMeasure> unitOfMeasureSet = argumentCaptor.getValue();
        assertEquals(2, unitOfMeasures.size());
        assertEquals(unitOfMeasures, argumentCaptor.getValue());
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
    public void addNewUomOk() throws Exception {
        mockMvc.perform(post("/uom/new")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("uom","unit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/uom"));
    }

    @Test
    public void assNewUomEmptyData() throws Exception{
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("uom",""))
            .andExpect(status().is4xxClientError());
    }

}
package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
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

import java.util.Optional;

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
    private final String Error404 = "404error";

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(unitOfMeasureController)
                .build();
    }

    @Test
    public void showForm() throws Exception {

        mockMvc.perform(get("/uom/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("uom"))
                .andExpect(view().name(UOM_FORM));
    }

    @Test
    public void testMockMCV() throws Exception {


        mockMvc.perform(get("/uom"))
                .andExpect(status().isOk())
                .andExpect(view().name("uom/index"));
    }

    @Test
    public void addNewUomErrors() throws Exception {
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM));
    }

    @Test
    public void addNewUomEmptyData() throws Exception {
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM))
        ;
    }

    @Test
    public void addUomNoId() throws Exception {
        //when
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "opis")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/uom"));

        //then
        verify(uomService).save(any());
    }

    @Test
    public void addUomId() throws Exception
    {
        //when
        mockMvc.perform(post("/uom/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","1")
                .param("description", "opis")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/uom"));

        //then
        verify(uomService).save(any());
    }

    @Test
    public void editExisting() throws Exception {
        //given
        when(uomService.findById(anyLong())).thenReturn(Optional.of(new UnitOfMeasure()));

        //when
        mockMvc.perform(get("/uom/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(UOM_FORM))
                .andExpect(model().attributeExists("uom"));

        //then
        verify(uomService).findById(anyLong());

    }

    @Test
    public void editIdNotNumber() throws Exception {
        mockMvc.perform(get("/uom/1c/edit"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

}
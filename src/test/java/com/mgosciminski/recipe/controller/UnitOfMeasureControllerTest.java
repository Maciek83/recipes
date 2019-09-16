package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.service.UomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void testMockMCV() throws Exception
    {
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(unitOfMeasureController)
                .build();

        mockMvc.perform(get("/uom"))
                .andExpect(status().isOk())
                .andExpect(view().name("uom/index"));
    }

    @Test
    public void getIndexPage() throws Exception
    {
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
        assertEquals(viewName,"uom/index");
        verify(uomService).findAll();
        verify(model).addAttribute(eq("uoms"), argumentCaptor.capture());
        Set<UnitOfMeasure> unitOfMeasureSet = argumentCaptor.getValue();
        assertEquals(2,unitOfMeasures.size());
    }

}
package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.service.CategoryService;
import org.junit.Before;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class CategoryControllerTest {

    @Mock
    CategoryService categoryService;

    @Mock
    Model model;

    @InjectMocks
    CategoryController categoryController;

    private MockMvc mockMvc;

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();
    }

    @Test
    public void testMockMCV() throws Exception {


        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/index"));
    }

    @Test
    public void showCategory() throws Exception {

        //given
        Set<CategoryDto> categoryDtos = new HashSet<>();
        categoryDtos.add(new CategoryDto());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("cat");
        categoryDtos.add(categoryDto);

        when(categoryService.findAll()).thenReturn(categoryDtos);
        //then
        ArgumentCaptor<Set<CategoryDto>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        String result = categoryController.showCategory(model);

        //then
        assertEquals(result,"category/index");
        verify(categoryService).findAll();
        verify(model).addAttribute(eq("categories"), argumentCaptor.capture());
        Set<CategoryDto> r = argumentCaptor.getValue();
        assertEquals(categoryDtos,r);
        assertEquals(2,r.size());

    }
}
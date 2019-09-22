package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.CategoryService;
import javassist.NotFoundException;
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

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private final String NOT_FOUND = "can't find this id";
    private final String CAT_FORM = "category/form/index";
    private final String Error404 = "404error";

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

        when(categoryService.findAllDto()).thenReturn(categoryDtos);
        //then
        ArgumentCaptor<Set<CategoryDto>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        String result = categoryController.showCategory(model);

        //then
        assertEquals(result,"category/index");
        verify(categoryService).findAllDto();
        verify(model).addAttribute(eq("categories"), argumentCaptor.capture());
        Set<CategoryDto> r = argumentCaptor.getValue();
        assertEquals(categoryDtos,r);
        assertEquals(2,r.size());

    }

    @Test
    public void showForm() throws Exception
    {
        //given
        mockMvc.perform(get("/category/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("category"))
                .andExpect(view().name(CAT_FORM));
        //when
        ArgumentCaptor<UnitOfMeasureDto> unitOfMeasureDtoArgumentCaptor = ArgumentCaptor.forClass(UnitOfMeasureDto.class);
        String view = categoryController.showForm(model);

        //then
        assertNotNull(view);
        assertEquals(view,CAT_FORM);
        verify(model).addAttribute(eq("category"),unitOfMeasureDtoArgumentCaptor.capture());

    }

    @Test
    public void editPresent() throws Exception
    {
        //given
        when(categoryService.findDtoById(anyLong())).thenReturn(new CategoryDto());

        //when
        mockMvc.perform(get("/category/1/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name","dto")
                .param("id","1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CAT_FORM))
                .andExpect(model().attributeExists("category"));


        //then
        verify(categoryService).findDtoById(anyLong());
    }

    @Test
    public void editIdNotNumber() throws Exception
    {
        mockMvc.perform(get("/category/1d/edit"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void editNotPresent() throws Exception
    {
        //given
        when(categoryService.findDtoById(anyLong())).thenThrow(new NotFoundException(NOT_FOUND));

        //when
        mockMvc.perform(get("/category/11/edit"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));

        //then
        verify(categoryService).findDtoById(anyLong());

    }

    @Test
    public void addCategoryWithBugs() throws Exception
    {
        mockMvc.perform(post("/category/new")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(CAT_FORM));
    }

    @Test
    public void addCategoryWithNoId() throws Exception
    {
        mockMvc.perform(post("/category/new")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name","myName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category"));

        verify(categoryService,times(1)).save(any(CategoryDto.class));
    }

    @Test
    public void addCategoryWithIdAndPresent() throws Exception
    {
        when(categoryService.findByIdPresentOfException(anyLong())).thenReturn(new Category());

        mockMvc.perform(post("/category/new")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name","myName")
                .param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category"));

        verify(categoryService).findByIdPresentOfException(anyLong());
        verify(categoryService,times(1)).save(any(Category.class));
    }

    @Test
    public void addCategoryWithIdButNotPresent()throws Exception
    {
        when(categoryService.findByIdPresentOfException(anyLong())).thenThrow(new NotFoundException(NOT_FOUND));

        mockMvc.perform(post("/category/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name","myName")
                .param("id","1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));

        verify(categoryService).findByIdPresentOfException(anyLong());
    }
}
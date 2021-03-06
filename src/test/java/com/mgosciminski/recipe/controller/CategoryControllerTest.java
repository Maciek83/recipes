package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.service.CategoryService;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        Set<Category> categoryDtos = new HashSet<>();
        categoryDtos.add(new Category());

        Category categoryDto = new Category();
        categoryDto.setName("cat");
        categoryDtos.add(categoryDto);

        when(categoryService.findAll()).thenReturn(categoryDtos);
        //then
        ArgumentCaptor<Set<Category>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        String result = categoryController.showCategory(model);

        //then
        assertEquals(result,"category/index");
        verify(categoryService).findAll();
        verify(model).addAttribute(eq("categories"), argumentCaptor.capture());
        Set<Category> r = argumentCaptor.getValue();
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
        ArgumentCaptor<UnitOfMeasure> unitOfMeasureDtoArgumentCaptor = ArgumentCaptor.forClass(UnitOfMeasure.class);
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
        when(categoryService.findById(anyLong())).thenReturn(Optional.of(new Category()));

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
        verify(categoryService).findById(anyLong());
    }

    @Test
    public void editIdNotNumber() throws Exception
    {
        mockMvc.perform(get("/category/1d/edit"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
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

        verify(categoryService,times(1)).save(any(Category.class));
    }

    @Test
    public void addCategoryWithId() throws Exception
    {
        mockMvc.perform(post("/category/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name","myName")
                .param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category"));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }
}
package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.service.CategoryService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final String CAT_FORM = "category/form/index";
    private final String Error404 = "404error";
    private final String NOT_FOUND = "can't find this id";

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategory(Model model)
    {
        model.addAttribute("categories",categoryService.findAllDto());

        return "category/index";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String showForm(Model model)
    {
        model.addAttribute("category", new CategoryDto());

        return CAT_FORM;
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable String id,Model model) throws NotFoundException
    {
        try { Long.valueOf(id); }
        catch (Exception e) { throw new NotFoundException(NOT_FOUND);}

        CategoryDto categoryDto = categoryService.findDtoById(Long.valueOf(id));
        model.addAttribute("category",categoryDto);

        return CAT_FORM;
    }

    @PostMapping("/new")
    public String addCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult bindingResult) throws Exception
    {
        if(bindingResult.hasErrors())
        {
            return CAT_FORM;
        }

        if(categoryDto.getId() == null)
        {
            categoryService.save(categoryDto);
        }
        else
        {
            Category category = categoryService.findByIdPresentOfException(categoryDto.getId());
            category.setName(categoryDto.getName());
            categoryService.save(category);
        }

        return "redirect:/category";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }


}

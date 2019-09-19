package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final String CAT_FORM = "category/form/index";

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategory(Model model)
    {
        model.addAttribute("categories",categoryService.findAll());

        return "category/index";
    }

    @GetMapping("new")
    @ResponseStatus(HttpStatus.OK)
    public String showForm(Model model)
    {
        model.addAttribute(new CategoryDto());

        return CAT_FORM;
    }
}

package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String showForm(Model model)
    {
        model.addAttribute("category", new CategoryDto());

        return CAT_FORM;
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable String id,Model model)
    {
        Optional<Category> optionalCategory = categoryService.findById(Long.valueOf(id));

        if(optionalCategory.isPresent())
        {
            CategoryDto categoryDto = categoryService.convertCategoryToCategoryDto(optionalCategory.get());
            model.addAttribute("category",categoryDto);
        }
        else
        {
            CategoryDto nullObj = new CategoryDto();
            nullObj.setId(-1L);
            nullObj.setName("imBad");
            model.addAttribute("category",nullObj);
        }

        return CAT_FORM;
    }

    @PostMapping("/new")
    public String addCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult bindingResult)
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
            Optional<Category> optionalCategory = categoryService.findById(categoryDto.getId());

            if(optionalCategory.isPresent())
            {
                Category category = optionalCategory.get();
                category.setName(categoryDto.getName());
                categoryService.save(category);
            }
        }

        return "redirect:/category";
    }


}

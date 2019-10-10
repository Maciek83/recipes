package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Category;
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
        model.addAttribute("categories",categoryService.findAll());

        return "category/index";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String showForm(Model model)
    {
        model.addAttribute("category", new Category());

        return CAT_FORM;
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable String id,Model model) throws NotFoundException
    {
        try { Long.valueOf(id); }
        catch (Exception e) { throw new NotFoundException(NOT_FOUND);}

        Category categoryDto = categoryService.findById(Long.valueOf(id)).orElseThrow(()->new NotFoundException(NOT_FOUND));
        model.addAttribute("category",categoryDto);

        return CAT_FORM;
    }

    @PostMapping("/new")
    public String addCategory(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult) throws Exception
    {
        if(bindingResult.hasErrors())
        {
            return CAT_FORM;
        }

        if(category.getId() == null)
        {
            categoryService.save(category);
        }
        else
        {
            category.setName(category.getName());
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

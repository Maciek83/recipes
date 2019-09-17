package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/uom")
public class UnitOfMeasureController {

    private final UomService uomService;
    private final String UOM_FORM = "uom/form/index.html";

    public UnitOfMeasureController(UomService uomService) {
        this.uomService = uomService;
    }

    @GetMapping
    public String showUom(Model model)
    {
        model.addAttribute("uoms",uomService.findAll());

        return "uom/index";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String showForm(Model model)
    {
        model.addAttribute("uom", new UnitOfMeasureDto());

        return UOM_FORM;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    public String addUom(@Valid @ModelAttribute("uom") UnitOfMeasureDto unitOfMeasureDto, ModelAndView model, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            model.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return UOM_FORM;
        }

        uomService.save(unitOfMeasureDto);
        return "redirect:/uom";
    }

}

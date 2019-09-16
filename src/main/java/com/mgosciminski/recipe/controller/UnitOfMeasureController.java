package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.service.UomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uom")
public class UnitOfMeasureController {

    private final UomService uomService;

    public UnitOfMeasureController(UomService uomService) {
        this.uomService = uomService;
    }

    @GetMapping
    public String showUom(Model model)
    {
        model.addAttribute("uoms",uomService.findAll());

        return "uom/index";
    }
}

package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.UomService;
import javassist.NotFoundException;
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
    private final String Error404 = "404error";
    private final String UOM_FORM = "uom/form/index.html";
    private final String NOT_FOUND = "can't find this id";

    public UnitOfMeasureController(UomService uomService) {
        this.uomService = uomService;
    }


    @GetMapping
    public String showUom(Model model) {
        model.addAttribute("uoms", uomService.findAllDto());

        return "uom/index";
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String showForm(Model model) {
        model.addAttribute("uom", new UnitOfMeasureDto());

        return UOM_FORM;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model) throws NotFoundException {

        try {Long.valueOf(id); }
        catch (Exception e) { throw new NotFoundException(NOT_FOUND);}

        UnitOfMeasureDto unitOfMeasureDto = uomService.findDtoById(Long.valueOf(id));
        model.addAttribute("uom", unitOfMeasureDto);

        return UOM_FORM;
    }

    @PostMapping({"/new"})
    public String addUom(@Valid @ModelAttribute("uom") UnitOfMeasureDto unitOfMeasureDto, BindingResult bindingResult) throws NotFoundException {

        if (bindingResult.hasErrors()) {
            return UOM_FORM;
        }

        if (unitOfMeasureDto.getId() == null) {
            uomService.save(unitOfMeasureDto);
        } else {
            UnitOfMeasure unitOfMeasure = uomService.findByIdPresentOrException(unitOfMeasureDto.getId());
            unitOfMeasure.setUom(unitOfMeasureDto.getUom());
            uomService.save(unitOfMeasure);
        }

        return "redirect:/uom";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }
}

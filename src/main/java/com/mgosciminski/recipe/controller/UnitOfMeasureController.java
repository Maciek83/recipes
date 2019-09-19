package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id,Model model)
    {
        Optional<UnitOfMeasure> unitOfMeasureOptional = uomService.findById(Long.valueOf(id));

        if(unitOfMeasureOptional.isPresent()) {
            UnitOfMeasureDto unitOfMeasureDto = uomService.convertToDto(unitOfMeasureOptional.get());
            model.addAttribute("uom", unitOfMeasureDto);
        }
        else
        {
            UnitOfMeasureDto unitOfMeasureDto = new UnitOfMeasureDto();
            unitOfMeasureDto.setUom("imBad");
            model.addAttribute("uom",unitOfMeasureDto);
        }
        return UOM_FORM;
    }

    @PostMapping({"/new"})
    public String addUom(@Valid @ModelAttribute("uom") UnitOfMeasureDto unitOfMeasureDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return UOM_FORM;
        }

        if(unitOfMeasureDto.getId() == null)
        {
            uomService.save(unitOfMeasureDto);
        }
        else
        {
            Optional<UnitOfMeasure> unitOfMeasureOptional = uomService.findById(unitOfMeasureDto.getId());

            if(unitOfMeasureOptional.isPresent()) {
                UnitOfMeasure u = unitOfMeasureOptional.get();
                u.setUom(unitOfMeasureDto.getUom());
                uomService.save(u);
            }
        }

        return "redirect:/uom";
    }

}

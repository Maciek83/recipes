package com.mgosciminski.recipe.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UnitOfMeasureDto {

    private Long id;
    @NotBlank
    @Size(min = 3, max = 255)
    private String uom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public UnitOfMeasureDto() {
    }
}

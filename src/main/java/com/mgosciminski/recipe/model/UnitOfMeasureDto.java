package com.mgosciminski.recipe.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitOfMeasureDto)) return false;
        UnitOfMeasureDto that = (UnitOfMeasureDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(uom, that.uom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uom);
    }
}

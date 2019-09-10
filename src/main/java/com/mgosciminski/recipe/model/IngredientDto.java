package com.mgosciminski.recipe.model;

import java.math.BigDecimal;

public class IngredientDto {
    private String description;
    private BigDecimal amount;
    private Long recipeId;
    private UnitOfMeasureDto unitOfMeasureDto;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public UnitOfMeasureDto getUnitOfMeasureDto() {
        return unitOfMeasureDto;
    }

    public void setUnitOfMeasureDto(UnitOfMeasureDto unitOfMeasureDto) {
        this.unitOfMeasureDto = unitOfMeasureDto;
    }
}

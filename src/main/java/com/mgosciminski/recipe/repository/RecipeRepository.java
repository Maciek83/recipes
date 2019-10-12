package com.mgosciminski.recipe.repository;

import com.mgosciminski.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeRepository extends CrudRepository<Recipe,Long> {

}

package com.mgosciminski.recipe.repository;

import com.mgosciminski.recipe.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UomRepository extends CrudRepository<UnitOfMeasure,Long> {
}

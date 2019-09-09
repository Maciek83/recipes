package com.mgosciminski.recipe.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String departmentName;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Recipe> recipes = new HashSet<>();


}

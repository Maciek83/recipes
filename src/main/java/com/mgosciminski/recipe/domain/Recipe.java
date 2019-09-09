package com.mgosciminski.recipe.domain;

import javax.persistence.*;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    @Lob
    private String directions;
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "recipe")
    private Note notes;



}

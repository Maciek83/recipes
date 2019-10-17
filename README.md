<ul>
                <li>Technical
                    <p>I used spring boot (jpa,web,thymeleaf,test).</p>
                </li>
                <li>Description
                    <p>The application is used to manage recipes. Each recipe consists with many properties like ingredients and categories.</p>
                </li>
                <li>Logic
                    <p>
                        The application allows you to add recipes. Each recipe can be edited and deleted.
                        You can add ingredients and categories to each recipe. Each ingredient can be removed separately
                        and the categories edited. In addition, you can add units of measure to which the ingredient is assigned.
                    </p>
                </li>
                <li>Database side
                    <p>Each <strong>Recipe</strong> has a one-to-one relationship with <strong>Note</strong> and
                        one-to-many relationship with <strong>Ingredient</strong> and many-to-many relationship with <strong>Category</strong>.</p>

                    <p>Each <strong>Note</strong> has a one-to-one relationship with <strong>Recipe</strong>.</p>

                    <p>Each <strong>Category</strong> has a many-to-many relationship with <strong>Recipe</strong>.</p>

                    <p>Each <strong>Ingredient</strong> has a many-to-one relationship with <strong>Recipe</strong>
                         and many-to-one relationship with <strong>Unit of Measure</strong>.</p>

                    <p>Each <strong>Unit of Measure</strong> has a one-to-many relationship with <strong>Ingredient</strong>.</p>
                </li>
            </ul>

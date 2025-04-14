package tacos.data.impl;

import tacos.domain.Ingredient;

import java.util.Optional;

// interface for usual implementation with JDBCTemplate
// no Spring Data
public interface IngredientRepositoryWithoutData {
    Iterable<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);
}

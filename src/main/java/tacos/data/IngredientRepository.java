package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.Ingredient;

// store Ingredient, primary key (id) - String

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}

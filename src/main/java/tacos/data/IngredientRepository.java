package tacos.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import tacos.domain.Ingredient;

// store Ingredient, primary key (id) - String

public interface IngredientRepository extends ListCrudRepository<Ingredient, String> {
}

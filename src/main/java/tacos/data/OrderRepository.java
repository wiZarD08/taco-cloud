package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.TacoOrder;

// has some basic methods
public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
}

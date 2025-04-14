package tacos.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import tacos.domain.Ingredient;
import tacos.domain.IngredientRef;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

//@Repository
public class JdbcOrderRepository implements OrderRepositoryWithoutData {
    private final JdbcOperations jdbcOperations; // interface (same JdbcTemplate)

    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public TacoOrder save(TacoOrder order) {
        order.setPlacedAt(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // new PreparedStatementCreator()
        jdbcOperations.update(con -> {
            PreparedStatement ps = con.prepareStatement("insert into Taco_Order "
                            + "(delivery_name, delivery_street, delivery_city, "
                            + "delivery_state, delivery_zip, cc_number, "
                            + "cc_expiration, cc_cvv, placed_at) "
                            + "values (?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getDeliveryName());
            ps.setString(2, order.getDeliveryStreet());
            ps.setString(3, order.getDeliveryCity());
            ps.setString(4, order.getDeliveryState());
            ps.setString(5, order.getDeliveryZip());
            ps.setString(6, order.getCcNumber());
            ps.setString(7, order.getCcExpiration());
            ps.setString(8, order.getCcCVV());
            ps.setObject(9, order.getPlacedAt());
            return ps;
        }, keyHolder);

        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        List<Taco> tacos = order.getTacos();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    public void saveTaco(long orderId, long orderKey, Taco taco) {
        jdbcOperations.update("insert into Taco (name, taco_order, taco_order_key, created_at)"
                + " values (?,?,?,?)", taco.getName(), orderId, orderKey, taco.getCreatedAt());
        // get id from select statement (not really safe)
        Long tacoId = jdbcOperations.query("select id from Taco where created_at = ? and name = ?",
                (ResultSet resultSet, int rowNum) -> resultSet.getLong("id"),
                taco.getCreatedAt(), taco.getName()).get(0);
        taco.setId(tacoId);
        List<Ingredient> ingredients = taco.getIngredients();
        List<IngredientRef> ingredientRefs = ingredients.stream()
                .map((x) -> new IngredientRef(x.getId())).toList();
        int i = 0;
        for (IngredientRef ref : ingredientRefs) {
            saveIngredient(tacoId, i++, ref);
        }
    }

    public void saveIngredient(long tacoId, long tacoKey, IngredientRef ingredientRef) {
        jdbcOperations.update("insert into Ingredient_Ref (ingredient, taco, taco_key)" +
                " values (?, ?, ?)", ingredientRef.getIngredient(), tacoId, tacoKey);
    }
}

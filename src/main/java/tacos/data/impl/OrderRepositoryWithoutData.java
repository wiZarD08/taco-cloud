package tacos.data.impl;

import tacos.domain.TacoOrder;

public interface OrderRepositoryWithoutData {
    TacoOrder save(TacoOrder tacoOrder);
}

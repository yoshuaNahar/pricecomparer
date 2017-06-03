package nl.yoshuan.pricecomparer.daos;//package nl.yoshuan.model.dao;

import nl.yoshuan.pricecomparer.entities.ProductVariables;
import org.springframework.stereotype.Repository;

@Repository
public class ProductVariablesDaoImpl extends GenericDaoImpl<ProductVariables, Long> implements ProductVariablesDao {

    public ProductVariablesDaoImpl() {
        super(ProductVariables.class);
    }

}

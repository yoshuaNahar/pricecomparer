package nl.yoshuan.pricecomparer.dao;//package nl.yoshuan.model.dao;

import nl.yoshuan.pricecomparer.entities.ProductVariables;
import org.springframework.stereotype.Repository;

@Repository
public class ProductVariablesDaoImpl extends GenericDaoImpl<ProductVariables, Long> implements ProductVariablesDao {

    public ProductVariablesDaoImpl() {
        super(ProductVariables.class);
    }

    @Override
    public ProductVariables persistIfNotExist(ProductVariables entity) {
        throw new UnsupportedOperationException("The Category en Product class implement this method; this class doesn't, because nothing is unique besides the id.");
    }

}

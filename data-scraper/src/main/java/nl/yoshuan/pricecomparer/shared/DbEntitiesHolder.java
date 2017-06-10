package nl.yoshuan.pricecomparer.shared;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;

// When scraping each product the data is placed into the relevant supermarket entity.
// It will then be split into my db entities and placed into this class for easier handling
public class DbEntitiesHolder {

    private Category category;
    private Product product;
    private ProductVariables productVariables;

    public DbEntitiesHolder(Category category, Product product, ProductVariables productVariables) {
        this.category = category;
        this.product = product;
        this.productVariables = productVariables;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariables getProductVariables() {
        return productVariables;
    }

    public void setProductVariables(ProductVariables productVariables) {
        this.productVariables = productVariables;
    }

}

package nl.yoshuan.pricecomparer.entities;

// I need to use projection with this class to get the product name and category id.
// This will be used in the luceneHelper
public class SimpleProduct {

    private String name;
    private Long categoryId;

    public SimpleProduct(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}

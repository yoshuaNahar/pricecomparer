package nl.yoshuan.pricecomparer.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "brand", nullable = false)
    private String brand;

    @ManyToOne() // no CascadeType.ALL, because I already persist my categories manually
    @JoinColumn(name = "category_id", nullable = false, updatable = false)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductVariables> productsVariables = new ArrayList<>();

    protected Product() {}

    public Product(String name, String amount, String brand, Category category, List<ProductVariables> productsVariables) {
        this.name = name;
        this.amount = amount;
        this.brand = brand;
        this.category = category;
        this.productsVariables = productsVariables;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", brand='" + brand + '\'' +
                ", category=" + category +
                ", productsVariables=" + productsVariables +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductVariables> getProductsVariables() {
        return productsVariables;
    }

    public void setProductsVariables(List<ProductVariables> productsVariables) {
        this.productsVariables = productsVariables;
    }

}

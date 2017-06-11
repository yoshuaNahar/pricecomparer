package nl.yoshuan.pricecomparer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_variables")
@JsonIgnoreProperties({"product"})
public class ProductVariables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_variables_id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "product_src", nullable = false, updatable = false)
    private String productSrc;

    @Column(name = "image_src", updatable = false) // some products have no img apparently
    private String imageSrc;

    @Column(name = "price", nullable = false, updatable = false)
    private int price;

    @Column(name = "bonus_price", updatable = false)
    private int bonusPrice;

    @Column(name = "bonus", updatable = false)
    private String bonus;

    @Column(name = "bonus_image_src", updatable = false)
    private String bonusImageSrc;

    @Column(name = "supermarket", nullable = false, updatable = false) // columnDefinition = "ENUM('AH', 'JUMBO')"
    @Enumerated(EnumType.STRING)
    private Supermarket supermarket;

    @Column(name = "product_icons", updatable = false)
    private String productIcons;

    @Column(name = "date", nullable = false, insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    protected ProductVariables() {}

    public ProductVariables(String productSrc, String imageSrc, int price, int bonusPrice, String bonus, String bonusImageSrc, Supermarket supermarket, String productIcons, Date date, Product product) {
        this.productSrc = productSrc;
        this.imageSrc = imageSrc;
        this.price = price;
        this.bonusPrice = bonusPrice;
        this.bonus = bonus;
        this.bonusImageSrc = bonusImageSrc;
        this.supermarket = supermarket;
        this.productIcons = productIcons;
        this.date = date;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getProductSrc() {
        return productSrc;
    }

    public void setProductSrc(String productSrc) {
        this.productSrc = productSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBonusPrice() {
        return bonusPrice;
    }

    public void setBonusPrice(int bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getBonusImageSrc() {
        return bonusImageSrc;
    }

    public void setBonusImageSrc(String bonusImageSrc) {
        this.bonusImageSrc = bonusImageSrc;
    }

    public Supermarket getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(Supermarket supermarket) {
        this.supermarket = supermarket;
    }

    public String getProductIcons() {
        return productIcons;
    }

    public void setProductIcons(String productIcons) {
        this.productIcons = productIcons;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public enum Supermarket {
        AH, JUMBO
    }

}

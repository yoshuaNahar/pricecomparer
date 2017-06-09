package nl.yoshuan.pricecomparer.ah.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AhProduct {

    @SerializedName("description") private String name;
    @SerializedName("id") private String productSrc; // To go to the actual product: https://www.ah.nl/producten/product/{productSrc}
    private String brandName;
    private String imageSrc; // set manually
    private String unitSize;
    private Discount discount = new Discount(); //only the label, not the type name (ah has BONUS for all almost all their labels (route 99, 2 for 0.99, Bonus) Only a few have ONLINE_BONUS.
    private String discountImageSrc; // not shown in json page for example the 2 VOOR 0.99 image .. Need to add this as last
    private List<String> propertyIcons = new ArrayList<>();
    private String categoryName;
    private String fullCategoryName; // set manually
    private PriceLabel priceLabel = new PriceLabel();

    public AhProduct(String productSrc, String brandName, String imageSrc, String unitSize, String name, Discount discount, String discountImageSrc, List<String> propertyIcons, String categoryName, String fullCategoryName, PriceLabel priceLabel) {
        this.productSrc = productSrc;
        this.brandName = brandName;
        this.imageSrc = imageSrc;
        this.unitSize = unitSize;
        this.name = name;
        this.discount = discount;
        this.discountImageSrc = discountImageSrc;
        this.propertyIcons = propertyIcons;
        this.categoryName = categoryName;
        this.fullCategoryName = fullCategoryName;
        this.priceLabel = priceLabel;
    }

    public String getProductSrc() {
        return productSrc;
    }

    public void setProductSrc(String productSrc) {
        this.productSrc = productSrc;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountImageSrc() {
        return discountImageSrc;
    }

    public void setDiscountImageSrc(String discountImageSrc) {
        this.discountImageSrc = discountImageSrc;
    }

    public List<String> getPropertyIcons() {
        return propertyIcons;
    }

    public void setPropertyIcons(List<String> propertyIcons) {
        this.propertyIcons = propertyIcons;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFullCategoryName() {
        return fullCategoryName;
    }

    public void setFullCategoryName(String fullCategoryName) {
        this.fullCategoryName = fullCategoryName;
    }

    public PriceLabel getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(PriceLabel priceLabel) {
        this.priceLabel = priceLabel;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "AhProduct{" +
                "name='" + name + '\'' +
                ", productSrc='" + productSrc + '\'' +
                ", brandName='" + brandName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", unitSize='" + unitSize + '\'' +
                ", discount=" + discount +
                ", discountImageSrc='" + discountImageSrc + '\'' +
                ", propertyIcons=" + propertyIcons +
                ", categoryName='" + categoryName + '\'' +
                ", fullCategoryName='" + fullCategoryName + '\'' +
                ", priceLabel=" + priceLabel +
                '}';
    }

    public static class PriceLabel {

        @SerializedName("now") private String priceNow;
        @SerializedName("was") private String priceWas;

        protected PriceLabel() {}

        public PriceLabel(String priceNow, String priceWas) {
            this.priceNow = priceNow;
            this.priceWas = priceWas;
        }

        public String getPriceNow() {
            return priceNow;
        }

        public void setPriceNow(String priceNow) {
            this.priceNow = priceNow;
        }

        public String getPriceWas() {
            return priceWas;
        }

        public void setPriceWas(String priceWas) {
            this.priceWas = priceWas;
        }

        @Override
        public String toString() {
            return "PriceLabel{" +
                    "priceNow='" + priceNow + '\'' +
                    ", priceWas='" + priceWas + '\'' +
                    '}';
        }

    }

    public static class Discount {

        private String label;

        protected Discount() {}

        public Discount(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "Discount{" +
                    "label='" + label + '\'' +
                    '}';
        }

    }

}

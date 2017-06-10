package nl.yoshuan.pricecomparer.jumbo.entities;

public class JumboProduct {

    private String name;
    private String productSrc;
    private String brandName; // first word in name is always the brand!
    private String imgUrl;
    private String unitSize;
    private int price;
    private int bonusPrice; // Price was and price will get the bonus price (just like ah)
    private String bonusType;
    private String bonusImg;
    // Jumbo doesnt have product icons

    public JumboProduct() {}

    public JumboProduct(String name, String productSrc, String brandName, String imgUrl, String unitSize, int price, int bonusPrice, String bonusType, String bonusImg) {
        this.name = name;
        this.productSrc = productSrc;
        this.brandName = brandName;
        this.imgUrl = imgUrl;
        this.unitSize = unitSize;
        this.price = price;
        this.bonusPrice = bonusPrice;
        this.bonusType = bonusType;
        this.bonusImg = bonusImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
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

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public String getBonusImg() {
        return bonusImg;
    }

    public void setBonusImg(String bonusImg) {
        this.bonusImg = bonusImg;
    }

    @Override
    public String toString() {
        return "JumboProduct{" +
                "name='" + name + '\'' +
                ", productSrc='" + productSrc + '\'' +
                ", brandName='" + brandName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", unitSize='" + unitSize + '\'' +
                ", price='" + price + '\'' +
                ", bonusPrice=" + bonusPrice +
                ", bonusType='" + bonusType + '\'' +
                ", bonusImg='" + bonusImg + '\'' +
                '}';
    }

}

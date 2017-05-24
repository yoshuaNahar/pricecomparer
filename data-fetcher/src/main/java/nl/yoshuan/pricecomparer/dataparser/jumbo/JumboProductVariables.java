package nl.yoshuan.pricecomparer.dataparser.jumbo;

public class JumboProductVariables {

    private int productVariablesId;
    private int price;
    private int bonusPrice;
    private String bonusType;
    private String bonusImg;
    private int productId;
    private int dateId;

    protected JumboProductVariables() {
    }

    public JumboProductVariables(int productVariablesId, int price, int bonusPrice, String bonusType, String bonusImg, int productId, int dateId) {
        this.productVariablesId = productVariablesId;
        this.price = price;
        this.bonusPrice = bonusPrice;
        this.bonusType = bonusType;
        this.bonusImg = bonusImg;
        this.productId = productId;
        this.dateId = dateId;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getProductVariablesId() {
        return productVariablesId;

    }

    public void setProductVariablesId(int productVariablesId) {
        this.productVariablesId = productVariablesId;
    }

    @Override
    public String toString() {
        return "JumboProductVariables{" +
                "productVariablesId=" + productVariablesId +
                ", price=" + price +
                ", bonusPrice=" + bonusPrice +
                ", bonusType='" + bonusType + '\'' +
                ", bonusImg='" + bonusImg + '\'' +
                ", productId=" + productId +
                ", dateId=" + dateId +
                '}';
    }

}

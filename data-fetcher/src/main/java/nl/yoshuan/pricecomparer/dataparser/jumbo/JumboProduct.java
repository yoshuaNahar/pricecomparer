package nl.yoshuan.pricecomparer.dataparser.jumbo;

import java.util.ArrayList;
import java.util.List;

public class JumboProduct {

    private String id;
    private String category;
    private String subCategory;
    private String brand;
    private String supermarket;
    private String siteUrl;
    private String imgUrl;
    private String weightAmount;
    private List<JumboProductVariables> jumboProductVariablesList = new ArrayList<>();

    protected JumboProduct() {
    }

    public JumboProduct(String id, String category, String subCategory, String brand, String supermarket, String siteUrl, String imgUrl, String weightAmount, List<JumboProductVariables> jumboProductVariablesList) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.supermarket = supermarket;
        this.siteUrl = siteUrl;
        this.imgUrl = imgUrl;
        this.weightAmount = weightAmount;
        this.jumboProductVariablesList = jumboProductVariablesList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWeightAmount() {
        return weightAmount;
    }

    public void setWeightAmount(String weightAmount) {
        this.weightAmount = weightAmount;
    }

    public List<JumboProductVariables> getJumboProductVariablesList() {
        return jumboProductVariablesList;
    }

    public void setJumboProductVariablesList(List<JumboProductVariables> jumboProductVariablesList) {
        this.jumboProductVariablesList = jumboProductVariablesList;
    }

    public String getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(String supermarket) {
        this.supermarket = supermarket;
    }

    @Override
    public String toString() {
        return "JumboProduct{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", brand='" + brand + '\'' +
                ", supermarket='" + supermarket + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", weightAmount='" + weightAmount + '\'' +
                ", jumboProductVariablesList=" + jumboProductVariablesList +
                '}';
    }

}

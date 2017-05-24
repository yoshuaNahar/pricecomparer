package nl.yoshuan.pricecomparer.dataparser.jumbo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Upload code to github
// TODO: Rewrite code from getElements to select(xpath)
// TODO: Write Readme with every spec
// TODO: FINISH THIS AND WRITE REAL ALGORITHM

public class JumboDataParser {

    private static final String CATEGORY = "Aardappel, rijst, pasta";
    private static final String[] SUB_CATEGORY = { "Ongeschilde aardapelen", "Geschilde aardapelen", "Aardappelpuree",
            "Pasta", "Rijst", "Couscous", "Quinoa", "Mie", "Noedels", "Mihoen" };

    // tmp
    private final String htmlFileLocation;

    // because when searching online the subcategories have a dash instead of a space
    static {
        Arrays.asList(SUB_CATEGORY).replaceAll(x -> x.replace(" ", "-"));
    }

    public JumboDataParser(String htmlFileLocation) {
        this.htmlFileLocation = htmlFileLocation;
    }

    private boolean pageEmpty(Elements dl_s) {
        if (dl_s.isEmpty()) {
            return true;
        }
        return false;
    }

    private String getAnchorHrefValue(Element anchor) {
        String aHref[] = anchor.attr("href").split(";");
//        checkArgument(aHref.length <= 3, "The href tag sometimes contains the url and this ';pgid=...;sid'");
        return aHref[0];
    }

    private Element getImg(Element product) {
        Elements img = product.getElementsByAttribute("data-jum-hr-src");
//        checkArgument(img.size() == 1, "The this dl node, there should be only one img tag with attr data-jum-hr-src.");

        return img.first();
    }

    private Element getBadge(Element product) {
        Elements badges = product.select("div[class = jum-promotion jum-mediumbadge] > img");
        if (badges.isEmpty()) {
            return null;
        }
//        checkArgument(badges.size() == 1,
//                "In this dl node, there should only be one tag with class jum-promotion jum-mediumbadge.");
        return badges.first();
    }

    private String getBadgeSrcValue(Element badge) {
        String src = badge.attr("src");

        Pattern p = Pattern.compile("&FileName=.*png");
        Matcher matcher = p.matcher(src);
        matcher.find();

        return src.substring((matcher.start() + 10), matcher.end()); //&FileName= has length 10
    }

    private Element getPriceDiv(Element product) {
        Elements priceDiv = product.getElementsByClass("jum-sale-price-info");
//        checkArgument(priceDiv.size() == 1,
//                "The this dl node, there should be only one tag with class jum-sale-price-info.");
        return priceDiv.first();
    }

    private Element getPriceInputField(Element product) {
        Elements priceInput = product.getElementsByAttribute("jum-data-price");
//        checkArgument(priceInput.size() == 1, "there should be only one tag with attribute jum-data-price.");
        return priceInput.first();
    }

    private boolean productHasBonus(Element badgeImg) {
        if (badgeImg == null) {
            return false;
        }
        return true;
    }

    public Element getFirstProductRaw(Elements products) {
        return products.first();
    }

    public List<JumboProduct> getProducts(Elements products) {
        int amountOfProductsOnPage = products.size();
        List<JumboProduct> jumboProducts = new ArrayList<>();

        for (int i = 0; i < amountOfProductsOnPage; i++) {
            Element product = products.get(i);

            Element anchor = getAnchor(product);
            String anchorHrefText = getAnchorHrefValue(anchor);
            String anchorText = anchor.text();

            Element img = getImg(product);
            String imgAttrText = img.attr("data-jum-hr-src");

            Element badgeImg = getBadge(product);
            String badgeAttrText = null;
            String badgeSrcText = null;
            if (productHasBonus(badgeImg)) {
                badgeAttrText = badgeImg.attr("alt");
                badgeSrcText = getBadgeSrcValue(badgeImg);
            }

            Element priceDiv = getPriceDiv(product);
            String priceText = priceDiv.text(); // Prints all the text within this div.

            Element priceInputField = getPriceInputField(product);
            String actualPrice = priceInputField.attr("jum-data-price");

            JumboProductVariables productVariables = new JumboProductVariables();
            productVariables.setPrice((int) (Double.parseDouble(actualPrice) * 100));
            productVariables.setBonusPrice(9999999);
            productVariables.setBonusType(badgeSrcText);
            productVariables.setBonusImg(badgeAttrText);
            productVariables.setDateId(1111);
            productVariables.setDateId(1);

            JumboProduct _product = new JumboProduct();
            _product.setId(anchorText);
            _product.setCategory(CATEGORY);
            _product.setSubCategory(SUB_CATEGORY[0]);
            _product.setBrand("Jumbo");
            _product.setSupermarket("Jumbo");
            _product.setSiteUrl(anchorHrefText);
            _product.setImgUrl(imgAttrText);
            _product.setWeightAmount(priceText);
            _product.getJumboProductVariablesList().add(productVariables);

            System.out.println(_product);
            jumboProducts.add(_product);
        }

        return jumboProducts;
    }

    public void runScraper() {
        boolean hasNextNumberPage;

        for (int subCatIndex = 0; subCatIndex < SUB_CATEGORY.length; subCatIndex++) {
            hasNextNumberPage = true;
            for (int numberPage = 0; hasNextNumberPage; numberPage++) {
                Elements products = getPage(SUB_CATEGORY[subCatIndex], numberPage);

                if (pageEmpty(products)) {
                    hasNextNumberPage = false;
                    continue;
                }

                getProducts(products);
                System.out.println("=== OTHER PAGE ===");

                hasNextNumberPage = false; // this should be true, if in production
            }
            break; // Remove if in production
        }
    }

    private Element getAnchor(Element product) {
        Elements a = product.getElementsByTag("a");
//        checkArgument(a.size() == 1, "In this dl node, there should be only one a tag.");

        return a.first();
    }

    private Elements getPage(String subCategory, int pageNumber) {
        Document document = null;
        try {
            document = Jsoup.connect("https://www.jumbo.com/producten/categorieen/aardappel,-rijst,-pasta/"
                    + subCategory + "/?PageNumber=" + pageNumber).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document.getElementsByTag("dl");
    }

    public void test() {
        String string = "https://www.jumbo.com/INTERSHOP/web/WFS/Jumbo-Grocery-Site/nl_NL/-/EUR/ViewPromotionAttachment-OpenFile;pgid=656nyL2z7u5SRpdIf9Zv96c40000XLQXg5AB;sid=VGLo6lzpITTU6gQjsar09j_j3PlfWdRy49FZW6wk?LocaleId=&DirectoryPath=Jaaraanbiedingen2017%2FPrijs-badge&FileName=Jumbo-Jaaraanbiedingen-badge-M-2v3.png&UnitName=Jumbo-Grocery";

        Pattern p = Pattern.compile("&FileName=.*png");
        Matcher matcher = p.matcher(string);
        matcher.find();
        System.out.println(string.substring((matcher.start() + 10), matcher.end()));
    }

    public Elements readLocalFile() {
        File file = new File(getClass().getClassLoader().getResource(htmlFileLocation).getFile());
        StringBuilder result = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Document document = Jsoup.parse(result.toString());

        return document.getElementsByTag("dl");
    }

}

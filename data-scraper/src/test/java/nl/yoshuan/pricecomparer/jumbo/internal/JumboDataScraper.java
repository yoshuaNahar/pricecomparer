package nl.yoshuan.pricecomparer.jumbo.internal;

import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Rewrite code from getElements to select(xpath)
// TODO: Write Readme with every spec
// TODO: FINISH THIS AND WRITE REAL ALGORITHM

public class JumboDataScraper {

    private static final String CATEGORY = "Aardappel, rijst, pasta";
    private static final String[] SUB_CATEGORY = {"Ongeschilde aardapelen", "Geschilde aardapelen", "Aardappelpuree",
            "Pasta", "Rijst", "Couscous", "Quinoa", "Mie", "Noedels", "Mihoen"};

    // because when searching online the subcategories have a dash instead of a space
    static {
        Arrays.asList(SUB_CATEGORY).replaceAll(x -> x.replace(" ", "-"));
    }

    private final String htmlFileLocation;


    public JumboDataScraper(String htmlFileLocation) {
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
        return aHref[0];
    }

    private Element getImg(Element product) {
        Elements img = product.getElementsByAttribute("data-jum-hr-src");
        return img.first();
    }

    private Element getBadge(Element product) {
        Elements badges = product.select("div[class = jum-promotion jum-mediumbadge] > img");
        if (badges.isEmpty()) {
            return null;
        }
        return badges.first();
    }

    private String getBadgeSrcValue(Element badge) {
        String src = badge.attr("src");
        return getBonusTypeImageFromSrc(src); //&FileName= has length 10
    }

    String getBonusTypeImageFromSrc(String url) {
        Pattern p = Pattern.compile("&FileName=.*png");
        Matcher matcher = p.matcher(url);
        matcher.find();
        return url.substring((matcher.start() + 10), matcher.end());
    }

    private Element getPriceDiv(Element product) {
        Elements priceDiv = product.getElementsByClass("jum-sale-price-info");
        return priceDiv.first();
    }

    private Element getPriceInputField(Element product) {
        Elements priceInput = product.getElementsByAttribute("jum-data-price");
        return priceInput.first();
    }

    private int getBonusPrice(Element product) {
        Elements bonusPriceElements = product.getElementsByClass("jum-was-price");
        Element bonusPriceElement = bonusPriceElements.first();

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = null;
        try {
            if (bonusPriceElement != null) {
                number = format.parse(bonusPriceElement.text());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int bonusPrice = 0;
        if (number != null) {
            bonusPrice = (int) (number.doubleValue() * 100);
        }

        return bonusPrice;
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
        List<JumboProduct> jumboProducts = new ArrayList<>();

        int amountOfProductsOnPage = products.size();
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

            JumboProduct jumboProduct = new JumboProduct();
            jumboProduct.setName(anchorText);
            jumboProduct.setProductSrc(anchorHrefText);
            jumboProduct.setUnitSize(priceText);
            jumboProduct.setBrandName(getBrandName(anchorText));

            jumboProduct.setPrice((int) (Double.parseDouble(actualPrice) * 100));
            jumboProduct.setBonusType(badgeSrcText);
            jumboProduct.setBonusImg(badgeAttrText);
            jumboProduct.setImgUrl(imgAttrText);

            jumboProduct.setBonusPrice(getBonusPrice(product));

            System.out.println(jumboProduct);
            jumboProducts.add(jumboProduct);
        }

        return jumboProducts;
    }

    private String getBrandName(String text) {
        if (text.indexOf(' ') > -1) { // Check if there is more than one word.
            return text.substring(0, text.indexOf(' ')); // Extract first word.
        } else {
            return text; // Wont ever occur tho
        }
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

package nl.yoshuan.pricecomparer.jumbo.scraper;

import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import nl.yoshuan.pricecomparer.shared.DataScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.yoshuan.pricecomparer.App.SLEEP_TIME_BETWEEN_REQUESTS;

@Component
public class JumboDataScraper extends DataScraper<JumboProduct> {

    private static final Logger logger = LoggerFactory.getLogger(JumboDataScraper.class);
    public static int ALL_PAGES = 10_000;

    @Override
    public List<JumboProduct> getAllProducts() {
        return getJumboProductsFrom(ALL_PAGES);
    }

    /**
     * Jumbo doesn't have the same category structure. Pages are numbered and there are
     * 12 products on each page.
     *
     * @param amountOfPagesToScrape amount of pages to scrape
     * @return list of scraped <code>JumboProduct</code>s
     */
    public List<JumboProduct> getJumboProductsFrom(int amountOfPagesToScrape) {
        List<JumboProduct> jumboProducts = new ArrayList<>();
        for (int pageNumber = 0; pageNumber < amountOfPagesToScrape; pageNumber++) {
            Elements products = getProductsOnPage(pageNumber);
            logger.info("Amount of products: " + products.size());
            logger.info(products.toString());

            if (products.isEmpty()) {
                break;
            }

            jumboProducts.addAll(getProductData(products));
            logger.info("jumboProducts size: " + jumboProducts.size());
            logger.info("=== OTHER PAGE ===");
            waitBetweenRequests(SLEEP_TIME_BETWEEN_REQUESTS);
        }

        return jumboProducts;
    }

    private Elements getProductsOnPage(int pageNumber) {
        Document document = null;
        try {
            document = Jsoup.connect("https://www.jumbo.com/producten/?PageNumber=" + pageNumber).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document.getElementsByTag("dl");
    }

    private List<JumboProduct> getProductData(Elements products) {
        List<JumboProduct> jumboProducts = new ArrayList<>();

        for (Element product : products) {
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
            jumboProduct.setBonusType(badgeAttrText);
            jumboProduct.setBonusImg(badgeSrcText);
            jumboProduct.setImgUrl(imgAttrText);

            jumboProduct.setBonusPrice(getBonusPrice(product));

            logger.info(jumboProduct.toString());
            jumboProducts.add(jumboProduct);
        }

        return jumboProducts;
    }

    private Element getAnchor(Element product) {
        Elements a = product.getElementsByTag("a");
        return a.first();
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

    private boolean productHasBonus(Element badgeImg) {
        return badgeImg != null;
    }

    private String getBadgeSrcValue(Element badge) {
        String src = badge.attr("src");
        return getBonusTypeImageFromSrc(src); //&FileName= has length 10
    }

    private String getBonusTypeImageFromSrc(String url) {
        Pattern p = Pattern.compile("&FileName=.*png");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return url.substring((matcher.start() + 10), matcher.end());
        }
        throw new RuntimeException("pattern should be found");
    }

    private Element getPriceDiv(Element product) {
        Elements priceDiv = product.getElementsByClass("jum-sale-price-info");
        return priceDiv.first();
    }

    private Element getPriceInputField(Element product) {
        Elements priceInput = product.getElementsByAttribute("jum-data-price");
        return priceInput.first();
    }

    private String getBrandName(String text) {
        if (text.indexOf(' ') > -1) { // Check if there is more than one word.
            return text.substring(0, text.indexOf(' ')); // Extract first word.
        } else {
            return text; // Wont ever occur tho
        }
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

}

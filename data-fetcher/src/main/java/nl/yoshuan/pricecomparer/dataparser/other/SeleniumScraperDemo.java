package nl.yoshuan.pricecomparer.dataparser.other;
//
//import nl.scraper.scrape_ah.element.content.productgroepcontent.HeleAssortiment;
//import nl.scraper.scrape_ah.page.AhPage;
//import nl.scraper.scrape_ah.page.LeftSideBar;
//import nl.scraper.scrape_ah.page.content.AfdelingenContent;
//import nl.scraper.scrape_ah.page.content.ProductContent;
//import nl.scraper.scrape_ah.page.content.ProductGroepContent;
//import nl.yoshuan.model.entity.dao.ProductDAO;
//import nl.yoshuan.scraperdb.model.entities.*;
//
//import java.util.LinkedList;
//
//public final class SeleniumScraperDemo {
//
//	private static LinkedList<Integer> currentProductLane = new LinkedList<>();
//
//	private static AhPage ahPage;
//	private static LeftSideBar leftSideBar;
//	private static AfdelingenContent afdelingenContent;
//	private static ProductGroepContent productGroepContent;
//	private static ProductContent productContent;
//
//	public static void main(String[] args) {
//		goToProductenPage();
//		searchPage();
//		ahPage.exit();
//	}
//
//	private static void searchPage() {
//		productGroepContent = (ProductGroepContent) ahPage.getContent();
//		currentProductLane.addLast(1);
//
//		while (hasNextProductLane()) {
//			handleProductLane();
//			System.out.println("1");
//			if (!hasNextProductLane()) {
//				for (int i = 1; productGroepContent.getNthSeeMore(i).isVisible(); i++) {
//					productGroepContent.getNthSeeMore(i).click();
//					System.out.println("99");
//					searchPage();
//				}
//				currentProductLane.removeLast();
//				ahPage.previousPage();
//				System.out.println("2");
//			}
//		}
//	}
//
//	private static boolean hasNextProductLane() {
//		int size = productGroepContent.amountOfProductLanes();
//		System.out.println("ProductLanes: " + size + " current:" + currentProductLane.getLast());
//		System.out.println(currentProductLane.toString());
//		return currentProductLane.getLast() <= size ? true : false;
//	}
//
//	private static void handleProductLane() {
//		HeleAssortiment heleAssortiment = productGroepContent.getNthHeleAssortiment(currentProductLane.getLast());
//		if (heleAssortiment.isVisible()) {
//			heleAssortiment.click();
//			System.out.println("3");
//			currentProductLane.addLast(currentProductLane.pollLast() + 1);
//			searchPage();
//			System.out.println("4");
//		} else {
//			getAllProductsFromProductLane();
//			System.out.println("5");
//		}
//		currentProductLane.addLast(currentProductLane.pollLast() + 1);
//	}
//
//	private static void getAllProductsFromProductLane() {
//		for (int j = 1; j <= productGroepContent.amountOfProductsInProductLane(currentProductLane.getLast()); j++) {
//			productGroepContent.getNthProduct(currentProductLane.getLast(), j).click();
//			productContent = (ProductContent) ahPage.getContent();
//			String text;
//			Product product = new Product();
//			ProductVariables productVariables = new ProductVariables();
//
//			product.setName(productContent.getProductName().getText().replace("�", ""));
//			text = product.getName();
//
//			productVariables.setPrice(Double.parseDouble(productContent.getProductPrice().getText()));
//			text += " | " + productVariables.getPrice();
//
//			String weight = productContent.getProductWeight().getText().replaceAll("[^\\d.]", "");
//			if (weight.isEmpty()) {
//				product.setAmount(1);
//			} else {
//				product.setAmount(Integer.parseInt(weight));
//			}
//			text += " | " + product.getAmount();
//
//			productVariables.setBonus(productContent.getProductBonusType().getText());
//			text += " | " + productVariables.getBonus();
//
//			String bonusPrice = productContent.getProductOriginalPrice().getText(); // Not specific enough!>
//			if (bonusPrice.contains("-")) {
//				bonusPrice = "0";
//			}
//			productVariables.setBonusPrice(Double.parseDouble(bonusPrice));
//			text += " | " + productVariables.getBonusPrice();
//
//			product.setUrl(productContent.getUrl());
//			text += " | " + product.getUrl();
//
//			System.out.println(text);
//			ahPage.previousPage();
//			productGroepContent = (ProductGroepContent) ahPage.getContent();
//			productGroepContent.amountOfProductLanes();
//
//			hibernateAdd(product, productVariables);
//		}
//	}
//
//	private static void goToProductenPage() {
//		ahPage = new AhPage();
//		leftSideBar = ahPage.getLeftSideBar();
//		leftSideBar.getProducten().click();
//		afdelingenContent = (AfdelingenContent) ahPage.getContent();
//		afdelingenContent.getNthAfdeling(1).click();
//	}
//
//
//	private static void hibernateAdd(Product product, ProductVariables productVariables) {
//		Type type = new Type();
//		Category department = new Category();
//		Date week = new Date();
//		ProductDao productDao = new ProductDao();
//
//		department.setProductSrc((byte) 1);
//		type.setProductSrc(1);
//		type.setDepartment(department);
//		week.setProductSrc(1);
//
//		productVariables.setWeek(week);
//		productVariables.setProduct(product);
//		product.setType(type);
//		product.getProductsVariables().add(productVariables);
//
//		productDao.create(product);
//	}
//
//}

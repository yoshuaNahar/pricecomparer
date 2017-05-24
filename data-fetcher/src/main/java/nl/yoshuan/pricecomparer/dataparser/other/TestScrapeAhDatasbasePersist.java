package nl.yoshuan.pricecomparer.dataparser.other;
//
//import nl.yoshuan.model.entity.dao.ProductDAO;
//import nl.yoshuan.model.entity.dao.ProductVariablesDaoImpl;
//import nl.yoshuan.scraperdb.model.entities.Product;
//import nl.yoshuan.scraperdb.model.entities.ProductVariables;
//import nl.yoshuan.scraperdb.model.entities.Type;
//import nl.yoshuan.scraperdb.model.entities.Date;
//
//final class TestScrapeAhDatasbasePersist {
//
//	private static Product product = new Product();
//	private static ProductVariables productVariables = new ProductVariables();
//	private static Date week = new Date();
//	private static Type type = new Type();
//
//	public static void main(String[] args) {
//		addNewProductVariablesToProduct();
//	}
//
//	private static void addNewProductVariablesToProduct() {
//		productVariables.setPrice(4.19);
//		productVariables.setProduct(product);
//		productVariables.setBonusPrice(0.0);
//		productVariables.setBonus("N");
//
//		product.setProductSrc(10L);
//
//		week.setProductSrc(1);
//
//		productVariables.setWeek(week);
//
//		ProductVariablesDaoImpl productVariablesDao = new ProductVariablesDaoImpl();
//		productVariablesDao.create(productVariables);
//	}
//
//	private static void addNewProduct() {
//		product.setName("Bloemkool");
//		product.setAmount(1);
//		product.setUrl("https://www.ah.nl/producten/product/wi4183/ah-bloemkool");
//
//		productVariables.setPrice(2.19);
//		productVariables.setBonus("BONUS");
//		productVariables.setBonusPrice(1.69);
//		productVariables.setProduct(product);
//
//		week.setProductSrc(1);
//
//		type.setProductSrc(1);
//
//		productVariables.setWeek(week);
//		product.setType(type);
//		product.getProductsVariables().add(productVariables);
//
//		ProductDao productDao = new ProductDao();
//		productDao.create(product);
//	}
//
//}

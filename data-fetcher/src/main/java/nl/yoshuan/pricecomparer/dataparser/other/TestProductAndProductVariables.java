package nl.yoshuan.pricecomparer.dataparser.other;
//
//import java.util.*;
//
//import nl.yoshuan.model.entity.dao.DepartmentDao;
//import nl.yoshuan.model.entity.dao.ProductDAO;
//import nl.yoshuan.model.entity.dao.ProductVariablesDaoImpl;
//import nl.yoshuan.scraperdb.model.entities.Category;
//import nl.yoshuan.scraperdb.model.entities.Product;
//import nl.yoshuan.scraperdb.model.entities.ProductVariables;
//import nl.yoshuan.scraperdb.model.entities.Type;
//
//final class TestProductAndProductVariables {
//
//	private static Map<Long, Category> departments = new HashMap<>();
//
//	public static void main(String[] args) {
//		// Scenario 1
//		getAllDepartments();
//		Product product = doScrape();
//		addProductWithProductVariables(product);
//		System.out.println("============================");
//		getAllProductVariables();
//		// Scenario 2
//		// getProductWithType();
//
//		// Scenario 3
//		// getProductVariableWithTypeAndWeek();
//	}
//
//	private static void getAllDepartments() {
//		DepartmentDao departmentDao = new DepartmentDao();
//		for (Category department : departmentDao.getAll()) {
//			departments.put(department.getProductSrc(), department);
//			System.out.println(department.getProductSrc());
//			System.out.println(department.getCategory());
//		}
//	}
//
//	private static Product doScrape() {
//		Product product = new Product();
//		product.setName("Brocolli");
//		product.setAmount(1);
//		Type type = new Type();
//		type.setDepartment(departments.get(3));
//		product.setType(type);
//		return product;
//	}
//
//	private static void addProductWithProductVariables(Product product) {
//		ProductVariables productVariables = new ProductVariables();
//		productVariables.setPrice(220);
//		productVariables.setBonusPrice(0);
//		productVariables.setProduct(product);
//		productVariables.setWeek(null);
//
//		ProductDao productDao = new ProductDao();
//		productDao.create(product);
//	}
//
//	private static void getAllProductVariables() {
//		ProductVariablesDaoImpl productVariablesDao = new ProductVariablesDaoImpl();
//		for (ProductVariables productVariables : productVariablesDao.getAll()) {
//			System.out.println(productVariables.getProductSrc());
//			System.out.println(productVariables.getPrice());
//			System.out.println(productVariables.getBonusPrice());
//			System.out.println(productVariables.getBonus());
//			System.out.println(productVariables.getWeek());
//			System.out.println(productVariables.getProduct().getName());
//		}
//	}
//
//}

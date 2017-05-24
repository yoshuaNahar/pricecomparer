package nl.yoshuan.pricecomparer;

import nl.yoshuan.pricecomparer.dao.CategoryDao;
import nl.yoshuan.pricecomparer.dao.CategoryDaoImpl;
import nl.yoshuan.pricecomparer.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;

@SpringBootApplication
public class Application {
    
    // I stopped learning from the book, because the book uses EJB's and JavaEE to setup the project, and I want to simulate that.
    // For that reason I bought an Udemy course and am now following it. If I finished it, I will continue doing that! -- Java Persistance with Hibernate on Desktop
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
    
    public static void dbLogic(EntityManager em) {
        System.out.println("start =============");

        testSetCategory(em);

        System.out.println("end =============");
    }

    private static void testGetCategory(EntityManager em) {
        System.out.println("start =============");

        em.getTransaction().begin();
        Category category = em.find(Category.class, 7L);
        em.getTransaction().commit();

        try {
            System.out.println(category.getCategoryName());
            System.out.println(category.getParentCategory().getCategoryName());
            System.out.println(category.getParentCategory().getParentCategory().getCategoryName());
            System.out.println(category.getParentCategory().getParentCategory().getParentCategory().getCategoryName());
            System.out.println(category.getParentCategory().getParentCategory().getParentCategory().getParentCategory().getCategoryName());
        } catch (NullPointerException e) {
            System.out.println("End of the category!");
        }

        System.out.println("end =============");
    }

    private static void testSetCategory(EntityManager em) {
        System.out.println("start =============");

        CategoryDao categoryDAO = new CategoryDaoImpl();
        categoryDAO.setEntityManager(em);

        Category parentCategory = categoryDAO.findById(7L);
        Category category = new Category("trostomaat-final", parentCategory);

        em.getTransaction().begin();
        em.persist(category);
        em.getTransaction().commit();

//        em.getTransaction().begin();
//        Category parentCategory = em.find(Category.class, 7L);
//        em.getTransaction().commit();
//
//        Category category = new Category("trostomaat", parentCategory);
//
//        em.getTransaction().begin();
//        em.persist(category);
//        em.getTransaction().commit();

        System.out.println("end =============");
    }
    
}

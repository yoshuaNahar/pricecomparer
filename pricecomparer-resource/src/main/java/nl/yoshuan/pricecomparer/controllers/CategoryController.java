package nl.yoshuan.pricecomparer.controllers;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("")
    public Category getCategoryByName(@RequestParam("categoryName") String categoryName) {
        if (categoryName == null) {
            return null;
        }

        return categoryService.getCategoryByName(categoryName);
    }

    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsByCategoryId(@PathVariable("categoryId") Long categoryId,
                                                 @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        return categoryService.getProductsByCategoryId(categoryId, pageNumber);
    }

    @GetMapping("/products")
    public List<Product> getProductsByCategoryName(@RequestParam("categoryName") String categoryName,
                                                   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        if (categoryName == null) {
            System.out.println("here");
            return null;
        }

        return categoryService.getProductsByCategoryName(categoryName, pageNumber);
    }

}

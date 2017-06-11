package nl.yoshuan.pricecomparer.controllers;

import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // https://stackoverflow.com/questions/35155916/handling-ambiguous-handler-methods-mapped-in-rest-application-with-spring
    // Cant differentiate between Long and String
    @GetMapping("/{productId}") // /products/1
    public Product getProductById(@PathVariable("productId") Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("") // /products?productName=AH Rivolo cherrytomaat
    public Product getProductByName(@RequestParam("productName") String productName) {
        return productService.getProductByName(productName);
    }

}

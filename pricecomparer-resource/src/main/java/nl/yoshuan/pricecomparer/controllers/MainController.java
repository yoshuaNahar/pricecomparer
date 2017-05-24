package nl.yoshuan.pricecomparer.controllers;

import nl.yoshuan.pricecomparer.Application;
import nl.yoshuan.pricecomparer.util.EntityManagerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class MainController {
    
    @GetMapping
    @ResponseBody
    public void doSomething() {
        Application.dbLogic(EntityManagerFactory.createEntityManager());
    }
    
    @GetMapping("/hello")
    @ResponseBody
    public String doSomethingElse() {
        return "something";
    }
    
}
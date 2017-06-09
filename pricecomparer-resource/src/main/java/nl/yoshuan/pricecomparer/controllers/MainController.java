package nl.yoshuan.pricecomparer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class MainController {

    @GetMapping("/hello")
    @ResponseBody
    public String doSomethingElse() {
        return "something";
    }
    
}

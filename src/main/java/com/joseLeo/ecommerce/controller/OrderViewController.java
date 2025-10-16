package com.joseLeo.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*/Esto es solo para poder trabajar con order.html en la carpeta templates ya que todo esta esn static¨*/
@Controller
public class OrderViewController {

    @GetMapping("/order")
    public String showOrderPage() {
        return "order"; // Spring busca src/main/resources/templates/order.html
    }
}


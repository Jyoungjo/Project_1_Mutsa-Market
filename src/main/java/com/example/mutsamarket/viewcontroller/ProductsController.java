package com.example.mutsamarket.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductsController {
    @GetMapping
    public String itemsPage() {
        return "products-main";
    }

    @GetMapping("/new")
    public String addItem() {
        return "add-product";
    }

    @GetMapping("/{productId}")
    public String itemPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "product";
    }

    @GetMapping("/{productId}/update")
    public String updatePage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "update-product";
    }

    @GetMapping("/{productId}/image")
    public String updateImage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "update-image";
    }
}

package com.example.mutsamarket.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PostMapping("/new")
    public String addItemPost() {
        return "redirect:/products";
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

    @GetMapping("/{productId}/update/image")
    public String updateImage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "update-product-image";
    }
}

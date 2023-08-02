package com.example.mutsamarket.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/suggestions")
public class SuggestionController {

    @GetMapping("/{productId}")
    public String suggestionPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "suggestion";
    }

    @GetMapping("/new/{productId}")
    public String addItem(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "add-suggestion";
    }
}

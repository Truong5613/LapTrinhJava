package com.example.Demo4.Controller;

import com.example.Demo4.models.Product;
import com.example.Demo4.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listproduct", productService.GetAll());
        return "products/index";
    }

    @GetMapping("/create")
    public String create (Model model) {
        model.addAttribute("product", new Product());
        return "products/create";
    }

    @PostMapping("/create")
    public String create (@Valid Product newProduct, @RequestParam MultipartFile imageProduct, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "products/create";
        }
        //save image to static/images folder
        if (imageProduct != null && imageProduct.getSize() > 0) {
            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                String newImageFile = UUID.randomUUID() + ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Product editproduct = productService.get(id);
        model.addAttribute("product", editproduct);
        return "products/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, @Valid Product editProduct, @RequestParam MultipartFile imageProduct, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            return "products/edit";
        }

        if (imageProduct != null && imageProduct.getSize() > 0) {
            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                String newImageFile = UUID.randomUUID() + ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                editProduct.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        productService.edit(editProduct);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        productService.delete(id);
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("listproduct", productService.search(keyword));
        return "products/index";
    }
}

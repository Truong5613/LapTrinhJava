package com.example.Demo4.services;

import com.example.Demo4.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private List<Product> listProduct= new ArrayList<>();
    public ProductService() {
    }
    // this.listProduct.add(new Product(1, "Sản phẩm 1", "1.jpg", 29312));
    // this.listProduct.add(new Product (2, "San pham 2", "2.jpg", 124246));
    public void add(Product newProduct) {
        int maxid = listProduct.stream().mapToInt(Product::getId).max().orElse(0);
        newProduct.setId(maxid+1);
        listProduct.add(newProduct);
    }

    public List<Product> GetAll() { return listProduct; }

    public Product get(int id) {
        return listProduct.stream().filter(p->p.getId() == id).findFirst().orElse( null);
    }
    public void edit (Product editProduct) {
        Product find = get(editProduct.getId());
        if(find!=null){
            find.setName(editProduct.getName());
            find.setImage(editProduct.getImage());
            find.setPrice(editProduct.getPrice());

        }
    }
    public void delete (int id) {
        Product find = listProduct.get(id-1);
        if(find!=null)
            listProduct.remove(find);
    }
    public List<Product> search(String keyword) {
        return listProduct.stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}

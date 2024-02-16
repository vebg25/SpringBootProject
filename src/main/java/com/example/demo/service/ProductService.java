package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Product;

public interface ProductService{
	public Product saveProduct(Product product);
	public List<Product> showProducts();
	public Product findById(Long id);
	public void deleteById(Long id);
}

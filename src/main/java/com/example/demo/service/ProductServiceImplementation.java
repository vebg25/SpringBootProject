package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductServiceImplementation implements ProductService{
	
	@Autowired
	ProductRepository productRepo;
	
	@Override
	public Product saveProduct(Product product) {
		return productRepo.save(product);
	}
	
	@Override
	public List<Product> showProducts() {
		return productRepo.findAll();
	}

	@Override
	public Product findById(Long id) {
		// TODO Auto-generated method stub
		return productRepo.findById(id).get();
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		 productRepo.deleteById(id);
	}
}

package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Cart;
import com.example.demo.repository.CartRepository;

public interface CartService{
	public Cart cartSave(Cart cart);
	public List<Cart> showCart();
	public void deleteById(Long id);
	public void deleteAll();
	
}

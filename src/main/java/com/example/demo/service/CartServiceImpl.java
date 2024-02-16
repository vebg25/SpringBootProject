package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cart;
import com.example.demo.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart cartSave(Cart cart){
		return cartRepository.save(cart);
	}

	@Override
	public List<Cart> showCart() {
		// TODO Auto-generated method stub
		return cartRepository.findAll();
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		cartRepository.deleteAll();
	}
}
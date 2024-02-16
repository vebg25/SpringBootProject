package com.example.demo.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayService {
	@Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    public Order createOrder(int amount) throws RazorpayException {
    	RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

    	JSONObject orderRequest = new JSONObject();
    	orderRequest.put("amount", amount*100); // amount in the smallest currency unit
    	orderRequest.put("currency", "INR");
    	
    	Order order = razorpay.orders.create(orderRequest);
    	System.out.println(order);
    	return order;
    }
}



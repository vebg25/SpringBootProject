package com.example.demo.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import com.example.demo.service.RazorpayService;
import com.example.demo.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	ProductService productService;
	@Autowired
	CartService cartService;
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/home")
	public String getHome() {
		return "home";
	}
	@PostMapping("/home")
	public String getBackHome() {
		cartService.deleteAll();
		return "home";
	}
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user",new User());
		return "register";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@PostMapping("/saveUser")
	public String saveUser(User user) {
		userService.saveUser(user);
		return "redirect:/login";
	}
	@GetMapping("/user/profile")
	public String profile(Principal p,Model model) {
		String email=p.getName();
		User user=userRepository.findByEmail(email);
		String firstName=user.getFirstName();
		String lastName=user.getLastName();
		String fullName=",  "+firstName+" "+lastName;
		model.addAttribute("fullName",fullName);
		return "profile";
	}
	@GetMapping("/user/product/add")
	public String addProductGet(Model model) {
		model.addAttribute("product",new ProductDTO());
		return "addProduct";
	}
	@PostMapping("/user/product/productDisplay")
	public String addProductPost(@ModelAttribute ProductDTO prod){
		Product product=new Product(prod.getName(),prod.getDescription(),prod.getPrice());
		MultipartFile image=prod.getImageFile();
		Date createdAt=new Date();
		String storageFileName=createdAt.getTime()+"_"+image.getOriginalFilename();
		try {
			String uploadDir="public/images/";
			Path uploadPath=Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try(InputStream inputStream=image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		product.setImageFile(storageFileName);
		System.out.println(product);
		productService.saveProduct(product);
		return "redirect:/updateProducts";
	}

	@GetMapping("/updateProducts")
	public String updateProducts(Model model) {
		model.addAttribute("product",productService.showProducts());
		return "showProducts";
	}
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id){
		productService.deleteById(id);
		return "redirect:/updateProducts";
	}
	@GetMapping("/update/{id}")
	public String updateUser(@PathVariable Long id,Model model){
		model.addAttribute("id",id);
		model.addAttribute("product",new ProductDTO());
		return "editProduct";
	}
	@PostMapping("/edit/{id}")
	public String updateDone(@PathVariable Long id,@ModelAttribute("product") ProductDTO productDto){
		Product product=new Product(productDto.getName(),productDto.getDescription(),productDto.getPrice());
		product.setId(id);
		MultipartFile image=productDto.getImageFile();
		Date createdAt=new Date();
		String storageFileName=createdAt.getTime()+"_"+image.getOriginalFilename();
		try {
			String uploadDir="public/images/";
			Path uploadPath=Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try(InputStream inputStream=image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		product.setImageFile(storageFileName);
		
		productService.saveProduct(product);
		
		return "redirect:/updateProducts";
	}
	@GetMapping("/card")
	public String card(Model model) {
		model.addAttribute("product",productService.showProducts());
		return "cardLayout";
	}
	@GetMapping("/cart")
	public String cart(Model model){
		List<Cart> list=cartService.showCart();
		double a=calculateTotalAmount(list);
		model.addAttribute("showProducts",cartService.showCart());
		model.addAttribute("bill",a);
		
		return "cart";
	}
	@GetMapping("/cart/add/{id}")
	public String addToCart(@PathVariable Long id,Model model){
		Product product=productService.findById(id);
		Cart cart=new Cart(product.getName(),product.getDescription(),product.getPrice(),product.getImageFile());
		cart.setId(id);
		cartService.cartSave(cart);
		return "redirect:/card";
	}
	@GetMapping("/delete/cart/{id}")
	public String deleteFromCart(@PathVariable Long id){
		cartService.deleteById(id);
		return "redirect:/cart";
	}
	private double calculateTotalAmount(List<Cart>items) {
        return items.stream()
                .mapToDouble(Cart::getPrice)
                .sum();
    }
	@Autowired
    private RazorpayService razorpayService;

    @PostMapping("/checkout")
    public String checkout(Model model,@RequestParam("bill") double bill) {
        try {
        	int finalBill=(int) bill;
            Order order = razorpayService.createOrder(finalBill); // Replace with the actual amount
     
            model.addAttribute("orderId", order.get("id"));
            model.addAttribute("amount", order.get("amount"));
            model.addAttribute("currency", order.get("currency"));
            
         System.out.println("Razorpay Order Response: " + order);
//            System.out.println("Order ID: " + order.get("id"));
//            System.out.println("Amount: " + order.get("amount"));
//            System.out.println("Currency: " + order.get("currency"));
 
            return "checkout";
        } catch (RazorpayException e){
            // Handle exception
            return "error";
        }	
    }
    @GetMapping("/cancelPayment")
    public String cancel() {
    	return "/cart";
    }
    @GetMapping("/contactUs")
    public String contact() {
    	return "contact";
    }
    @GetMapping("/about")
    public String about() {
    	return "about";
    }
}

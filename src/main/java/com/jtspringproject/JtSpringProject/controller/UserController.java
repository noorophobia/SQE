package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.jtspringproject.JtSpringProject.services.cartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.cartService;



@Controller
public class UserController{

	@Autowired
	private userService userService;

	@Autowired
	private productService productService;

	@Autowired
	private cartService cartService;


	@GetMapping("/register")
	public String registerUser()
	{
		return "register";
	}

	@GetMapping("/buy")
	public String buy()
	{
		return "buy";
	}


	@GetMapping("/")
	public String userlogin(Model model) {
		return "userLogin";
	}

	@GetMapping("/userLogin")
	public String userloginRedirect(Model model) {
		return "userLogin";
	}

	@GetMapping("/updateProfile")
	public String updateProfle() {
		return "updateProfile";
	}

	@GetMapping("/displayProfile")
	public String updateProfile(Model model) {
		// Retrieve the current logged-in user's data (adjust this based on your authentication mechanism)
		User user = this.userService.getCurrentUser();
		model.addAttribute("username", user.getUsername());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("address", user.getAddress());

		return "displayProfile";
	}

	@PostMapping("/updateuser")
	public String updateUser( @RequestParam("username") String username,
							  @RequestParam("password") String pass,
							  @RequestParam("email") String email,
							  @RequestParam("address") String address) {
		User updatedUser = new User();
		updatedUser.setUsername(username);
		updatedUser.setPassword(pass);
		updatedUser.setEmail(email);
		updatedUser.setAddress(address);

		this.userService.updateUserProfile(updatedUser);
		return "redirect:/displayProfile";
	}


	@RequestMapping(value = "userloginvalidate", method = RequestMethod.POST)
	public ModelAndView userlogin( @RequestParam("username") String username, @RequestParam("password") String pass,Model model,HttpServletResponse res) {
		User u = this.userService.checkLogin(username, pass);
		if(u != null && u.getUsername() != null && u.getUsername().equals(username)) {
			res.addCookie(new Cookie("username", u.getUsername()));
			res.addCookie(new Cookie("cutomerid", String.valueOf(u.getId())));
			ModelAndView mView  = new ModelAndView("index");
			mView.addObject("user", u);
			List<Product> products = this.productService.getProducts();

			if (products.isEmpty()) {
				mView.addObject("msg", "No products are available");
			} else {
				mView.addObject("products", products);
			}
			return mView;

		}else {
			ModelAndView mView = new ModelAndView("userLogin");
			mView.addObject("msg", "Please enter correct email and password");
			return mView;
		}

	}

	@GetMapping("/user/products")
	public ModelAndView getproduct() {
		ModelAndView mView = new ModelAndView("uproduct");
		List<Product> products = this.productService.getProducts();
		if(products.isEmpty()) {
			mView.addObject("msg","No products are available");
		}else {
			mView.addObject("products",products);
		}
		return mView;
	}
	@RequestMapping(value = "newuserregister", method = RequestMethod.POST)
	public String newUseRegister(@ModelAttribute User user)
	{
		System.out.println(user.getEmail());
		user.setRole("ROLE_NORMAL");
		this.userService.addUser(user);

		return "redirect:/";
	}



	   //for Learning purpose of model
		@GetMapping("/test")
		public String Test(Model model)
		{
			System.out.println("test page");
			model.addAttribute("author","jay gajera");
			model.addAttribute("id",40);

			List<String> friends = new ArrayList<String>();
			model.addAttribute("f",friends);
			friends.add("xyz");
			friends.add("abc");

			return "test";
		}

		// for learning purpose of model and view ( how data is pass to view)

		@GetMapping("/test2")
		public ModelAndView Test2()
		{
			System.out.println("test page");
			//create modelandview object
			ModelAndView mv=new ModelAndView();
			mv.addObject("name","jay gajera 17");
			mv.addObject("id",40);
			mv.setViewName("test2");

			List<Integer> list=new ArrayList<Integer>();
			list.add(10);
			list.add(25);
			mv.addObject("marks",list);
			return mv;


		}

	@GetMapping("/cartproduct")
	public ModelAndView  getCartDetail()
	{
		ModelAndView mv= new ModelAndView();
		List<Cart>carts = cartService.getCarts();
		return mv;

	}


	@GetMapping("/logout")
	public String userlogout(Model model) {
		return "redirect:/userLogin";
	}

	@GetMapping("/userhome")
	public String userhome(Model model) {
		return "redirect:/index";
	}
	@PostMapping("/products/addtocart")
	public String addToCart(@RequestParam("id") int productId) {
		User user = this.userService.getCurrentUser();
		Cart cart = cartService.getCart();
		Product product = this.productService.getProduct(productId);
		boolean check = false;

		if(cart == null){
			cart = cartService.createCart();
			check = true;
		}
		if (cart.getCustomer() == null) {
			cart.setCustomer(user);
		}

		cart.addProduct(product);

		if (check == true) {
			cartService.addCart(cart);
			check = false;
		} else {
			cartService.updateCart(cart);
		}

		return "redirect:/cartproduct";
	}


	@PostMapping("/cart/delete")
	public String deleteProductFromCart(@RequestParam("id") int productId) {
		User user = this.userService.getCurrentUser();
		Cart cart = cartService.getCart();

		Product productToRemove = null;
		for (Product product : cart.getProducts()) {
			if (product.getId() == productId) {
				productToRemove = product;
				break;
			}
		}

		if (productToRemove != null) {
			cart.removeProduct(productToRemove);
			cartService.updateCart(cart);
		}

		return "redirect:/cartproduct";
	}

}

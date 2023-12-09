package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.controller.AdminController;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.categoryService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class adminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private userService userService;

    @Mock
    private categoryService categoryService;

    @Mock
    private productService productService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        adminController = new AdminController();
        userService = mock(userService.class);
        MockitoAnnotations.openMocks(this);
        model = mock(Model.class);
        adminController.setAdminlogcheck(1); // Assuming the admin is logged in for these tests
        adminController.setUsernameforclass("admin"); // Set a dummy username

        productService = mock(productService.class);
        userService = mock(userService.class);
        categoryService = mock(categoryService.class);
        model = mock(Model.class);

        productService = mock(productService.class);
        when(productService.getProducts()).thenReturn(new ArrayList<>());

        MockitoAnnotations.openMocks(this);

        adminController.setAdminlogcheck(1); // Assuming the admin is logged in for these tests
        adminController.setUsernameforclass("admin");
    }

    @Test
    public void testIndexWithEmptyUsername() {
        // Arrange
        adminController.setUsernameforclass(""); // Set an empty username

        // Act
        String result = adminController.index(model);

        // Assert
        assertEquals("userLogin", result);
    }

    @Test
    public void testIndexWithNonEmptyUsername() {
        adminController.setUsernameforclass("admin");
        String result = adminController.index(model);
        assertEquals("index", result);
    }

    @Test
    public void testAdminHome() {
        String result = adminController.adminHome();
        assertEquals("adminHome", result);
    }

    @Test
    public void testAdminHomeWithInvalidLogin() {
        adminController.setAdminlogcheck(0);
        String result = adminController.adminHome(model);
        assertEquals("redirect:/admin/login", result);
    }

    @Test

    public void testAdminHomeWithValidLogin() {
        adminController.setAdminlogcheck(1);
        String result = adminController.adminHome(model);
        assertEquals("adminHome", result);
    }

    @Test
    public void testAdminloginWithCorrectCredentials() {
        User mockUser = new User();
        mockUser.setRole("ROLE_ADMIN");
        when(userService.checkLogin(anyString(), anyString())).thenReturn(mockUser);

        ModelAndView modelAndView = adminController.adminlogin("admin", "123");

        assertEquals("adminHome", modelAndView.getViewName());
        assertEquals(mockUser, modelAndView.getModel().get("admin"));
        assertEquals(1, adminController.getAdminlogcheck());
    }


    @Test
    public void testAdminloginWithIncorrectCredentials() {
        // Arrange
        when(userService.checkLogin(anyString(), anyString())).thenReturn(null);

        // Act
        ModelAndView modelAndView = adminController.adminlogin("admin", "password");

        // Assert
        assertEquals("adminlogin", modelAndView.getViewName());
        assertEquals("Please enter correct username and password", modelAndView.getModel().get("msg"));
        assertEquals(1, adminController.getAdminlogcheck());
        // assertEquals("", adminController.getUsernameforclass(), "setUsernameforclass should be an empty string");
    }

    // Helper methods to set private fields for testing
    private void setUsernameforclass(String usernameforclass) {
        try {
            MockitoAnnotations.openMocks(this);
            Mockito.when(model.addAttribute(anyString(), any())).thenReturn(model);
            adminController.setUsernameforclass(usernameforclass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProduct() {
        List<Product> mockProducts = new ArrayList<>();
        when(productService.getProducts()).thenReturn(mockProducts);

        ModelAndView result = adminController.getproduct();


        assertEquals("products", result.getViewName());


    }




    @Test
    public void testAddProductToCart() {
        int productId = 1;
        Product mockProduct = new Product();
        when(productService.getProduct(productId)).thenReturn(mockProduct);

        ModelAndView mockModelAndView = new ModelAndView("productsAdd");
        List<Category> mockCategories = new ArrayList<>();
        when(categoryService.getCategories()).thenReturn(mockCategories);

        ModelAndView result = adminController.addProduct();

        assertEquals("productsAdd", result.getViewName());
        assertEquals(mockCategories, result.getModel().get("categories"));
    }


    @Test
    public void testRemoveProductFromCart() {
        // Arrange
        int productId = 1; // Specify a product ID for testing
        Product mockProduct = new Product();
        when(productService.getProduct(productId)).thenReturn(mockProduct);

        // Act
        String redirectURL = adminController.removeProduct(productId);

        // Assert
        assertEquals("redirect:/admin/products", redirectURL);
        // Additional assertions based on your implementation, e.g., checking the cart content
    }

    private void setAdminlogcheck(int adminlogcheck) {
        try {
            MockitoAnnotations.openMocks(this);
            adminController.setAdminlogcheck(adminlogcheck);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetCategoryWithAdminLoggedIn() {
        // Arrange
        List<Category> mockCategories = Collections.singletonList(new Category());
        when(categoryService.getCategories()).thenReturn(mockCategories);

        // Act
        ModelAndView result = adminController.getcategory();

        // Assert
        assertEquals("categories", result.getViewName());
        assertEquals(mockCategories, result.getModel().get("categories"));
    }

    @Test
    public void testGetCategoryWithAdminNotLoggedIn() {
        // Arrange
        adminController.setAdminlogcheck(0);

        // Act
        ModelAndView result = adminController.getcategory();

        // Assert
        assertEquals("adminlogin", result.getViewName());
    }
    @Test
    public void testAddCategory() {
         String categoryName = "Test Category";

         String redirectURL = adminController.addCategory(categoryName);
System.out.println(redirectURL);
         assertEquals("redirect:categories", redirectURL);
    }


    @Test
    public void testRemoveCategory() {
        // Arrange
        int categoryId = 1; // Specify a category ID for testing
        when(categoryService.deleteCategory(categoryId)).thenReturn(true);

        // Act
        String redirectURL = adminController.removeCategoryDb(categoryId);

        // Assert
        assertEquals("redirect:/admin/categories", redirectURL);
    }

    @Test
    public void testUpdateCategory() {
        // Arrange
        int categoryId = 1;
        String categoryName = "Updated Category";
        Category mockCategory = new Category();
        when(categoryService.updateCategory(categoryId, categoryName)).thenReturn(mockCategory);

        // Act
        String redirectURL = adminController.updateCategory(categoryId, categoryName);

        // Assert
        assertEquals("redirect:/admin/categories", redirectURL);
    }
}

package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.controller.UserController;
import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.cartService;
import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.productService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class userControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private userService userService;

    @Mock
    private productService productService;

    @Mock
    private cartService cartService;

    @Mock
    private Model model;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUserLoginWithCorrectCredentials() {
        User mockUser = new User();
        mockUser.setUsername("testUser");
        when(userService.checkLogin(anyString(), anyString())).thenReturn(mockUser);

        ModelAndView modelAndView = userController.userlogin("testUser", "password", model, response);

        assertEquals("index", modelAndView.getViewName());
        assertEquals(mockUser, modelAndView.getModel().get("user"));
    }

    @Test
    public void testUserLoginWithIncorrectCredentials() {
        when(userService.checkLogin(anyString(), anyString())).thenReturn(null);

        ModelAndView modelAndView = userController.userlogin("invalidUser", "invalidPassword", model, response);

        assertEquals("userLogin", modelAndView.getViewName());
        assertEquals("Please enter correct email and password", modelAndView.getModel().get("msg"));
    }

    @Test
    public void testUpdateUser() {
        //
        String username = "lisa";
        String password = "newPassword";
        String email = "newEmail@test.com";
        String address = "newAddress";

        User mockUser = new User();
        mockUser.setUsername(username);

        lenient().when(userService.getCurrentUser()).thenReturn(mockUser);

        userController.updateUser(username, password, email, address);

        User updatedUser = new User();
        updatedUser.setUsername(username);
        updatedUser.setPassword(password);
        updatedUser.setEmail(email);
        updatedUser.setAddress(address);

        verify(userService, times(1)).updateUserProfile(any(User.class));
    }


    @Captor
    private ArgumentCaptor<Cart> cartCaptor;

    @Test
    public void testAddToCart() {
        int productId = 1;
        User mockUser = new User();
        Product mockProduct = new Product();
        Cart mockCart = new Cart();

        when(userService.getCurrentUser()).thenReturn(mockUser);


        when(productService.getProduct(productId)).thenReturn(mockProduct);

        when(cartService.getCart()).thenReturn(mockCart);

        String result = userController.addToCart(productId);

        verify(cartService, times(1)).updateCart(cartCaptor.capture());

        Cart capturedCart = cartCaptor.getValue();
        assertNotNull(capturedCart);
        assertEquals(mockUser, capturedCart.getCustomer());
        assertTrue(capturedCart.getProducts().contains(mockProduct));


        assertEquals("redirect:/cartproduct", result);
    }



    public void testDeleteProductFromCart() {
        int productId = 1;
        User mockUser = new User();
        Cart mockCart = new Cart();
        Product mockProductToRemove = new Product();

        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(cartService.getCart()).thenReturn(mockCart);
        doNothing().when(cartService).updateCart(any()); // Use doNothing() for void methods
        when(productService.getProduct(productId)).thenReturn(mockProductToRemove);

        String result = userController.deleteProductFromCart(productId);

        verify(cartService, times(1)).updateCart(any()); // Verify the updateCart method was called
        assert result.equals("redirect:/cartproduct");
    }


    @Test
    public void testGetCartDetail() {
        ModelAndView expectedModelAndView = new ModelAndView();
        List<Cart> mockCarts = new ArrayList<>();

        when(cartService.getCarts()).thenReturn(mockCarts);

        ModelAndView actualModelAndView = userController.getCartDetail();

        verify(cartService, times(1)).getCarts();

        ModelAndViewAssertions.assertModelAndViewEquals(expectedModelAndView, actualModelAndView);
    }


    @Test
    public void testGetProduct() {
        ModelAndView expectedModelAndView = new ModelAndView("uproduct");
        List<Product> mockProducts = new ArrayList<>();

        when(productService.getProducts()).thenReturn(mockProducts);

        ModelAndView result = userController.getproduct();

        verify(productService, times(1)).getProducts();

        assertEquals(expectedModelAndView.getViewName(), result.getViewName());

        System.out.println("Model attributes: " + result.getModel());

    }




}
class ModelAndViewAssertions {

    public static void assertModelAndViewEquals(ModelAndView expected, ModelAndView actual) {
        assertEquals(expected.getView(), actual.getView());
        assertEquals(expected.getModel(), actual.getModel());
    }
}



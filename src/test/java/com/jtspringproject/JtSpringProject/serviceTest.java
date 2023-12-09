package com.jtspringproject.JtSpringProject;


import com.jtspringproject.JtSpringProject.dao.*;
import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.services.*;
        import com.jtspringproject.JtSpringProject.models.User;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;

        import java.util.List;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
        import static org.mockito.Mockito.*;

class serviceTest {

    @InjectMocks
    private userService userService;

    @Mock
    private userDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsers() {
        List<User> userList = List.of(new User(), new User());

        when(userDao.getAllUser()).thenReturn(userList);

        List<User> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        verify(userDao, times(1)).getAllUser();
    }

    @Test
    void testAddUser() {
        User user = new User();
        when(userDao.saveUser(any(User.class))).thenReturn(user);

        User newUser = userService.addUser(user);

        assertNotNull(newUser);
        assertEquals(user, newUser);
        assertEquals(user, userService.getCurrentUser());
        verify(userDao, times(1)).saveUser(any(User.class));
    }

    @Test
    void testCheckLogin() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User();
        when(userDao.getUser(username, password)).thenReturn(user);

        User loggedInUser = userService.checkLogin(username, password);

        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
        assertEquals(user, userService.getCurrentUser());
        verify(userDao, times(1)).getUser(username, password);
    }


}


class CartServiceTest {

    @InjectMocks
    private cartService cartService;

    @Mock
    private cartDao cartDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddCart() {
        Cart cart = new Cart();
        when(cartDao.addCart(any(Cart.class))).thenReturn(cart);

        Cart addedCart = cartService.addCart(cart);

        assertNotNull(addedCart);
        assertEquals(cart, addedCart);
        verify(cartDao, times(1)).addCart(any(Cart.class));
    }

    @Test
    void testGetCarts() {
        List<Cart> cartList = List.of(new Cart(), new Cart());
        when(cartDao.getCarts()).thenReturn(cartList);

        List<Cart> result = cartService.getCarts();

        assertNotNull(result);
        assertEquals(cartList.size(), result.size());
        verify(cartDao, times(1)).getCarts();
    }

    @Test
    void testUpdateCart() {
        Cart cart = new Cart();
        doNothing().when(cartDao).updateCart(any(Cart.class));

        cartService.updateCart(cart);

        verify(cartDao, times(1)).updateCart(any(Cart.class));
    }

    @Test
    void testDeleteCart() {
        Cart cart = new Cart();
        doNothing().when(cartDao).deleteCart(any(Cart.class));

        cartService.deleteCart(cart);

        verify(cartDao, times(1)).deleteCart(any(Cart.class));
    }

    @Test
    void testCreateCart() {
        Cart createdCart = cartService.createCart();

        assertNotNull(createdCart);
        assertEquals(createdCart, cartService.getCart());
    }
}

class ProductServiceTest {

    @InjectMocks
    private productService productService;

    @Mock
    private productDao productDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetProducts() {
        List<Product> productList = List.of(new Product(), new Product());
        when(productDao.getProducts()).thenReturn(productList);

        List<Product> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(productList.size(), result.size());
        verify(productDao, times(1)).getProducts();
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        when(productDao.addProduct(any(Product.class))).thenReturn(product);

        Product addedProduct = productService.addProduct(product);

        assertNotNull(addedProduct);
        assertEquals(product, addedProduct);
        verify(productDao, times(1)).addProduct(any(Product.class));
    }

    @Test
    void testGetProduct() {
        int productId = 1;
        Product product = new Product();
        when(productDao.getProduct(productId)).thenReturn(product);

        Product result = productService.getProduct(productId);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productDao, times(1)).getProduct(productId);
    }

    @Test
    void testUpdateProduct() {
        int productId = 1;
        Product product = new Product();
        when(productDao.updateProduct(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(productId, product);

        assertNotNull(updatedProduct);
        assertEquals(product, updatedProduct);
        assertEquals(productId, updatedProduct.getId());
        verify(productDao, times(1)).updateProduct(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        int productId = 1;
        when(productDao.deletProduct(productId)).thenReturn(true);

        boolean result = productService.deleteProduct(productId);

        assertTrue(result);
        verify(productDao, times(1)).deletProduct(productId);
    }
}



class CategoryServiceTest {

    @InjectMocks
    private categoryService categoryService;

    @Mock
    private categoryDao categoryDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddCategory() {
        String categoryName = "Test Category";
        Category category = new Category();
        when(categoryDao.addCategory(categoryName)).thenReturn(category);

        Category addedCategory = categoryService.addCategory(categoryName);

        assertNotNull(addedCategory);
        assertEquals(category, addedCategory);
        verify(categoryDao, times(1)).addCategory(categoryName);
    }

    @Test
    void testGetCategories() {
        List<Category> categoryList = List.of(new Category(), new Category());
        when(categoryDao.getCategories()).thenReturn(categoryList);

        List<Category> result = categoryService.getCategories();

        assertNotNull(result);
        assertEquals(categoryList.size(), result.size());
        verify(categoryDao, times(1)).getCategories();
    }

    @Test
    void testDeleteCategory() {
        int categoryId = 1;
        when(categoryDao.deleteCategory(categoryId)).thenReturn(true);

        boolean result = categoryService.deleteCategory(categoryId);

        assertTrue(result);
        verify(categoryDao, times(1)).deleteCategory(categoryId);
    }

    @Test
    void testUpdateCategory() {
        int categoryId = 1;
        String updatedName = "Updated Category";
        Category category = new Category();
        when(categoryDao.updateCategory(categoryId, updatedName)).thenReturn(category);

        Category updatedCategory = categoryService.updateCategory(categoryId, updatedName);

        assertNotNull(updatedCategory);
        assertEquals(category, updatedCategory);
        verify(categoryDao, times(1)).updateCategory(categoryId, updatedName);
    }

    @Test
    void testGetCategory() {
        int categoryId = 1;
        Category category = new Category();
        when(categoryDao.getCategory(categoryId)).thenReturn(category);

        Category result = categoryService.getCategory(categoryId);

        assertNotNull(result);
        assertEquals(category, result);
        verify(categoryDao, times(1)).getCategory(categoryId);
    }
}

package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.dao.*;
import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DaoTest {

    @InjectMocks
    private cartDao cartDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCart() {
        Cart cart = new Cart();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.save(any(Cart.class))).thenReturn(1);

        Cart savedCart = cartDao.addCart(cart);

        assertNotNull(savedCart);
        verify(session, times(1)).save(cart);
    }


    @Test
    public void testUpdateCart() {
        Cart cart = new Cart();

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        cartDao.updateCart(cart);

        verify(session, times(1)).update(cart);
    }

    @Test
    public void testDeleteCart() {
        Cart cart = new Cart();

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        cartDao.deleteCart(cart);

        verify(session, times(1)).delete(cart);
    }

    @SuppressWarnings("unchecked")
    private org.hibernate.query.Query<Cart> mockQuery() {
        org.hibernate.query.Query<Cart> query = mock(org.hibernate.query.Query.class);
        when(query.list()).thenReturn(List.of(new Cart(), new Cart()));
        return query;
    }


}
    class CategoryDaoTest {

        @InjectMocks
        private categoryDao categoryDao;

        @Mock
        private SessionFactory sessionFactory;

        @Mock
        private Session session;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        void testAddCategory() {
            String categoryName = "Test Category";
            Category category = new Category();
            category.setName(categoryName);

            when(sessionFactory.getCurrentSession()).thenReturn(session);
            doNothing().when(session).saveOrUpdate(any(Category.class));
 
             Category savedCategory = categoryDao.addCategory(categoryName);

            assertNotNull(savedCategory);
            assertEquals(categoryName, savedCategory.getName());
            verify(session, times(1)).saveOrUpdate(any(Category.class));
        }



        @Test
        void testDeleteCategory() {
            int categoryId = 1;

            when(sessionFactory.getCurrentSession()).thenReturn(session);
            when(session.load(Category.class, categoryId)).thenReturn(new Category());

            boolean result = categoryDao.deleteCategory(categoryId);

            assertTrue(result);
            verify(session, times(1)).delete(any());
        }

        @Test
        void testUpdateCategory() {
            int categoryId = 1;
            String updatedName = "Updated Category";

            Category category = new Category();
            category.setId(categoryId);

            when(sessionFactory.getCurrentSession()).thenReturn(session);
            when(session.get(Category.class, categoryId)).thenReturn(category);
            doNothing().when(session).update(any(Category.class));

            Category updatedCategory = categoryDao.updateCategory(categoryId, updatedName);

            assertNotNull(updatedCategory);
            assertEquals(updatedName, updatedCategory.getName());
            verify(session, times(1)).update(any(Category.class));
        }

        @Test
        void testGetCategory() {
            int categoryId = 1;
            Category category = new Category();

            when(sessionFactory.getCurrentSession()).thenReturn(session);
            when(session.get(Category.class, categoryId)).thenReturn(category);

            Category result = categoryDao.getCategory(categoryId);

            assertNotNull(result);
            assertEquals(category, result);
        }

        // Helper method to mock Query
        @SuppressWarnings("unchecked")
        private org.hibernate.query.Query<Category> mockQuery() {
            Query mock = mock(Query.class);
            return mock;
        }}

class ProductDaoTest {

    @InjectMocks
    private productDao productDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }





    @Test
    void testGetProduct() {
        int productId = 1;
        Product product = new Product();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Product.class, productId)).thenReturn(product);

        Product result = productDao.getProduct(productId);

        assertNotNull(result);
        assertEquals(product, result);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        doNothing().when(session).update(product);

        Product updatedProduct = productDao.updateProduct(product);

        assertNotNull(updatedProduct);
        verify(session, times(1)).update(product);
    }

    @Test
    void testDeleteProduct() {
        int productId = 1;

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.load(Product.class, productId)).thenReturn(new Product());

        boolean result = productDao.deletProduct(productId);

        assertTrue(result);
        verify(session, times(1)).delete(any());
    }

    // Helper method to mock Query
    @SuppressWarnings("unchecked")
    private Query<Product> mockQuery() {
        return (Query<Product>) mock(Query.class);
    }
}

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @InjectMocks
    private userDao userDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<User> query;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        doNothing().when(session).saveOrUpdate(user);

        User savedUser = userDao.saveUser(user);

        assertNotNull(savedUser);
        verify(session, times(1)).saveOrUpdate(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(user, capturedUser);
    }









}

class ProductTest {

    @Test
    void testProductSettersAndGetters() {
        Product product = new Product();
        product.setId(1);
        product.setName("TestProduct");
        product.setImage("test_image.jpg");
        product.setCategory(new Category());
        product.setQuantity(10);
        product.setPrice(50);
        product.setWeight(5);
        product.setDescription("Test description");

        assertEquals(1, product.getId());
        assertEquals("TestProduct", product.getName());
        assertEquals("test_image.jpg", product.getImage());
        assertNotNull(product.getCategory());
        assertEquals(10, product.getQuantity());
        assertEquals(50, product.getPrice());
        assertEquals(5, product.getWeight());
        assertEquals("Test description", product.getDescription());
     }



 }


class CartTest {

    @Test
    void testCartSettersAndGetters() {
        Cart cart = new Cart();
        cart.setId(1);
        cart.setCustomer(new User());
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        cart.setProducts(products);

        assertEquals(1, cart.getId());
        assertNotNull(cart.getCustomer());
        assertEquals(products, cart.getProducts());
    }

    @Test
    void testAddProductToCart() {
        Cart cart = new Cart();
        Product product = new Product();
        cart.addProduct(product);

        assertTrue(cart.getProducts().contains(product));
    }

    @Test
    void testRemoveProductFromCart() {
        Cart cart = new Cart();
        Product product = new Product();
        cart.addProduct(product);
        cart.removeProduct(product);

        assertFalse(cart.getProducts().contains(product));
    }

 }

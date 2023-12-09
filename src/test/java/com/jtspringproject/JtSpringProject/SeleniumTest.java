package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.cartService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SeleniumTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Download and setup the latest version of ChromeDriver
        WebDriverManager.chromedriver().forceDownload().setup();
        // Initialize the ChromeDriver
        driver = new ChromeDriver();
    }



        @Test
        public void testAdminLogin() {
             driver.get("http://localhost:8080/admin/login");

             WebElement usernameInput = driver.findElement(By.name("username"));
            WebElement passwordInput = driver.findElement(By.name("password"));

             usernameInput.sendKeys("admin");
            passwordInput.sendKeys("123");

             WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit']"));
            loginButton.click();

             try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

             assertEquals("http://localhost:8080/admin/loginvalidate", driver.getCurrentUrl());
        }


    @Test
    public void testAddProduct() {
         driver.get("http://localhost:8080/admin/login");

         WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit']"));

         usernameInput.sendKeys("admin");
        passwordInput.sendKeys("123");
        loginButton.click();

         driver.get("http://localhost:8080/admin/products/add");

         WebElement nameInput = driver.findElement(By.name("name"));
        WebElement categoryDropdown = driver.findElement(By.name("categoryid"));
        WebElement priceInput = driver.findElement(By.name("price"));
        WebElement weightInput = driver.findElement(By.name("weight"));
        WebElement quantityInput = driver.findElement(By.name("quantity"));
        WebElement descriptionInput = driver.findElement(By.name("description"));
        WebElement productImageInput = driver.findElement(By.name("productImage"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

         nameInput.sendKeys("Test Product");

         Select categorySelect = new Select(categoryDropdown);
        categorySelect.selectByVisibleText("MM");

        priceInput.sendKeys("100");
        weightInput.sendKeys("500");
        quantityInput.sendKeys("10");
        descriptionInput.sendKeys("This is a test product.");
        productImageInput.sendKeys("arrowDown.jpg");

         submitButton.click();

         Duration time = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, time);

         assertEquals("http://localhost:8080/admin/products", driver.getCurrentUrl());
    }

    @After
        public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }
    }

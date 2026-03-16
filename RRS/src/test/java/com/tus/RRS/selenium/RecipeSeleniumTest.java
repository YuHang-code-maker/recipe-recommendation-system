package com.tus.RRS.selenium;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class RecipeSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:8085");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String username, String password) {
        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginBtn"));

        usernameInput.clear();
        usernameInput.sendKeys(username);

        passwordInput.clear();
        passwordInput.sendKeys(password);

        loginButton.click();
    }

    private void waitForAppView() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("appView")));
    }

    private boolean anyVisible(By by) {
        List<WebElement> elements = driver.findElements(by);
        return elements.stream().anyMatch(WebElement::isDisplayed);
    }

    @Test
    void adminLoginShouldShowManagementButtons() {
    	login("admin", "admin");
        waitForAppView();

        WebElement appView = driver.findElement(By.id("appView"));
        assertTrue(appView.isDisplayed());

        assertTrue(anyVisible(By.id("addBtn")), "Admin should see add button");
    }

    @Test
    void customerLoginShouldNotShowManagementButtons() {
        login("customer", "customer");
        waitForAppView();

        WebElement appView = driver.findElement(By.id("appView"));
        assertTrue(appView.isDisplayed());

        assertFalse(anyVisible(By.id("addBtn")), "Customer should not see add button");
        assertFalse(anyVisible(By.className("editButton")), "Customer should not see edit buttons");
        assertFalse(anyVisible(By.className("deleteButton")), "Customer should not see delete buttons");
    }
}
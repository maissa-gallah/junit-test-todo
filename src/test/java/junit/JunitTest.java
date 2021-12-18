package junit;



import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class JunitTest {
    WebDriver driver;

    @BeforeAll
    public static void initialize() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void prepareDriver() {

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().timeouts().setScriptTimeout(Duration.ofMinutes(4));
    }

    public static void takeScreenshot(WebDriver webdriver, String fileWithPath) throws Exception {

        TakesScreenshot screenshot = ((TakesScreenshot) webdriver);
        File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(fileWithPath);
        FileUtils.copyFile(sourceFile, destinationFile);

    }

    @Test
    public void todoTestCase() throws Exception {

        driver.get("https://todomvc.com");
        selectLink("Backbone.js");
        addTodo("Read a book");
        addTodo("listen to music");
        addTodo("dance");
        Thread.sleep(2000);
        tickTodo(2);
        Thread.sleep(1000);
        assertRemainingTodos(2);
        takeScreenshot(driver, "C:\\Users\\USER\\eclipse-workspace\\junit\\pictures\\test_exception"+ ".png");
        ;
    }

    @ParameterizedTest
    @ValueSource(strings = { "Backbone.js",
            "AngularJS",
            "Dojo",
            "React" })
    public void todosTestCase(String technology) throws Exception {
        driver.get("https://todomvc.com");
        selectLink(technology);
        addTodo("Read a book");
        addTodo("listen to music");
        addTodo("dance");
        tickTodo(2);
        assertRemainingTodos(2);
        takeScreenshot(driver, "C:\\Users\\USER\\eclipse-workspace\\junit\\pictures\\image_" + technology + ".png");
    }

    private void selectLink(String technology) {
        WebElement element = driver.findElement(By.linkText(technology));
        element.click();
    }

    private void addTodo(String todo) throws InterruptedException {
        WebElement element = driver.findElement(By.className("new-todo"));
        element.sendKeys(todo);
        element.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
    }

    private void tickTodo(int number) throws InterruptedException {
        driver.findElement(By.cssSelector("li:nth-child(" + number + ") > div > input")).click();
    }

    private void assertRemainingTodos(int expectedRemaining) throws InterruptedException {
        WebElement element = driver.findElement(By.cssSelector("footer > span > strong"));
        validateInnerText(element, Integer.toString(expectedRemaining));
        Thread.sleep(2000);
    }

    private void validateInnerText(WebElement element, String expectedTest) {
        ExpectedConditions.textToBePresentInElement(element, expectedTest);
    }

    @AfterEach
    public void quitDriver() throws InterruptedException {
        driver.quit();
    }
}

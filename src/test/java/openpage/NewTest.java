package openpage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NewTest {

    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\rus21\\ChromeDriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(130));
        driver.get("https://www.google.com/"); //Перейти на сайт
    }

    @Test
    public void openPage(){

        driver.findElement(By.cssSelector("[aria-label=\"Найти\"]")).click();
        driver.findElement(By.cssSelector("[aria-label=\"Найти\"]")).sendKeys("Сайт компании Победа");
        driver.findElement(By.cssSelector("[aria-label=\"Найти\"]")).sendKeys(Keys.ENTER);
        // Дожидаемся загрузки страницы с результатами поиска и кликаем на первую ссылку
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='https://www.flypobeda.ru/']"))).click();


        // Дожидаемся загрузки страницы АК «Победа» и появления картинки с текстом «Полетели в Калининград»
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dp-1ihjhh6-root') and contains(text(), 'Полетели в Калининград')]")));

        // Проверяем, что текст на странице совпадает с ожидаемым

        WebElement kaliningradText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'dp-1ihjhh6-root') and contains(text(), 'Полетели в Калининград')]")));

        // Проверяем, что текст на странице совпадает с ожидаемым
        Assert.assertEquals("Полетели в Калининград!", kaliningradText.getText());

        WebElement imageElement = driver.findElement(By.xpath("//img[contains(@src, '68665f32303236303331_f1cd.png')]"));



        // Кликаем на переключатель языка и выбираем английский язык
        driver.findElement(By.xpath("//button[@class='dp-11x0mgu-root-root']")).click();//эта кнопка найдена
        driver.findElement(By.xpath("//div[contains(@class,'dp-1ct2iey-root')]//div[2]")).click();//'эта тоже найдена

        // Убедимся, что на главной странице отображаются тексты "Ticket search", "Online check-in", "Manage my booking"
        WebElement ticketSearch = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(@class,'dp-12qummd-root-inner') and text()='Ticket search']")));

        WebElement onlineCheckIn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(@class,'dp-12qummd-root-inner') and text()='Online check-in']")));

        WebElement manageBooking = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(@class,'dp-12qummd-root-inner') and text()='Manage my booking']")));
    }

    @After
    public void tearDown() {
        driver.quit();
    }


}
package pikabu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLOutput;
import java.time.Duration;
import java.util.List;

public class PikabuTest{

    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\rus21\\ChromeDriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://pikabu.ru/"); //Перейти на сайт
    }

    @Test
    public void pikabuSearch(){

    //Убедиться, что заголовок сайта соответствует требованию
    Assert.assertEquals(driver.getTitle(), "Горячее – самые интересные и обсуждаемые посты | Пикабу");

    //Кликнуть на кнопку «Войти»
    driver.findElement(By.xpath("//button[contains(@class,'pkb-normal-btn') and normalize-space(.)='Войти']")).click();

    //Убедиться, что отображается модальное окно «Авторизация», отображаются поля «Логин» и «Пароль», отображается кнопка «Войти».
    Assert.assertTrue(driver.findElement(By.cssSelector(".auth-modal")).isDisplayed());
    Assert.assertTrue(driver.findElement(By.name("username")).isDisplayed());
    Assert.assertTrue(driver.findElement(By.name("username")).isDisplayed());
    Assert.assertTrue(driver.findElement(By.xpath("//button[contains(@class,'button_success') and normalize-space(.)='Войти']")).isDisplayed());

    //Ввести в поля данные в формате логин/пароль – Qwerty/Qwerty и нажать «Войти».

    driver.findElement(By.xpath("//div[contains(@class,'auth-modal')]//input[@name='username']")).sendKeys("Qwerty");
    driver.findElement(By.xpath("//div[contains(@class,'auth-modal')]//input[@name='password']")).sendKeys("Qwerty");


    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.button_success.button_width_100")));

    driver.findElement(By.xpath("//div[contains(@class,'auth-modal')]//button[@type='submit']")).click();

    // Задержка в 20 секунд (чтобы было время пройти вручную капчу при необходимости)
        try {
            Thread.sleep(20000); // Задержка в миллисекундах
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    //Убедиться, что появилось сообщение об ошибке, и его текст: «Ошибка. Вы ввели неверные данные авторизации»
    String errorMessage = driver.findElement(By.xpath("//span[normalize-space(.)='Ошибка. Вы ввели неверные данные авторизации']")).getText().trim();
    Assert.assertEquals("Ошибка. Вы ввели неверные данные авторизации", errorMessage);


    }

@After
public void tearDown() {
    driver.quit();
}


}
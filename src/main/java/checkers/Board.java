package checkers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.lang.String.format;

public class Board {
    private ChromeDriver driver;
   // private WebDriverWait wait;

    public Board(ChromeDriver driver) {
        this.driver = driver;
        //Duration timeout = Duration.ofMillis(2000);
       // wait = new WebDriverWait(driver, timeout);
    }

    public void makeMove(int rowFrom, int colFrom, int rowTo, int colTo) {
        Space space = new Space();
        String spaceFrom = space.locators[rowFrom][colFrom];
        String spaceTo = space.locators[rowTo][colTo];
        By locatorFrom = By.xpath(format("//*[@name='%s']", spaceFrom));
        By locatorTo = By.xpath(format("//*[@name='%s']", spaceTo));
        driver.findElement(locatorFrom).click();
        driver.findElement(locatorTo).click();
        //wait.until(ExpectedConditions.attributeContains(locatorTo,"src","you"));
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

    }

    public void assertSpotIsOccupied(int rowTo, int colTo) {
        Space space = new Space();
        String spaceTo = space.locators[rowTo][colTo];
        By locatorTo = By.xpath(format("//*[@name='%s']", spaceTo));
        assert (driver.findElement(locatorTo).getAttribute("src").contains("you"));
    }

}
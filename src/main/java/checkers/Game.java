package checkers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Game {
    private ChromeDriver driver;
    private Board board;
    private By restartLink = By.xpath("//a[text()='Restart...']");
    private By makeMoveMessage = By.xpath("//p[@id='message']");

    public Game() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("https://www.gamesforthebrain.com/game/checkers/");

        board = new Board(driver);
    }

    public Board getBoard() {
        return board;
    }

    public boolean isMessageAllowsToMove(String message) {
        WebElement messageElement = driver.findElement(makeMoveMessage);
        return messageElement.getText().contains(message);
    }

    public Board restart() {
        driver.findElement(restartLink).click();
        //nothing is done with the returned board but was implemented for possible further logic convenience
        board = new Board(driver);
        return board;
    }
}
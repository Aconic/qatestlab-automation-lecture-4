package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions builder;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    /**
     * Logs in to Admin Panel.
     *
     * @param login
     * @param password
     */
    public void login(String login, String password) {
        // TODO implement logging in to Admin Panel

        CustomReporter.log("Login as user: " + login);
        driver.get(Properties.getBaseAdminUrl());
        driver.get(Properties.getBaseAdminUrl());
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData pd) {
        // TODO implement product creation scenario
        waitForContentLoad();

        builder = new Actions(driver);
        WebElement ctlgTab = driver.findElement(By.id("subtab-AdminCatalog"));
        builder.moveToElement(ctlgTab).perform();
        WebElement ctlg = driver.findElement(By.id("subtab-AdminProducts"));
        builder.moveToElement(ctlg).click(ctlg).perform();

        WebElement addBtn = driver.findElement(By.id("page-header-desc-configuration-add"));
        builder.moveToElement(addBtn).click(addBtn);
        builder.perform();

        driver.findElement(By.id("form_step1_name_1"))
                .sendKeys(pd.getName());
        driver.findElement(By.id("form_step1_qty_0_shortcut"))
                .sendKeys((pd.getQty()).toString());
        driver.findElement(By.id("form_step1_price_ttc_shortcut"))
                .sendKeys((pd.getPrice()));
        driver.findElement(By.className("switch-input")).click();
        waitForSuccessPopup();
        WebElement saveBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary js-btn-save']"));
        builder.moveToElement(saveBtn).click(saveBtn);
        builder.perform();
        waitForSuccessPopup();
    }

    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ajax_running")));
    }

    public void waitForSuccessPopup() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.
                className("js-spinner spinner hide btn-primary-reverse onclick pull-left m-r-1")));
        WebElement closePopup = driver.findElement(By.className("growl-close"));
        builder.moveToElement(closePopup).click();
        builder.perform();
    }
}

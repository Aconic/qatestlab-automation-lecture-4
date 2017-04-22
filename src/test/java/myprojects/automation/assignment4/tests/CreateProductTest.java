package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;


public class CreateProductTest extends BaseTest {

    private ProductData productData;

    @Test(dataProvider = "DataProviderData")
    public void createNewProduct(String login, String password) throws InterruptedException {
        // TODO implement test for product creation
        actions.login(login, password);
        productData = ProductData.generate();
        actions.createProduct(productData);
    }

    @DataProvider
    public Object[][] DataProviderData() {
        return new Object[][]{
                new Object[]{"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"}
        };
    }

    // TODO implement logic to check product visibility on website

    @Test(dependsOnMethods = {"createNewProduct"})
    public void checkProductVisibility() {
        driver.get(Properties.getBaseUrl());
        Actions builder = new Actions(driver);
        driver.executeScript("scroll(0,700)");

        WebElement allProducts = driver.
                findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']"));
        builder.click(allProducts).perform();

        paginationCheck();

        WebElement displayedElement = driver.findElement(By.xpath("//div[@class='product-description']/h1/a[contains" +
                "(.,'" + productData.getName() + "')]"));
        Assert.assertNotNull(displayedElement);
        builder.click(displayedElement).perform();

        String actualName = driver.findElement(By.xpath("//h1[@itemprop='name']")).getText();
        String actualPrice = driver.findElement(By.xpath("//span[@itemprop='price']")).getText();
        driver.findElement(By.xpath("//a[@href='#product-details']")).click();
        String actualQty = driver.findElement(By.xpath("//div[@class='product-quantities']/span")).getText();

        actualPrice = actualPrice.substring(0, actualPrice.indexOf(" "));
        actualQty = actualQty.substring(0, actualQty.indexOf(" "));

        Assert.assertEquals(actualName.toLowerCase(), productData.getName().toLowerCase());
        Assert.assertEquals(actualPrice, productData.getPrice());
        Assert.assertEquals(actualQty, productData.getQty().toString());
    }

    public void paginationCheck() {
        WebElement page = driver.findElement(By.xpath("//nav//ul[@class='page-list clearfix text-xs-center']//li[last()-1]"));
        page.click();
    }
}
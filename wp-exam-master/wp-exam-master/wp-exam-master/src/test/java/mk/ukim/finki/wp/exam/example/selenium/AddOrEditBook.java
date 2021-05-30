package mk.ukim.finki.wp.exam.example.selenium;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddOrEditBook extends AbstractPage {

    private WebElement name;
    private WebElement price;
    private WebElement quantity;
    private WebElement categories;
    private WebElement submit;

    public AddOrEditBook(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage add(WebDriver driver, String addPath, String name, String price, String quantity, String[] categories) {
        get(driver, addPath);
        AbstractPage.assertRelativeUrl(driver, addPath);
        AddOrEditBook addOrEditBook = PageFactory.initElements(driver, AddOrEditBook.class);
        addOrEditBook.name.sendKeys(name);
        addOrEditBook.price.sendKeys(price);
        addOrEditBook.quantity.sendKeys(quantity);
        Select select = new Select(addOrEditBook.categories);
        for (String c : categories) {
            select.selectByValue(c);
        }
        addOrEditBook.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public static ItemsPage update(WebDriver driver, WebElement editButton, String name, String price, String quantity, String[] categories) {
        String href = editButton.getAttribute("href");
        System.out.println(href);
        editButton.click();
        AbstractPage.assertAbsoluteUrl(driver, href);

        AddOrEditBook addOrEditBook = PageFactory.initElements(driver, AddOrEditBook.class);
        addOrEditBook.name.clear();
        addOrEditBook.name.sendKeys(name);
        addOrEditBook.price.sendKeys(price);
        addOrEditBook.quantity.sendKeys(quantity);
        Select select = new Select(addOrEditBook.categories);
        select.deselectAll();
        for (String c : categories) {
            select.selectByValue(c);
        }
        addOrEditBook.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }


}
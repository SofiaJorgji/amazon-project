package com.sqa.sj;

import java.util.concurrent.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.safari.*;
import org.openqa.selenium.support.ui.*;
import org.testng.*;
import org.testng.annotations.*;

public class AmazonTest {

	// Variables: Driver to drive test
	private WebDriver driver;

	// Keep track of the base domain of testing site
	private String baseUrl;

	@AfterMethod
	public void deleteItems() throws Exception {
		// delete the item from your cart
		WebElement deleteButton = this.driver.findElement(By.cssSelector("input[value='Delete']"));
		deleteButton.click();
	}

	@DataProvider
	public Object[][] dp() {
		return new Object[][] { new Object[] { "dog treats", 5, 169.95 }, };
	}

	@BeforeClass(enabled = true)
	public void setUpChrome() throws Exception {
		// Set system property to use Chrome driver
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		// Setup the driver to use Chrome
		this.driver = new ChromeDriver();
		// Set the base URL for this test
		this.baseUrl = "https://www.amazon.com";
		// Set an implicit wait of up to 30 seconds
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Maximize the window
		this.driver.manage().window().maximize();
	}

	@BeforeClass(enabled = false)
	public void setUpFirefox() throws Exception {
		// Setup the driver to use Firefox
		this.driver = new FirefoxDriver();
		// Set the base URL for this test
		this.baseUrl = "https://www.amazon.com/";
		// Set an implicit wait of up to 30 seconds
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Maximize the window
		this.driver.manage().window().maximize();
	}

	@BeforeClass(enabled = false)
	public void setUpSafari() throws Exception {
		// Set system property to use Safari driver
		this.driver = new SafariDriver();
		// Set the base URL for this test
		this.baseUrl = "https://www.amazon.com/";
		// Set an implicit wait of up to 30 seconds
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Maximize the window
		this.driver.manage().window().maximize();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		// quit closes all instances of the browser.
		this.driver.quit();
		// close method would close current instance only.
		// driver.close();
	}

	@Test(dataProvider = "dp")
	public void testAmazon(String itemName, int quantity, double expectedPrice) throws Exception {
		// Go to base url of domain of site being tested
		this.driver.get(this.baseUrl + "/");
		// Capture the search field in a WebElement
		WebElement searchField = this.driver.findElement(By.id("twotabsearchtextbox"));
		// Clear the search field
		searchField.clear();
		// Type text into the search field
		searchField.sendKeys("dog treats");
		// Capture the search button in a WebElement and click on it
		WebElement searchButton = this.driver.findElement(By.cssSelector("input.nav-input"));
		searchButton.click();
		// Alternatively submit the form for searching
		// searchField.submit();
		// Click on first item in results
		WebElement resultItem = this.driver.findElement(By.xpath("//li[@id='result_0']/div/div[4]/div/a/h2"));
		resultItem.click();
		// Choose option to buy one time as a guest user
		this.driver.findElement(By.xpath("//div[@id='oneTimeBuyBox']/div/div/a/i")).click();
		// Set the quantity to 5
		// new
		new Select(this.driver.findElement(By.id("quantity"))).selectByVisibleText(String.valueOf(quantity));
		// Click on the add to cart button
		this.driver.findElement(By.id("add-to-cart-button")).click();
		// Thread.sleep(10000);
		WebElement myDynamicElement = (new WebDriverWait(this.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("hlb-view-cart-announce")));
		// Click on the view cart button
		this.driver.findElement(By.id("hlb-view-cart-announce")).click();
		WebElement priceElement = this.driver.findElement(By.cssSelector("span#sc-subtotal-amount-activecart span"));
		String priceString = priceElement.getText().trim().replace("$", "");
		double actualPrice = Double.parseDouble(priceString);
		Assert.assertEquals(actualPrice, expectedPrice);
	}
}

package org.opencorrelate.ci.selenium;

import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Predicate;



/**
 * 
 * @author Presley H. Cannady, Jr. <revprez@opencorrelate.org>
 *
 */
public abstract class TestWebDriver<T extends WebDriver> implements ITestWebDriver<T> {

	
	private T driver;
	
	public void setDelegateDriver(T driver) {
		this.driver = driver;
	}
	
	public TestWebDriver(T driver) {
		setDelegateDriver(driver);
	}
	
	public void get(String url) {
		driver.get(url);
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public void close() {
		driver.close();
	}

	public void quit() {
		driver.quit();
	}

	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	public Navigation navigate() {
		return driver.navigate();
	}

	public Options manage() {
		return driver.manage();
	}

	
	public Keyboard getKeyboard() {
		@SuppressWarnings("unchecked")
		T d = (driver instanceof TestWebDriver) ? ((TestWebDriver<T>)driver).getDelegateDriver() : driver;
		
		if (d instanceof RemoteWebDriver)
			return ((RemoteWebDriver)d).getKeyboard();
		else
			return null;
	}
	
	public T getDelegateDriver() {
		return driver;
	}
	
	/**
	 * 
	 * @param by : element locator
	 * @return true if element is present, false if not
	 */
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e){
			return false;
		}
	}
	
	/**
	 * 
	 * @param text : text to be found
	 * @return true if text is present, false if not
	 */
	public boolean isTextPresent(String text) {
		try {
			driver.findElement(By.xpath(String.format("//body[contains(text(),'%s)']",text)));
			return true;
		} catch (NoSuchElementException e){
			return false;
		}
	}
	
	
	public void inputAndValidate(final By invalid, final By valid, String input) {
		T driver = getDelegateDriver();
		(new FluentWait<T>(driver)).until(new Predicate<T>() {
			public boolean apply(T driver) { return driver.findElement(invalid).isDisplayed(); }
		});
		
		driver.findElement(invalid).clear();
		driver.findElement(invalid).sendKeys(input);
		if (driver instanceof RemoteWebDriver)
			((RemoteWebDriver)driver).getKeyboard().pressKey(Keys.TAB);
		
		(new FluentWait<T>(driver)).until(new Predicate<T>() {
			public boolean apply(WebDriver d) { return d.findElement(valid).isDisplayed(); }
		});
	}
	
	public static void mouseDownRight(final WebDriver driver, final WebElement element) {
		if (driver instanceof RemoteWebDriver && element instanceof RemoteWebElement) {
			Mouse mouse = ((RemoteWebDriver)driver).getMouse();
			mouse.click(((RemoteWebElement)element).getCoordinates());
		}
	}
	
	

}

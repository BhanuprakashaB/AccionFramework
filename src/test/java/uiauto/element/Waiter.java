package uiauto.element;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import uiauto.page.Page;

public class Waiter {
	private WebDriverWait waiter;
	private Page page;
	private WebDriver driver;
	
	public Waiter(Page page, WebDriver driver, Long waitTime) {
		this.waiter = new WebDriverWait(driver, waitTime);
		this.page = page;
		this.driver = driver;
	}
	
	public Waiter(WebDriver driver, Long waitTime) {
		this.waiter = new WebDriverWait(driver, waitTime);
		this.driver = driver;
	}
	
	public WebDriverWait getWaiter() {
		return this.waiter;
	}
	
	public void waitTillElementClickable(String name, Object... inputs) throws Exception {
		this.getWaiter().until(ExpectedConditions.elementToBeClickable(this.page.getIdentifier(name, inputs)));
	}
	
	public void waitTillElementInvisible(String name, Object... inputs) throws Exception {
		try {
			this.getWaiter().until(ExpectedConditions.invisibilityOf(this.driver.findElement(page.getIdentifier(name, inputs))));
		} catch (NoSuchElementException e) {
//			 Silencing the exception if element is not present 
		}
	}
	
	public void waitTillElementDisplayed(String name, Object... inputs) throws Exception {
		this.getWaiter().until(ExpectedConditions.visibilityOf(this.driver.findElement(page.getIdentifier(name, inputs))));
	}
}

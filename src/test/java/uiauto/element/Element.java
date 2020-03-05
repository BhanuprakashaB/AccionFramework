package uiauto.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import uiauto.lib.enums.ConfigType;
import uiauto.page.Page;

public class Element {
	private Page page;
	private WebElement element;
	
	public Element(Page page, WebElement element, By by){
		this.page = page;
		this.element = element;
	}
	
	public Element element(String name, Object... inputs) throws Exception {
		By by = this.page.getIdentifier(name, inputs);
		List<WebElement> elements = ElementFactory.identify(this.element, by, Long.valueOf(this.page.getConfig().getConfigValue(ConfigType.WAIT_EXPLICIT))); 
		return this.page.convertToElement(elements.get(0), name, by);
	}
	
	public List<Element> elements(String name, Object... inputs) throws Exception {
		By by = this.page.getIdentifier(name, inputs);
		List<WebElement> elements = ElementFactory.identify(this.element, by, Long.valueOf(this.page.getConfig().getConfigValue(ConfigType.WAIT_EXPLICIT))); 
		return this.page.convertToElements(elements, name, by);
	}
	
	public void click(){
		this.element.click();
	}
	
	public void clear(){
		this.element.clear();
	}
	
	public void sendKeys(String value){
		this.element.sendKeys(value);;
	}
	
	public boolean isEnabled(){
		return this.element.isEnabled();
	}
	
	public boolean isDisplayed(){
		return this.element.isDisplayed();
	}
	
	public String getAttribute(String property){
		return this.element.getAttribute(property);
	}
	
	public String getText(){
		return this.element.getText();
	}
}

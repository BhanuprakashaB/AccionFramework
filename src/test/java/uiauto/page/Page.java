package uiauto.page;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import uiauto.central.ContextConfiguration;
import uiauto.client.Client;
import uiauto.element.Element;
import uiauto.loader.Loader;

public interface Page {

	void setLoader(ContextConfiguration cong) throws IOException, Exception;
	Element element(String name, Object... inputs) throws Exception;
	List<Element> elements(String name, Object... inputs) throws Exception;
	Loader getLoader();
	Client getClient();
	ContextConfiguration getConfig();
	By getIdentifier(String name, Object... inputs) throws Exception;
	Element convertToElement(WebElement element, String name, By by) throws Exception;
	List<Element> convertToElements(List<WebElement> elements, String name, By by) throws Exception;
}
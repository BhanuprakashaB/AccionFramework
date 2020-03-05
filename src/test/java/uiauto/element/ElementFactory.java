package uiauto.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import uiauto.lib.enums.ByType;

public class ElementFactory {
	public static By convertToBy(Identifier id, Object... inputs) throws Exception{
		By by = null;
		ByType idType = id.getIDType();
		String value = id.getIDValue(inputs);
    	switch(idType) {
		case ID:
			by = By.id(value);
			break;
		case XPATH:
			by = By.xpath(value);
			break;
		case CLASSNAME:
			by = By.className(value);
			break;
		case NAME:
			by = By.name(value);
			break;	
		case LINKTEXT:
			by = By.linkText(value);
			break;
		case PARTIALLINKTEXT:
			by = By.partialLinkText(value);
			break;
		case CSSSELECTOR:
			by = By.cssSelector(value);
			break;
		case TAGNAME:
			by = By.tagName(value);
			break;
		default :
			throw new Exception(String.format("Unsupported identifier %s", idType));
		}
    	return by;
	}
	
	public static List<WebElement> identify(SearchContext context, By by, long explicitWait) throws Exception{
        long start = System.currentTimeMillis() / 1000;
        long current = start;
        List<WebElement> identifiedElements = null;
        
        while (current - start < explicitWait){
            identifiedElements = context.findElements(by);
             if ((identifiedElements != null) && (identifiedElements.size() != 0)){
            	 return identifiedElements;
             } else {
                 Thread.sleep(500);
                 current = System.currentTimeMillis() / 1000;
             }
        }
        
        throw new Exception(String.format("Not able to identify element for by : %s", by));
    }
}
package uiauto.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import uiauto.central.ContextConfiguration;
import uiauto.client.Client;
import uiauto.element.Element;
import uiauto.element.ElementFactory;
import uiauto.element.Identifier;
import uiauto.element.Waiter;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.utilities.Utility;
import uiauto.loader.JsonLoader;
import uiauto.loader.Loader;

public class BasePage implements Page {
	private String jsonFileName;
	private Client client;
	private Loader loader;
	private ContextConfiguration conf;
	private Waiter waiter;

	public BasePage(String jsonFileName, ContextConfiguration conf, Client client) throws Exception {
		String packageName = Utility.getJsonFilePathFromMapDir(getClass().getPackage().getName());
		if(packageName != "") {
			this.jsonFileName = Utility.joinPaths(conf.getConfigValue(ConfigType.MAPS_DIR), packageName, jsonFileName);
		} else {
			this.jsonFileName = Utility.joinPaths(conf.getConfigValue(ConfigType.MAPS_DIR), jsonFileName);
		}
		this.client = client;
		this.conf = conf;
		setLoader(conf);
		setWaiter();
	}
	
	public By getIdentifier(String elementName, Object... inputs) throws Exception {
		Identifier id = this.getLoader().getIdentifier(elementName);
		return ElementFactory.convertToBy(id, inputs);
	}

	public void setLoader(ContextConfiguration conf) throws Exception {
		this.loader = new JsonLoader(jsonFileName, conf.getConfigValue(ConfigType.PLATFORM_TYPE));
	}
	
	public Element convertToElement(WebElement element, String name, By by) throws Exception{
		return new Element(this, element, by);
	}
	
	public List<Element> convertToElements(List<WebElement> elements, String name, By by) throws Exception{
		List<Element> outElements = new ArrayList<Element>();
		for(WebElement element: elements){
			outElements.add(convertToElement(element, name, by));
		}
		return outElements;
	}
	
	public Element element(String name, Object... inputs) throws Exception {
		By by = getIdentifier(name, inputs);
		List<WebElement> elements = ElementFactory.identify(this.getClient().getDriver(), by, Long.valueOf(this.conf.getConfigValue(ConfigType.WAIT_EXPLICIT))); 
		return convertToElement(elements.get(0), name, by);
	}
	
	public List<Element> elements(String name, Object... inputs) throws Exception {
		By by = getIdentifier(name, inputs);
		List<WebElement> elements = ElementFactory.identify(this.getClient().getDriver(), by, Long.valueOf(this.conf.getConfigValue(ConfigType.WAIT_EXPLICIT))); 
		return convertToElements(elements, name, by);
	}
	
	public Loader getLoader() {
		return loader;
	}
	
	public Client getClient() {
		return client;
	}
	
	public ContextConfiguration getConfig() {
		return conf;
	}
	
	private void setWaiter() {
		this.waiter = new Waiter(this, this.getClient().getDriver(), 
				Long.parseLong(this.getConfig().getConfigValue(ConfigType.WAIT_EXPLICIT)));
	}
	
	public Waiter getWaiter() {
		return this.waiter;	
	}
}

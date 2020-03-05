package uiauto.client;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;

import uiauto.central.ContextConfiguration;
import uiauto.lib.enums.BrowserType;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.enums.DesktopPlatformType;
import uiauto.lib.utilities.Utility;

public class SeleniumClient implements Client{
	private ContextConfiguration config;
	private WebDriver seleniumDriver;
	private DesktopPlatformType platform;
	private BrowserType browser;
	
	public SeleniumClient(ContextConfiguration config) throws Exception {
		this.config = config;
		this.platform = DesktopPlatformType.valueOf(this.config.getConfigValue(ConfigType.DESKTOP_PLATFORM).toUpperCase());
		this.browser = BrowserType.valueOf(this.config.getConfigValue(ConfigType.BROWSER_NAME).toUpperCase());
		loadDriver();
	}

	private void loadDriver() throws Exception {
		switch(this.browser) {
		case CHROME:
			loadChromeDriver();
			break;
		case SAFARI:
			loadSafariDriver();
			break;
		}
		
		this.getDriver().get(this.config.getConfigValue(ConfigType.SITE_URL));
		this.getDriver().manage().window().maximize();
		this.getDriver().manage().timeouts().implicitlyWait(Long.parseLong(this.config.getConfigValue(ConfigType.WAIT_IMPLICIT)), TimeUnit.SECONDS);
	}
	
	private void loadChromeDriver() {
		String pcOS = System.getProperty("os.name").toUpperCase();
		String chromeDriverPath;
		
		if(pcOS.contains("MAC")) {
			chromeDriverPath = Utility.joinPaths(System.getProperty("user.dir"), "drivers", "chromedriver_mac");
		} else {
			chromeDriverPath = Utility.joinPaths(System.getProperty("user.dir"), "drivers", "chromedriver_win.exe");
		}
		
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		
		seleniumDriver = new ChromeDriver();
	}
	
	private void loadSafariDriver() throws Exception {
		String pcOS = System.getProperty("os.name").toUpperCase();
		
		if(pcOS.contains("MAC")) {
			seleniumDriver = new SafariDriver();
		} else {
			throw new Exception("Safari driver is not supported other than MAC OS");
		}
	}
	
	public WebDriver getDriver() {
		return this.seleniumDriver;
	}

	public DesktopPlatformType getDevicePlatform() {
		return this.platform;
	}

	public String captureScreenForReport() throws IOException {
		String screenshotName = Utility.getDateTimeString("yyyy_MM_dd_HH_mm_ss_sss") + ".jpg";
		TakesScreenshot ts = (TakesScreenshot)getDriver();
		File tempLocation = ts.getScreenshotAs(OutputType.FILE);
		FileHandler.createDir(new File(config.getConfigValue(ConfigType.SCREENSHOT_DIR)));
		String screenshotPath = Utility.joinPaths(
				config.getConfigValue(ConfigType.SCREENSHOT_DIR), screenshotName);
		FileHandler.copy(tempLocation, new File(screenshotPath));
	
		//To add relative path for screenshots in html report
		String relativePath = Utility.joinPaths("./Screenshots/", screenshotName);
		return relativePath;
	}
	
	public void quit() {
		if(seleniumDriver != null) {
			seleniumDriver.quit();
		}
	}

}

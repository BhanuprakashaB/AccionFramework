package uiauto.client;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

public interface Client {
	void quit();
	WebDriver getDriver();
	String captureScreenForReport() throws IOException;
}

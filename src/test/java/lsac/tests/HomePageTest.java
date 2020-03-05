package lsac.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import lsac.pages.LibraryPage;
import uiauto.lib.enums.PlatformType;
import uiauto.test.BaseTest;

public class HomePageTest extends BaseTest{
	
	@Test(groups= {"RegressionTest"})
	public void tc001_verifyHomePageIsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
		
		home.getWaiter().waitTillElementDisplayed("LibraryText");
		Assert.assertTrue(home.element("LibraryText").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc002_verifyPrepTest71IsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
		
		Assert.assertTrue(home.element("PrepTest71Link").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc003_verifyPrepTest73IsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
		
		Assert.assertTrue(home.element("PrepTest73Link").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc004_verifyPrepTest74IsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
		
		Assert.assertTrue(home.element("PrepTest74Link").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc005_verifyLSACIconIsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
		
		if(getPlatform().equals(PlatformType.MOBILE)) {
			home.element("MobileMenuItem").click();
			home.getWaiter().waitTillElementDisplayed("LSACLogo");
		}
		Assert.assertTrue(home.element("LSACLogo").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc006_verifyLibraryLinkIsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
	
		Assert.assertTrue(home.element("LibraryLink").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc007_verifyTutorialsLinkIsDisplayed() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
	
		Assert.assertTrue(home.element("TutorialsLink").isDisplayed());
	}
}

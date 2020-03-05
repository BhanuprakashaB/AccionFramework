package lsac.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import lsac.pages.LSATPrepTest71HomePage;
import lsac.pages.LibraryPage;
import uiauto.test.BaseTest;

public class LSATPrepTest71Test extends BaseTest{

	@Test(groups= {"RegressionTest"})
	public void tc001_verifyNavigationToPrepTest71Page() throws Exception {
		LibraryPage home = new LibraryPage(config, client);
		LSATPrepTest71HomePage prepTest71 = new LSATPrepTest71HomePage(config, client);
		
		home.getWaiter().waitTillElementClickable("PrepTest71Link");
		home.element("PrepTest71Link").click();
		prepTest71.getWaiter().waitTillElementDisplayed("MasterPagePageName");
		Assert.assertTrue(prepTest71.element("MasterPagePageName").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest", "OnlyWeb"})
	public void tc002_verifyLibraryText() throws Exception {
		LSATPrepTest71HomePage prepTest71 = new LSATPrepTest71HomePage(config, client);

		Assert.assertTrue(prepTest71.element("MasterPagePageName").isDisplayed());
	}
	
	@Test(groups= {"RegressionTest"})
	public void tc003_verifyScctionSelectionTextDisplayed() throws Exception {
		LSATPrepTest71HomePage prepTest71 = new LSATPrepTest71HomePage(config, client);

		Assert.assertTrue(prepTest71.element("SectionSelectionText").isDisplayed());
	}
}

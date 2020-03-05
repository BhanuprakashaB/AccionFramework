package uiauto.test;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import uiauto.central.ContextConfiguration;
import uiauto.central.SMASingleton;
import uiauto.client.AppiumServer;
import uiauto.client.Client;
import uiauto.client.ClientFactory;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.enums.PlatformType;

public class BaseTest {
	private AppiumServer appiumServer;
	protected Client client = null;
	protected ExtentTest test;
	protected PlatformType platform;
	protected ContextConfiguration config = null;
	protected ExtentReports reporter = null;
	private ITestContext context = null;
	
	@BeforeSuite(alwaysRun = true)
	public void configureSuite(ITestContext context) throws Exception {			
		SMASingleton.INSTANCE.init();
	}
	
	@BeforeTest(alwaysRun = true)
	public void processTestContext(ITestContext context) throws Exception {
		SMASingleton.INSTANCE.processTestContext(context);
	}
	
	@BeforeClass(alwaysRun = true)
	public void configureTestClass(ITestContext context) throws Exception {
		ClientFactory tool = new ClientFactory();
		this.config = SMASingleton.INSTANCE.getConfiguration(context);
		platform = PlatformType.valueOf(this.config.getConfigValue(ConfigType.PLATFORM_TYPE).toUpperCase());
		this.reporter  = SMASingleton.INSTANCE.getReporter(context);
		this.context = context;
		if(this.platform.equals(PlatformType.MOBILE)) {
			this.appiumServer = new AppiumServer(this.config);
			this.appiumServer.start();
		} 
		
		client = tool.getClient(platform, config, appiumServer);
	}
	
	@BeforeMethod(alwaysRun = true)
	public void startApplication(Method m) throws Exception {
		createReport(m.getName());
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		writeToReport(result);
	}
	
	@AfterClass(alwaysRun = true)
	public void quitDriver() throws Exception {
		if(client != null) {
			client.quit();
		}
		
		if(this.appiumServer !=null) {
			this.appiumServer.stop();
		}
	}
	
	protected void writeToReport(ITestResult result) throws Exception {
		switch(result.getStatus()) {
		case ITestResult.SUCCESS:
			onTestSuccess(this.test);
			break;
		case ITestResult.FAILURE:
			onTestFailure(this.test, result);
			break;
		case ITestResult.SKIP:
			onTestSkipped(this.test);
			break;
		default:
			onException();
		}
		
		if (this.reporter != null) {
			this.reporter.flush();
		}
	}
	
	private void onTestSuccess(ExtentTest test) {
		test.pass("TestCase is passed");
	}
	
	private void onTestFailure(ExtentTest test, ITestResult result) throws Exception{
		String screenshotPath;
		try {
			screenshotPath = this.client.captureScreenForReport();
		}catch(Exception e) {
			screenshotPath = " Failed to get sreenshot ";
		}
		
		test.fail("Test failed with Exception : " + result.getThrowable());
		test.fail("Screenshot of failed test");
		test.addScreenCaptureFromPath(screenshotPath);
	}

	private void onTestSkipped(ExtentTest test) {
		test.skip("TestCase skipped ");
	}
	
	private void onException() throws Exception{
		test.fail("Test not executed");
		test.fail("Failed to execute the test");
	}
	
	protected ITestContext getContext(){
		return this.context;
	}
	
	protected PlatformType getPlatform(){
		return this.platform;
	}
	
	public void createReport(String testName) {
		test = reporter.createTest(testName);
	}
}

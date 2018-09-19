package com.valytics.SDDAutomation.JCCQAS;

import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.Date;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.glass.events.KeyEvent;
import com.valytics.SDDAutomation.Utilities.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JCCQAS_AddProvider_Demo extends ValTestBase{
	private static final String AddProvider1001_with_Kathlon_Chrome = null;
	public static String myClassName="JCCQAS_Regression_AddProvider_Demo"; //myClassName is reflection of the class
	ExtentTest logger;
	String funcName;
	// constructor goes before @BeforeClass
	JCCQAS_AddProvider_Demo() {

		testCaseName="JCCQAS_Regression_AddProvider_Demo"; //Note: TestCaseName could contain space or other characters
	}
	Page_SelfREgistration selfReg;
	String ssnNname[];
	static String ssnbase="800000000"; //because SSN with leading 8 are not legitimate
	boolean acceptNextAlert = true;
	WebDriverWait aGoodWait = null;
	InternetExplorerDriver driver; // instantiation by @Test Regression_System
	InternetExplorerOptions ieOptions;

	//@BeforeTest
	@BeforeClass
	public void beforeTest() {
		funcName="JCCQAS_AddProvider_Demo";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, /*avtb.*/tDelta());

		super.beforeTest();
		logger = super.logger;
		System.out.println(myClassName + ": beforeTest");
		logger.log(LogStatus.INFO, myClassName + ": beforeTest");

	}
	//@AfterTest
	@AfterClass
	public void afterTest() throws InterruptedException {
		System.out.println(myClassName + ": afterTest");
		logger.log(LogStatus.INFO, myClassName + " afterTest");
		super.afterTest();

	}
	@Test
	public void Regression_System() throws Exception {
		super.mainTest();
		username_login("LNDODTWOF");
		testAddProvider(driver);
		if(false) { // Not doing this on Demo
		if(true) { // need to wait till able to handle Window pop-up for File Search and image/Sikuli
			String failedFileFullpath="C:\\Workspace\\EclipseProjects\\JCCQAS\\JCCQAS_Upload_download\\SimpleText1001.txt";
			String GoodFileFullpath = "C:\\Workspace\\EclipseProjects\\JCCQAS\\JCCQAS_Upload_download\\AssertObjectFunctionAutocompleteOptions.tif";
			doDocumentUpload(driver, failedFileFullpath, GoodFileFullpath);
		}
		if(true) {
			doContingencyTraining(driver);
		}
		}
		driver.findElement(By.linkText("Close")).click();
		//driver.close();
		driver.quit();
		System.out.println("driver.quit() for AddProvider_Demo");
		super.testHasRunToTheEnd=true;


	} // end of Regression_System()
	//This module implemented JCCQAS Regression System (tap4)
	//beforeTest();
	public void username_login(String username) throws Exception {
		System.out.println("!!!!!!!!!!  Please make sure having fresh IE and CAC out then in");

		//ssnNname = GenericLibs.time2ssn2name(ssnbase, "LN", "FN");
		System.setProperty("webdriver.ie.driver","C:\\Workspace\\EclipseProjects\\JavaLibs\\Selenium\\IEDriverServer_Win32_3.12.0\\IEDriverServer.exe");
		//String url = "https://ccqasvld.csd.disa.mil/" ;
		String url = ExtentReports_Annotations.jccqasURL ;

		//driver.get("https://ccqasvld.csd.disa.mil/");
		ieOptions = new InternetExplorerOptions();
		ieOptions.introduceFlakinessByIgnoringSecurityDomains();
		ieOptions.withInitialBrowserUrl(url);
		/*
		if(false) {
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			//caps.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, url);
			@SuppressWarnings("deprecation")
			WebDriver driver = new InternetExplorerDriver(caps);
			//WebDriver driver = new InternetExplorerDriver(null, caps, 0);
		}*/
		//NOTE: InternetExplorerDriver has many more members than WebDriver;
		try {	
			driver = new InternetExplorerDriver(ieOptions);
		} catch(org.openqa.selenium.SessionNotCreatedException zoom100) {
			System.out.println("\n==========> Exception: Can it be because of IE not Zoom(100%)? <================== ");
			System.out.println("==========> Early Test Ends. Please Investigate, set to Zoom(100%)! <==================\n");

			return;
		}
		//NOTE: if seeing error: org.openqa.selenium.SessionNotCreatedException, then it could be IE zoom != 100%.
		if(false) {
			Thread.sleep(12000);
		}
		else {
			System.out.println("Before WebDriverWait()");
			System.out.flush();

			aGoodWait = new WebDriverWait(driver, 150);

			//driver.navigate().to(url);
			//driver.get(url);
			System.out.println("Before AltertIsPresent()");
			System.out.flush(); //needed

			Object ret1 = aGoodWait.until(ExpectedConditions.alertIsPresent());
			System.out.println("After aGoodWait.until()" + ret1.toString());
		}
		if(null != aGoodWait ) {
			aGoodWait = new WebDriverWait(driver, 150);
		}
		System.out.println("Before alert().accept()");

		driver.switchTo().alert().accept(); // Click any pop-up; Modal message box
		System.out.println("After alert().accept()");

		driver.manage().window().maximize();
		System.out.println("Before PageFactory()");

		selfReg = PageFactory.initElements(driver, Page_SelfREgistration.class);
		System.out.println("After PageFactory()");

		//driver.findElement(By.name("UicTextField"));
		//Utility.isElementLinktextPresnt(driver, "Registration (DoD Only)", 30);
		/* if(false) {
			if(driver.findElement(By.linkText("CAC/PIV Logon")).isEnabled()) {
				driver.findElement(By.linkText("CAC/PIV Logon")).click();
			}
	}
		else {
		 */
		boolean whichway = true;
		if(whichway) {
			if(selfReg.UsernameLogon.isEnabled()) {
				selfReg.UsernameLogon.click();
				System.out.println("UsernameLogon.click");
				logger.log(LogStatus.PASS, "Username Logon");

			}
		}
		else {
			//String lTextAt = "Registration (DoD Only)" ;
			String lTextAt = "Username Logon" ;
			switch (lTextAt) {
			case "Registration (DoD Only)":
				break;
			case "Username Logon":
				break;
			default:
				System.out.println("linkText=" + lTextAt + "  <== Not yet implemented");
			}
			if(driver.findElement(By.linkText(lTextAt)).isEnabled()) {
				//<a class="btn btn-primary btn-block" href="/Account/PrivacyAct?method=register">Registration (DoD Only)</a>
				driver.findElement(By.linkText(lTextAt)).click();
				System.out.println("UsernameLogon.click");
				logger.log(LogStatus.PASS, "Username Logon");
			}
		}


		Utility.isElementIdPresnt(driver, "privacyYes", 20);
		driver.findElement(By.id("privacyYes")).click();
		if(false) {
			//<IMG border=0 alt="OK, agree to DoD Consent Banner" src="/amserver/images/button/ok.gif" align=middle a <>
			driver.findElement(By.xpath("//img[@alt='OK, agree to DoD Consent Banner']")).click();
		}
		driver.findElementById("username").clear();
		driver.findElementById("username").sendKeys(username);
		//		driver.findElementById("password").clear();
		//		driver.findElementById("password").sendKeys("LNDODTWOF");

		//driver.findElementById("password").click();

		//driver.findElementByClassName("Submit").click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();;

		// It doesn't seem to need CAC for the username logon???????
		if(false) {
			//<IMG border=0 alt="Click Here to Access with DoD CAC, ECA or VA PIV" src="https://ssopt.csd.disa.mil/amserver/CAC/images/cac_yes_sm.gif"> ...
			driver.findElement(By.xpath("//img[@alt='Click Here to Access with DoD CAC, ECA or VA PIV']")).click();
			Thread.sleep(10000); // wait for 2 imgaes
			driver.switchTo().alert().accept(); // OK on the 2nd image; and wait for the PIN pop-up
			//with 10 seconds, typing the PIN, waiting for the Self-Registration window to display 
			Thread.sleep(10000); // to type the PIN
		}
		//TODO: need to wait for the System Menu
		aGoodWait.until(ExpectedConditions.elementToBeClickable(By.id("SelectorMtfId")));
		logger.log(LogStatus.PASS, "Login sucessful");
		System.out.println("Login sucessful");

	} // end of username_login()


	public  void testAddProvider(WebDriver driver) throws Exception {
		//String[] ssnname ;
		//String basessn = "800000000";	
		ssnNname = GenericLibs.time2ssn2name(ssnbase, "LN", "FN");
		Utility.syncClickableOneElementBy(driver, By.id("SelectorMtfId"));
		//assertEquals(true, (null != Utility.isElementPresentBy(driver, By.id("SelectorMtfId"), 150)) );
		// Utility.syncClickableOneElementBy(driver, By.id("SelectorMtfId"));
		System.out.println("Ready to add a provider");
		driver.findElement(By.id("SelectorMtfId")).click();
		driver.findElement(By.id("SelectorMtfId")).sendKeys("W0Q1AA");
		new Select(driver.findElement(By.id("SelectorMtfId"))).selectByVisibleText("W0Q1AA - MADIGAN ARMY MED CTR");

		driver.findElement(By.linkText("Credentialing")).click();
		driver.findElement(By.linkText("Provider Search")).click();
		Utility.syncClickableOneElementBy(driver, By.linkText("Add Provider"));

		driver.findElement(By.linkText("Add Provider")).click();
		Utility.syncClickableOneElementBy(driver, By.id("PersonIdentifier"));

		driver.findElement(By.id("PersonIdentifier")).click();
		driver.findElement(By.id("PersonIdentifier")).clear();
		//driver.findElement(By.id("PersonIdentifier")).sendKeys("800000075");
		driver.findElement(By.id("PersonIdentifier")).sendKeys(ssnNname[0]);

		driver.findElement(By.id("VerifyPersonIdentifier")).clear();
		//driver.findElement(By.id("VerifyPersonIdentifier")).sendKeys("800000075");
		driver.findElement(By.id("VerifyPersonIdentifier")).sendKeys(ssnNname[0]);

		driver.findElement(By.id("CitizenshipVisaType")).click();
		new Select(driver.findElement(By.id("CitizenshipVisaType"))).selectByVisibleText("US - United States of America");
		driver.findElement(By.id("CredProvider_Title")).click();
		new Select(driver.findElement(By.id("CredProvider_Title"))).selectByVisibleText("Dr.");
		driver.findElement(By.id("CredProvider_Title")).click();
		driver.findElement(By.id("CredProvider_LastName")).click();
		driver.findElement(By.id("CredProvider_LastName")).clear();
		//driver.findElement(By.id("CredProvider_LastName")).sendKeys("LNZZSEVENTYFIVE");
		driver.findElement(By.id("CredProvider_LastName")).sendKeys(ssnNname[1]);

		driver.findElement(By.id("CredProvider_FirstName")).click();
		driver.findElement(By.id("CredProvider_FirstName")).clear();
		//driver.findElement(By.id("CredProvider_FirstName")).sendKeys("FNZZSEVENTYFIVE");
		driver.findElement(By.id("CredProvider_FirstName")).sendKeys(ssnNname[2]);

		driver.findElement(By.id("MiddleName")).click();
		driver.findElement(By.id("MiddleName")).clear();
		driver.findElement(By.id("MiddleName")).sendKeys("B");
		driver.findElement(By.id("CredProvider_DateOfBirth")).click();
		driver.findElement(By.id("CredProvider_DateOfBirth")).clear();
		driver.findElement(By.id("CredProvider_DateOfBirth")).sendKeys("07/05/1980");
		new Select(driver.findElement(By.id("GenderCode"))).selectByVisibleText("Female");
		driver.findElement(By.id("ProviderType")).click();
		new Select(driver.findElement(By.id("ProviderType"))).selectByVisibleText("CNT - Contractor");
		driver.findElement(By.id("ProviderType")).click();
		driver.findElement(By.id("Occupation")).click();
		new Select(driver.findElement(By.id("Occupation"))).selectByVisibleText("Physician");
		driver.findElement(By.id("Occupation")).click();
		driver.findElement(By.xpath("//form[@id='AddProviderForm']/div/div/div/div[2]/div[10]/div/fieldset/div[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//input[@id='CredProvider_ProviderMilCivStatus'])[2]")).click();
		driver.findElement(By.id("add-submit")).click();
		AP_resubmitOnlyIfDuplicateSSN( driver,  logger,  ssnNname ) ;
		System.out.println("JCCQAS_AddProvider_Demo Submit for PersonIdentifier= " + ssnNname[0] + ", LastName=" + ssnNname[1]);

		//TODO: this is checkpoint
		Utility.syncTwoIdElements(driver, "alert-success", "alert-failure");
		aGoodWait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("alert-success")), ExpectedConditions.visibilityOfElementLocated(By.id("alert-failure"))));

		driver.findElement(By.id("alert-success")).click();
		logger.log(LogStatus.PASS, "Checkpoint: Successful Submit");

		driver.findElement(By.id("CredProvider_Remarks")).click();
		driver.findElement(By.id("CredProvider_Remarks")).clear();
		driver.findElement(By.id("CredProvider_Remarks")).sendKeys("Profile remarks here");
		driver.findElement(By.name("UpdateProfile")).click();
		//TODO: need to sync on this update
		if( null == Utility.isElementIdPresnt(driver, "alert-success", 150)) {

			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\UpdateProfileFailed_" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot1);
			logger.log(LogStatus.FAIL, logger.addScreenCapture(shot1));
		}
		else {
			logger.log(LogStatus.PASS, "Checkpoint: Update Profile");

			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\UpdateProfileInfo_" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot1);
			logger.log(LogStatus.INFO, logger.addScreenCapture(shot1));
		}
		
		if(false) { // not doing these for Demo

		//driver.findElement(By.linkText("Identification")).click();
		driver.findElement(By.linkText("Address")).click();
		Utility.syncClickableOneElementBy(driver, By.linkText("Address"));

		driver.findElement(By.linkText("Add")).click();
		Utility.syncClickableOneElementBy(driver, By.id("AddressTypeId"));
		System.out.println("Address Added");
		//Utility.isElementIdPresnt(driver, "AddressTypeId", 150);
		driver.findElement(By.id("AddressTypeId")).click();
		new Select(driver.findElement(By.id("AddressTypeId"))).selectByVisibleText("Home");
		driver.findElement(By.id("AddressTypeId")).click();
		driver.findElement(By.id("Address1Txt")).click();
		driver.findElement(By.id("Address1Txt")).clear();
		driver.findElement(By.id("Address1Txt")).sendKeys("12345 Main Street");
		driver.findElement(By.id("CityTxt")).click();
		driver.findElement(By.id("CityTxt")).clear();
		driver.findElement(By.id("CityTxt")).sendKeys("Richmond");
		new Select(driver.findElement(By.id("StateCd"))).selectByVisibleText("VA - Virginia");
		driver.findElement(By.id("PostalCd")).click();
		driver.findElement(By.id("PostalCd")).clear();
		driver.findElement(By.id("PostalCd")).sendKeys("10123");
		driver.findElement(By.id("PrimaryBln1")).click();
		driver.findElement(By.id("submitButton")).click();
		String xpathE = "//div[@id='alert-success']/strong";
		if(null != Utility.isElementXpathPresnt(driver, xpathE, 150)) {
			driver.findElement(By.xpath("//div[@id='alert-success']/strong")).click();
			logger.log(LogStatus.PASS, "Checkpoint: Update Address");
			System.out.println("Checkpoint: Update Address");

		}
		else {
			logger.log(LogStatus.FAIL, "Checkpoint: Update Address Failed; need to take snapshot");
			System.out.println("Checkpoint: Update Address Failed; need to take snapshot");
		}
		
		Utility.syncClickableOneElementBy(driver, By.linkText("Email"));
		driver.findElement(By.linkText("Email")).click();
		
		Utility.syncClickableOneElementBy(driver, By.linkText("Add"));
		driver.findElement(By.linkText("Add")).click();
		
		Utility.syncClickableOneElementBy(driver, By.id("PersonEmailTxt"));
		driver.findElement(By.id("PersonEmailTxt")).click();
		driver.findElement(By.id("PersonEmailTxt")).clear();
		driver.findElement(By.id("PersonEmailTxt")).sendKeys("baijoufu@valytics.com");
		driver.findElement(By.id("PrimaryEmail")).click();
		driver.findElement(By.id("submitButton")).click();
		Utility.isElementIdPresnt(driver, "alert-success", 150);
		driver.findElement(By.id("alert-success")).click();
		logger.log(LogStatus.PASS, "Checkpoint: Update Email");
		System.out.println("Checkpoint: Update Email");

		driver.findElement(By.linkText("Phone")).click();
		Utility.syncClickableOneElementBy(driver, By.linkText("Add"));

		driver.findElement(By.linkText("Add")).click();
		Utility.isElementIdPresnt(driver, "CommunicationTypeId", 150);

		driver.findElement(By.id("CommunicationTypeId")).click();
		new Select(driver.findElement(By.id("CommunicationTypeId"))).selectByVisibleText("Business Direct");
		driver.findElement(By.id("CommunicationTypeId")).click();
		driver.findElement(By.id("PersonCommunicationTxt")).click();
		driver.findElement(By.id("PersonCommunicationTxt")).clear();
		driver.findElement(By.id("PersonCommunicationTxt")).sendKeys("7035755052");
		driver.findElement(By.id("PrimaryBln")).click();
		driver.findElement(By.id("submitButton")).click();
		//Utility.isElementIdPresnt(driver, "alert-success", 150);
		Utility.syncClickableOneElementBy(driver, By.id("alert-success"));
		driver.findElement(By.id("alert-success")).click();
		logger.log(LogStatus.PASS, "Checkpoint: Update Phone");

		//		driver.findElement(By.linkText("NPI/Taxonomy")).click();
		//		driver.findElement(By.linkText("License")).click();
		//		driver.findElement(By.linkText("State")).click();
		//		driver.findElement(By.linkText("Add")).click();
		//		driver.findElement(By.linkText("Certification/Registration")).click();
		//		driver.findElement(By.xpath("(//a[contains(text(),'State')])[2]")).click();
		//		driver.findElement(By.linkText("Add")).click();
		//		driver.findElement(By.linkText("Board Certification")).click();
		//		driver.findElement(By.linkText("Add")).click();
		//		driver.findElement(By.linkText("DEA")).click();
		//		driver.findElement(By.linkText("Add")).click();
		//		driver.findElement(By.linkText("State CDS")).click();
		//		driver.findElement(By.linkText("Add")).click();
		driver.findElement(By.linkText("References")).click();
		Utility.syncClickableOneElementBy(driver, By.linkText("Add"));

		driver.findElement(By.linkText("Add")).click();
		//Utility.isElementNamePresnt(driver, "VerifiedCurrentBln", 150);
		Utility.syncClickableOneElementBy(driver, By.name("VerifiedCurrentBln"));


		driver.findElement(By.name("VerifiedCurrentBln")).click();
		driver.findElement(By.id("VerifiedTitleTxt")).click();
		new Select(driver.findElement(By.id("VerifiedTitleTxt"))).selectByVisibleText("Dr.");
		driver.findElement(By.id("VerifiedTitleTxt")).click();
		driver.findElement(By.id("VerifiedNameTxt")).click();
		driver.findElement(By.id("VerifiedNameTxt")).clear();
		driver.findElement(By.id("VerifiedNameTxt")).sendKeys("John Adams");
		driver.findElement(By.id("VerifiedReferenceTypeId")).click();
		new Select(driver.findElement(By.id("VerifiedReferenceTypeId"))).selectByVisibleText("Chief of Medical Staff");
		driver.findElement(By.id("VerifiedReferenceTypeId")).click();
		driver.findElement(By.id("VerifiedAddress1")).click();
		driver.findElement(By.id("VerifiedAddress1")).clear();
		driver.findElement(By.id("VerifiedAddress1")).sendKeys("23456 Main Street");
		driver.findElement(By.id("VerifiedCity")).click();
		driver.findElement(By.id("VerifiedCity")).clear();
		driver.findElement(By.id("VerifiedCity")).sendKeys("Richmond");
		driver.findElement(By.id("VerifiedState")).click();
		new Select(driver.findElement(By.id("VerifiedState"))).selectByVisibleText("VA - Virginia");
		driver.findElement(By.id("VerifiedZip")).click();
		driver.findElement(By.id("VerifiedZip")).clear();
		driver.findElement(By.id("VerifiedZip")).sendKeys("20123");
		driver.findElement(By.id("VerifiedCountryCd")).click();
		new Select(driver.findElement(By.id("VerifiedCountryCd"))).selectByVisibleText("United States");
		driver.findElement(By.id("VerifiedCountryCd")).click();
		driver.findElement(By.id("VerifiedPhoneTxt")).click();
		driver.findElement(By.id("VerifiedPhoneTxt")).clear();
		driver.findElement(By.id("VerifiedPhoneTxt")).sendKeys("7035755052");
		driver.findElement(By.id("VerifiedEmailTxt")).click();
		driver.findElement(By.id("VerifiedEmailTxt")).clear();
		driver.findElement(By.id("VerifiedEmailTxt")).sendKeys("baijoufu@valytics.com");
		driver.findElement(By.id("PeriodCoverBeginDate")).click();
		driver.findElement(By.xpath("//div[6]/div/table/tbody/tr/td")).click();
		driver.findElement(By.id("PeriodCoverBeginDate")).click();
		driver.findElement(By.id("PeriodCoverBeginDate")).clear();
		driver.findElement(By.id("PeriodCoverBeginDate")).sendKeys("05/27/2000");
		driver.findElement(By.id("PeriodCoverEndDate")).click();
		driver.findElement(By.xpath("//div[6]/div/table/tbody/tr/td")).click();
		driver.findElement(By.id("PeriodCoverEndDate")).click();
		driver.findElement(By.id("PeriodCoverEndDate")).clear();
		driver.findElement(By.id("PeriodCoverEndDate")).sendKeys("05/27/2015");
		driver.findElement(By.id("VerifiedRemarkTxt")).click();
		driver.findElement(By.id("VerifiedRemarkTxt")).clear();
		driver.findElement(By.id("VerifiedRemarkTxt")).sendKeys("good reference");

		driver.findElement(By.id("btnSave")).click();
		//assertEquals(closeAlertAndGetItsText(), "The verification of this record is not yet complete. Select 'Ok' to proceed in saving the information that has been entered and complete the verification of the record at another time. Select 'Cancel' to return to the form page and add a PSV.");
		assertEquals(closeAlertAndGetItsText(driver), "The verification of this record is not yet complete. Select 'Ok' to proceed in saving the information that has been entered and complete the verification of the record at another time. Select 'Cancel' to return to the form page and add a PSV.");
		//Utility.isElementIdPresnt(driver, "alert-success", 150);
		Utility.syncClickableOneElementBy(driver, By.id("alert-success"));

		driver.findElement(By.id("alert-success")).click();
		logger.log(LogStatus.PASS, "Checkpoint: Update Reference");

		driver.findElement(By.xpath("(//a[contains(text(),'Remarks')])[2]")).click();
		driver.findElement(By.linkText("Add")).click();
		Utility.isElementIdPresnt(driver, "RemarkType", 150);

		driver.findElement(By.id("RemarkType")).click();
		new Select(driver.findElement(By.id("RemarkType"))).selectByVisibleText("PCF Sent");
		//driver.findElement(By.id("RemarkType")).click();
		driver.findElement(By.id("RemarkText")).click();
		driver.findElement(By.id("RemarkText")).clear();
		driver.findElement(By.id("RemarkText")).sendKeys("Remarks for PCF Sent");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		//Utility.isElementIdPresnt(driver, "alert-success", 150);
		Utility.syncClickableOneElementBy(driver, By.id("alert-success"));

		driver.findElement(By.id("alert-success")).click();
		logger.log(LogStatus.PASS, "Checkpoint: Update Remarks");
		}

		//		driver.findElement(By.xpath("(//a[contains(text(),'Risk Management')])[4]")).click();
		//		driver.findElement(By.linkText("Pending Actions")).click();
		//		Utility.isElementIdPresnt(driver, "alert-information", 150);
		//		driver.findElement(By.id("alert-information")).click();
		//		logger.log(LogStatus.PASS, "Checkpoint: No pending Actions");

	} // end of testAddProvider()
	public void doContingencyTraining(WebDriver driver) throws Exception{
		driver.findElement(By.linkText("Contingency/Additional Training")).click();
		driver.findElement(By.linkText("Add")).click();
		Utility.isElementIdPresnt(driver, "ContingencyTrainingTypeId", 150);

		driver.findElement(By.id("ContingencyTrainingTypeId")).click();
		new Select(driver.findElement(By.id("ContingencyTrainingTypeId"))).selectByVisibleText("BLS - Basic Life Support");
		driver.findElement(By.id("ContingencyTrainingTypeId")).click();
		driver.findElement(By.id("ExpirationDt")).click();
		driver.findElement(By.id("ExpirationDt")).clear();
		driver.findElement(By.id("ExpirationDt")).sendKeys("12/31/2025");
		driver.findElement(By.id("RemarkTxt")).click();
		driver.findElement(By.id("RemarkTxt")).clear();
		driver.findElement(By.id("RemarkTxt")).sendKeys("Remarks for BLS");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Utility.isElementIdPresnt(driver, "alert-success", 150);

		driver.findElement(By.id("alert-success")).click();
		logger.log(LogStatus.PASS, "Contingency Training passed");
	} // end of doContingencyTraining()

	public void doDocumentUpload(WebDriver driver, String failedFileFullpath, String GoodFileFullpath) throws Exception {
		Utility.syncClickableOneElementBy(driver, By.linkText("Documents"));
		driver.findElement(By.linkText("Documents")).click();
		
		Utility.syncClickableOneElementBy(driver, By.linkText("Provider Documents"));

		driver.findElement(By.linkText("Provider Documents")).click();
		Utility.syncClickableOneElementBy(driver, By.linkText("Add"));

		driver.findElement(By.linkText("Add")).click();
		Utility.isElementIdPresnt(driver, "CredDocument_DocumentTypeId", 150);

		driver.findElement(By.id("CredDocument_DocumentTypeId")).click();
		new Select(driver.findElement(By.id("CredDocument_DocumentTypeId"))).selectByVisibleText("Other");
		driver.findElement(By.id("CredDocument_DescriptionTxt")).click();
		driver.findElement(By.id("CredDocument_DescriptionTxt")).clear();
		driver.findElement(By.id("CredDocument_DescriptionTxt")).sendKeys("Other to demo upload");
		//??????????????   The "file" is an input which pop-up a file search; and cannot be handled by WebDriver ??????
		//Click the label and wait for the Window Explorer pop-up
		driver.findElement(By.xpath("//label[. = 'Browse...']")).click(); // This is good, I see Window Explorer pop-up
		aGoodWait.until(ExpectedConditions.alertIsPresent());
		String xpathE;
		int[] tte = { KeyEvent.VK_TAB, KeyEvent.VK_TAB, KeyEvent.VK_ENTER} ;
		if(false) {
			driver.switchTo().alert().sendKeys(failedFileFullpath);
			SendVKSequence(tte);
	
			Utility.syncClickableOneElementBy(driver, By.id("docSave"));
	
			driver.findElement(By.id("docSave")).click();
			//String xpathE="//form[@id='docSaveForm']/div/div/div[2]/div[2]/div[2]/div/span[2]";
			xpathE="//span[. = 'You may only upload files of the following types (*.pdf, *.jpg, *.gif, *.tif).']";
			Utility.syncClickableOneElementBy(driver, By.xpath(xpathE));
			driver.findElement(By.xpath(xpathE)).click();
			logger.log(LogStatus.FAIL, "Checkpoint: Not able to Upload .TXT");
			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failedUploadText" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot1);
			logger.log(LogStatus.INFO, logger.addScreenCapture(shot1));
	
			driver.findElement(By.xpath("//label[. = 'Browse...']")).click();
			aGoodWait.until(ExpectedConditions.alertIsPresent());
		}
		driver.switchTo().alert().sendKeys(GoodFileFullpath);
		// int[] tte = { KeyEvent.VK_TAB, KeyEvent.VK_TAB, KeyEvent.VK_ENTER} ;
			
		SendVKSequence(tte);
		Utility.syncClickableOneElementBy(driver, By.id("docSave"));
		driver.findElement(By.id("docSave")).click();
		xpathE = "//div[@id='alert-success']/strong" ;
		Utility.syncClickableOneElementBy(driver, By.xpath(xpathE));
		driver.findElement(By.xpath(xpathE)).click();
		//driver.findElement(By.xpath("(//a[contains(text(),'Other')])[2]")).click();
		//assertEquals(closeAlertAndGetItsText(driver), "NOTICE:\nThis is confidential medical quality assurance information exempt from discovery and restricted from release under 10 U.S.C. 1102. Information contained in this report may be used only by authorized persons in the conduct of official business. Any unauthorized disclosure or misuse of information may result in civil penalties. If you are not the intended recipient of this report, please destroy all copies of the report after notifying the sender of your receipt of it.");
		logger.log(LogStatus.PASS, "Checkpoint: Able to Uplad .TIF");

	} // end of doDocumentUpload()
	public void SendVKSequence(int[] keysequence) throws Exception {
		Robot robot1 = new Robot();
		//System.out.println("VK_len=" + keysequence.length);
			
		for ( int s = 0; s < keysequence.length; s++) {
			robot1.keyPress(keysequence[s]);
			Thread.sleep(300);
	
		}
	}

	private String closeAlertAndGetItsText(WebDriver driver) {

		try {
			// bjf, 20180622 added
			WebDriverWait wait = new WebDriverWait(driver, 150); 
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	/*
	 * SR_ for Self Registration resbumitOnlyIfDuplicateSSN
	 */
	public static void SR_resubmitOnlyIfDuplicateSSN(WebDriver driver, ExtentTest logger, String[] ssnNname ) throws Exception {
		long MsecBefore = (new Date()).getTime();
		WebDriverWait wait = new WebDriverWait(driver, 150); // explicit 150 seconds
		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("alert-success")), ExpectedConditions.visibilityOfElementLocated(By.id("alert-failure"))));
		long MsecAfter = (new Date()).getTime();
		logger.log(LogStatus.INFO, "Submit response time in seconds=" + (MsecAfter - MsecBefore)/1000 );
		if(driver.findElements(By.id("alert-success")).size() != 0){ // this won't fail even if not sync.
			//if(driver.findElement(By.id("alert-success")).isDisplayed()) { //this fail if it did not show up fast enough
			logger.log(LogStatus.PASS, "SelfRegistration_Hardcoded Submitted for PersonIdentifier= " + ssnNname[0] + ", LastName=" + ssnNname[1]);

			//			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-success_" + ssnNname[0] + ".png";
			//			ValTestBase.takesScreenshot(driver, shot1);
			//			logger.log(LogStatus.INFO, logger.addScreenCapture(shot1));
		}

		else if(driver.findElements(By.id("alert-failure")).size() != 0){  //must be alter-failure

			//if ( driver.findElement(By.id("alert-failure")).isDisplayed()) {
			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failure_SRSSN" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot1);
			logger.log(LogStatus.WARNING, logger.addScreenCapture(shot1));

			//takesScreenshot("C:\\workspace\\Valytics-Selenium\\Com.Valytics\\Test Data and Results\\Screen Shots Results\\" + tfolder + "\\"+xlTD[xlTDIdx][1]+"_"+xlTC[i][0]+"_"+stepNum+".png");

			ssnNname = GenericLibs.time2ssn2name(ssnbase, "LN", "FN");

			driver.findElement(By.id("PersonIdentifier")).sendKeys(ssnNname[0]);
			driver.findElement(By.id("ConfirmPersonIdentifier")).sendKeys(ssnNname[0]);
			driver.findElement(By.id("LastName")).clear();
			driver.findElement(By.id("FirstName")).clear();
			driver.findElement(By.id("LastName")).sendKeys(ssnNname[1]);
			driver.findElement(By.id("FirstName")).sendKeys(ssnNname[2]);
			driver.findElement(By.id("process-submit")).click();		
			WebDriverWait wait2 = new WebDriverWait(driver, 150); // explicit 150 seconds
			wait2.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("alert-success")), ExpectedConditions.visibilityOfElementLocated(By.id("alert-failure"))));

			if(driver.findElement(By.id("alert-success")).isDisplayed()) {
				logger.log(LogStatus.PASS, "SelfRegistration_Hardcoded Submitted (retry) for PersonIdentifier= " + ssnNname[0] + ", LastName=" + ssnNname[1]);
			}
			else {
				String shot2 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failure_SRSSN2_" + ssnNname[0] + ".png";
				takesScreenshot(driver, shot2);
				//logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2), "Please investigate: SelfRegistration_Hardcoded Failed (retry) for PersonIdentifier= " + ssnNname[0] + ", LastName=" + ssnNname[1]);
				logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2));

			}

		}
		else {
			String shot2 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failure_SRSSN2_" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot2);
			//logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2), "Please investigate: Submit Neither Pass nor Fail; Must be one of them"); 
			logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2)); 

		}

	}

	/*
	 * AP_ for Add Provider resbumitOnlyIfDuplicateSSN
	 */
	public static void AP_resubmitOnlyIfDuplicateSSN(WebDriver driver, ExtentTest logger, String[] ssnNname ) throws Exception {
		long MsecBefore = (new Date()).getTime();
		WebDriverWait wait = new WebDriverWait(driver, 150); // explicit 150 seconds
		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("alert-success")), ExpectedConditions.visibilityOfElementLocated(By.id("alert-failure"))));
		long MsecAfter = (new Date()).getTime();
		logger.log(LogStatus.INFO, "Submit response time in seconds=" + (MsecAfter - MsecBefore)/1000 );
		if(driver.findElements(By.id("alert-success")).size() != 0){ // this won't fail even if not sync.
			//if(driver.findElement(By.id("alert-success")).isDisplayed()) { //this fail if it did not show up fast enough
			logger.log(LogStatus.PASS, "SelfRegistration_Hardcoded Submitted for PersonIdentifier= " + ssnNname[0] + ", LastName=" + ssnNname[1]);

			//			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-success_" + ssnNname[0] + ".png";
			//			ValTestBase.takesScreenshot(driver, shot1);
			//			logger.log(LogStatus.INFO, logger.addScreenCapture(shot1));
		}

		else if(driver.findElements(By.id("alert-failure")).size() != 0){  //must be alter-failure

			//if ( driver.findElement(By.id("alert-failure")).isDisplayed()) {
			String shot1 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failure_APSSN_" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot1);
			//.log(LogStatus.WARNING, logger.addScreenCapture(shot1), "Duplicate SSN, retry with new SSN, Names");
			logger.log(LogStatus.WARNING, logger.addScreenCapture(shot1));

			ssnNname = GenericLibs.time2ssn2name(ssnbase, "LN", "FN");

			driver.findElement(By.id("PersonIdentifier")).sendKeys(ssnNname[0]);
			driver.findElement(By.id("VerifyPersonIdentifier")).sendKeys(ssnNname[0]);
			driver.findElement(By.id("CredProvider_LastName")).clear();
			driver.findElement(By.id("CredProvider_FirstName")).clear();
			driver.findElement(By.id("CredProvider_LastName")).sendKeys(ssnNname[1]);
			driver.findElement(By.id("CredProvider_FirstName")).sendKeys(ssnNname[2]);
			driver.findElement(By.id("add-submit")).click();		
			WebDriverWait wait2 = new WebDriverWait(driver, 150); // explicit 150 seconds
			wait2.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("alert-success")), ExpectedConditions.visibilityOfElementLocated(By.id("alert-failure"))));

			if(driver.findElement(By.id("alert-success")).isDisplayed()) {
				logger.log(LogStatus.PASS, "SelfRegistration_Hardcoded Submitted (retry) for PersonIdentifier= " + ssnNname[0] + ", LastName=" + ssnNname[1]);
			}
			else {
				String shot2 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failure_APSSN2_" + ssnNname[0] + ".png";
				takesScreenshot(driver, shot2);
				//logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2), "Please investigate: SelfRegistration_Hardcoded Failed (retry) for PersonIdentifier= " + ssnNname[0] + "LastName=" + ssnNname[1]);
				logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2));
			}

		}
		else {
			String shot2 ="C:\\Workspace\\EclipseProjects\\JCCQAS\\Reports\\alter-failure_APSSN2_" + ssnNname[0] + ".png";
			takesScreenshot(driver, shot2);
			//logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2), "Please investigate: Submit Neither Pass nor Fail; Must be one of them"); 
			logger.log(LogStatus.FAIL, logger.addScreenCapture(shot2)); 

		}

	}
	public static void main(String[] argv) throws Exception {
		JCCQAS_AddProvider_Demo meResearch = new JCCQAS_AddProvider_Demo();
		InternetExplorerDriver driver;
		meResearch.beforeTest();
		meResearch.username_login("LNDODTWOF");
		driver = meResearch.driver;

		if(false) {
			meResearch.testAddProvider(driver);
		}
		else {
			System.out.println("argv[0]=" + argv[0]);
			meResearch.ssnNname[0] = argv[0];
			meResearch.searchExistingProvider(driver, argv[0]);
		}
		if(true) { // need to wait till able to handle Window pop-up for File Search and image/Sikuli
			String failedFileFullpath="C:\\Workspace\\EclipseProjects\\JCCQAS\\JCCQAS_Upload_download\\SimpleText1001.txt";
			String GoodFileFullpath = "C:\\Workspace\\EclipseProjects\\JCCQAS\\JCCQAS_Upload_download\\AssertObjectFunctionAutocompleteOptions.tif";
			meResearch.doDocumentUpload(driver, failedFileFullpath, GoodFileFullpath);
		}
		else {
			meResearch.doContingencyTraining(driver);
		}

		driver.findElement(By.linkText("Close")).click();
		meResearch.afterTest();

	}

	public void searchExistingProvider(WebDriver driver, String ssnToSearch) {
		Utility.syncClickableOneElementBy(driver, By.id("SelectorMtfId"));
		driver.findElement(By.id("SelectorMtfId")).click();
		driver.findElement(By.id("SelectorMtfId")).sendKeys("W0Q1AA");
		new Select(driver.findElement(By.id("SelectorMtfId"))).selectByVisibleText("W0Q1AA - MADIGAN ARMY MED CTR");

		driver.findElement(By.linkText("Credentialing")).click();
		driver.findElement(By.linkText("Provider Search")).click();
		Utility.isElementIdPresnt(driver, "ProviderSearchCriteria_ProviderIdentifierTxt", 150);
		driver.findElement(By.id("ProviderSearchCriteria_ProviderIdentifierTxt")).click();
		driver.findElement(By.id("ProviderSearchCriteria_ProviderIdentifierTxt")).clear();
		driver.findElement(By.id("ProviderSearchCriteria_ProviderIdentifierTxt")).sendKeys(ssnToSearch);
		driver.findElement(By.id("frmSearch")).submit();
		Utility.syncClickableOneElementBy(driver, By.id("dropdownMenu1"));
		driver.findElement(By.id("dropdownMenu1")).click();
		driver.findElement(By.xpath("//a[. = 'Open']")).click();;

		

	} // end of searchExistingProvider()

}


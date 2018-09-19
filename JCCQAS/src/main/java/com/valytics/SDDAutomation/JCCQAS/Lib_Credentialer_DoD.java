package com.valytics.SDDAutomation.JCCQAS;


import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.valytics.SDDAutomation.Utilities.Enum_JCCQAS.EAppCompletelProcesses;
import com.valytics.SDDAutomation.Utilities.Lib_JCCQAS;
import com.valytics.SDDAutomation.Utilities.ValTestBase;
import com.valytics.SDDAutomation.Utilities.Lib_JCCQAS.SaveType;
import com.valytics.SDDAutomation.Utilities.Utility;

import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class shall contain libaray methods that are reusable by (all) DoD credentialers;  Being a library class, it should not extend other class.
 * This class should not be used as a @Test; merely library methods.
 * 
 * 
 * @author Bai-Jou Fu 20180731
 *
 */
public class Lib_Credentialer_DoD {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	ValTestBase avtb = new ValTestBase();
	Lib_JCCQAS aLib_JCCQAS = new Lib_JCCQAS();
	ExtentTest logger;
	WebDriverWait aGoodWait;
	int n, uboundStatements, uboundTS;
	int x, uboundWhatSync;
	String[] statements = new String[100];
	String[][] testSteps = new String[100][2];
	String[] subLinkNames = new String[10];
	int[] whatSync = new int[10];
	String funcName;
	String subLinkName;
	String xmlSearch ; //Note: anXMLData is actually XMLData; use Specialty/elelmentx,  if is an XMLElement /elmentX
	String parentContainer;
	TestFactory myTest; 

	Lib_Credentialer_DoD( WebDriver driver, ExtentTest logger, WebDriverWait aGoodWait, TestFactory myTest){
		this.driver = driver;
		this.logger = logger;
		this.myTest = myTest;
		avtb.brandVTB(driver, logger, null);

		if(aGoodWait == null) {
			this.aGoodWait = new WebDriverWait(driver, 250);
		}
		else {
			this.aGoodWait = aGoodWait;
		}
	}
	//@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://www.katalon.com/";
		driver.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
	}


	public void credentialer_toVerify(Document allEApplications, boolean onlineReal) throws Exception{
		funcName="credentialer_toVerify";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		//		//int runFromStart = proStartAt.ordinal();
		//
		//		NodeList nEApplication = avtb.getElementsByXpath(allEApplications, "//EApplication");
		//
		//		int nE = nEApplication.getLength();
		//		Node thisEApp = nEApplication.item(0);
		//		Document anEApplicationXMLData = avtb.nodeGetDocument( thisEApp);
		//		Document anEApplicationXMLData = avtb.documentGetDocument(allEApplications, "//EApplication", 0);

		//String username="LNDODTWOF";
		String[] myLogin = avtb.myLoginFromXML(allEApplications, "Credentialer");
		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);

		credentialer_takePSV(allEApplications, onlineReal) ;
		credentialer_verifyOnly(allEApplications, onlineReal);


	} // end of credentialer_toVerify()

	private void appWorkListProviderFilter(String selItem) {
		funcName="appWorkListProviderFilter";
		System.out.printf( "%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		avtb.waitForPageLoad(driver);
		driver.findElement(By.linkText("Application Workflow")).click();
		driver.findElement(By.linkText("Work List")).click();
		avtb.waitForPageLoad(driver);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Provider Search by exact names
		//TODO: Do I need to wait for the text visible?  It seems the waitForPageLoad is getting many "Error executing JavaSCript"....
		new Select(driver.findElement(By.id("Providers"))).selectByVisibleText(selItem);
//		new Select(driver.findElement(By.id("Providers"))).selectByValue(value); // should I use this instead????

		System.out.printf("for provider=%s; to prevent JavaSCript error, hard wait before waitForPageLoad\n", selItem);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		avtb.waitForPageLoad(driver);
		System.out.println("Provider located for " + selItem);
		logger.log(Status.PASS, "Provider located for " + selItem);
	} // end of appWorkListProviderFilter()
	
	private String locateProvider(Document allEApplications, boolean onlineReal) {
		funcName="locateProvider";
		System.out.printf( "%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		String[] ssnNname = avtb.ssnFromXML(allEApplications);
		String selItem = ssnNname[4]; // e.g. "LNMAENQA, FNMAENQA";
		appWorkListProviderFilter(selItem);
		System.out.println(funcName + " returns " + selItem);
		return selItem;
	}
	private void credentialer_takePSV(Document allEApplications, boolean onlineReal) {
		funcName="credentialer_takePSV";
		System.out.printf( "%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		// always need to search for a specific provider
		String selItem = locateProvider(allEApplications, onlineReal);
		System.out.println("Ready to click link to process");


		// step 15, login as PAC
		//
		avtb.waitForPageLoad(driver);
		acceptNextAlert = true;
		By[] allElems = new By[] { By.linkText("Application Ready for Review"), By.linkText("Complete PSV")};

		System.out.printf("ready to wait for links of Ready for Review OR Complete PSV found\n");

		int retIdx = Utility.syncNElementsBy( driver,allElems, allElems.length);
		System.out.printf("Application Ready for Review OR Complete PSV found, retIdx=%d\n", retIdx);
		if( retIdx < 0 || 2 < retIdx) {
			System.out.printf("Did not find one of Application Ready for Review OR Complete PSV with retIdx=%d;  Must investigate the error!!!\n", retIdx);
		}
		//		if(driver.findElement(By.linkText("Application Ready for Review")).isDisplayed()){
		if(retIdx == 0){ // find the elem0
			//
			System.out.println("Application Ready for Review, found and will take responsibility for processing");
			driver.findElement(By.linkText("Application Ready for Review")).click();

			// cannot wait when alert is present.   avtb.waitForPageLoad(driver);
			if(Utility.isAlertPresent(driver)) { // bjf, 20180829, sometimes it doesn't show!!!!!!!!
			assertTrue(avtb.closeAlertAndGetItsText().matches("^Assign Credentialer\n\nWould you like to take responsibility for processing this application[\\s\\S]$"));

			avtb.waitForPageLoad(driver);
			logger.log(Status.PASS, "Take responsibility for applicant " + selItem);
			}
			driver.findElement(By.linkText("PSV")).click();
			driver.findElement(By.id("mainContent")).click();
			driver.findElement(By.xpath("//div[@id='main-panel']/div/div/div/fieldset/label[2]")).click();
			driver.findElement(By.xpath("//div[@id='main-panel']/div/div/div/fieldset/label")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			avtb.waitForPageLoad(driver);
			acceptNextAlert = true;
		}

		driver.findElement(By.linkText("Complete PSV")).click();
		
		logger.log(Status.PASS, "Complete PSV: will be doing this");
		acceptNextAlert = true;

		if(Utility.isAlertPresent(driver)) {
			assertTrue(avtb.closeAlertAndGetItsText().matches("^Assign PSV\n\nWould you like to take responsibility for the PSV of this application[\\s\\S]$"));
		}
		avtb.waitForPageLoad(driver);
	} // end of credentialer_takePSV()



	private void credentialer_verifyOnly(Document anEApplicationXMLData, boolean onlineReal) {
		funcName="credentialer_verifyOnly";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		avtb.waitForPageLoad(driver);

		System.out.println("Looking for NotVerifiedText");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		List<WebElement> allNotVerified = driver.findElements(By.xpath("//span[@class='NotVerifiedText']"));
		driver.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);

		int intSegs = allNotVerified.size();
		String[] allSegments = new String[intSegs];
		System.out.println("NotVerifiedText cnt=" + intSegs);

		for	( int j = 0 ; j < intSegs; j++) { // TODO: use iterator instead
			WebElement awe = allNotVerified.get(j);
			String myid = awe.getAttribute("id");
			System.out.printf("Not verified: id=%s\n",myid );
			allSegments[j] = myid;
		}
		for	( int j = 0 ; j < intSegs; j++) { // only the not verified show up here.
			switch(allSegments[j]) {
			case "verifiedStateLicense":
				verifyLicense(true, anEApplicationXMLData, "State", onlineReal);	
				break;
			case "verifiedBoardCertification":
				System.out.println("verifiedBoardCertification: calling verifySpecialty( )");
				verifySpecialty(true, anEApplicationXMLData, onlineReal);
				break;

			case "verifiedProfessionalEducation":
				verifyEducationTraining(true, anEApplicationXMLData, "ProfessionalEducation", onlineReal);	
				break;
			case "verifiedPostGrad":
				verifyEducationTraining(true, anEApplicationXMLData, "PostGraduateTraining", onlineReal);	

				break;
			case "verifiedReference":
				verifyReferences(true, anEApplicationXMLData, "Reference", onlineReal);
				break;
			case "verifiedDatabank":
				verifyDatabank(true, anEApplicationXMLData, onlineReal);
				break;
			case "verifiedWorkHistory":
				verifyWorkHistory(true, anEApplicationXMLData, onlineReal);
				break;
			default:
				System.out.printf("Not yet implemented: %s\n",  allSegments[j]);
			}
		}
		driver.findElement(By.linkText("Complete PSV")).click();

		avtb.waitForPageLoad(driver);
	} // end of credentialer_verifyOnly()

	public void Credentialer_toRoute( Document allEApplications, boolean onlineReal) throws Exception {
		funcName="Credentialer_toRoute";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		//		
		//		String[] ssnNname = avtb.ssnFromXML(allEApplications);
		//		//driver.findElement(By.linkText("W0Q1AA")).click(); // TODO: need to change to config.
		//		//		SelectorMtfId
		//		//		W0Q1AA   - MADIGAN ARMY MED CTR
		//		String selItem = ssnNname[1] + ", " + ssnNname[2]; // e.g. "LNMAENQA, FNMAENQA";
		//		appWorkListProviderFilter(selItem);
		String UicText = allEApplications.getElementsByTagName("UicText").item(0).getTextContent(); // there is only one UicText in the entire EApplication
		String[] myLogin = avtb.myLoginFromXML(allEApplications, "Credentialer");
		//String nameToSearch = myLogin[2] + ", " + myLogin[3] + "   ";
		String nameToSearch = myLogin[2] + ", " + myLogin[3];

		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);
		locateProvider(allEApplications,  onlineReal);


		// step 21, Route to Level 1 Reviewer, and PA
		System.out.printf("Ready to click the link for PSV Complete/Action Required\n");

		driver.findElement(By.linkText("PSV Complete/Action Required")).click();
		avtb.waitForPageLoad(driver);
		driver.findElement(By.linkText("Routing")).click();
		avtb.waitForPageLoad(driver);

		//		driver.findElement(By.xpath("//div[@id='sideNav']/ul/li[4]/a/span")).click();
		//		driver.findElement(By.xpath("//div[@id='navbar']/ul/li[2]/a/span")).click();
		//		driver.findElement(By.linkText("Work List")).click();
		//		driver.findElement(By.linkText("PSV Complete/Action Required")).click();

		//TODO:
		//NOTE: before Routing, the W0Q1AA must assign L1 and PA for the Adolescent Medicine privilege, which you select in the SelfRegistration.
		//
		//TODO: if link W0Q1AA is present and 
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		if(driver.findElement(By.id("alert-information")).isDisplayed()) {
			driver.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);

			n = -1;
			n += 1; testSteps[n][0] = "LinkText:" + UicText +":click" ; testSteps[n][1] = null;
			n += 1; testSteps[n][0] = "::waitpage" ; testSteps[n][1] = null;
			n += 1; testSteps[n][0] = "id:AvailableReviewers_400_15262:select" ; testSteps[n][1] = "UseThis:" + nameToSearch;

			n += 1; testSteps[n][0] = "xpath://input[@title='Select Reviewers']:click"; testSteps[n][1] = null;
			n += 1; testSteps[n][0] = "id:PaUserId:select" ; testSteps[n][1] = "UseThis:" + nameToSearch;
			n += 1; testSteps[n][0] = "id:submitButton:click" ; testSteps[n][1] = null;
			n += 1; testSteps[n][0] = "::waitpage" ; testSteps[n][1] = null;
			n += 1; testSteps[n][0] = "id:submitButton:click" ; testSteps[n][1] = null;
			n += 1; testSteps[n][0] = "::waitpage" ; testSteps[n][1] = null;
			uboundTS = n ;

			uboundWhatSync = -1;
			aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, "//", null, onlineReal, whatSync, uboundWhatSync);



			// need to do W0Q1AA
			//click "linktext:W0Q1AA:click"
			//"::waitpage"
			//"select id:AvailableReviewers_400_15262:clickvalue "LNDODTWO, FNDODTWO   "
			//xpath://input[@title='Select Reviewers']:click 
			// select "LNDODTWO, FNDODTWO   " from id='PaUserId"
			//id:PaUserId:clickvalue "LNDODTWO, FNDODTWO   "
			//  id:submitButton:click
			//::waitpage
			// another submitButton then another waitpage
		}

		if(false) {
			// ERROR: Caught exception [ERROR: Unsupported command [addSelection | id=AvailableReviewers_400_12891 | label=LNDODTWO, FNDODTWO]]
			driver.findElement(By.xpath("//option[@value='4133']")).click(); // 4133 is for LNDODTWO
			// 12877 is for General Surgery
			// click this to move LNDODTWO to the "Selected Reviewer" box
			driver.findElement(By.id("single-left-right_400_12877")).click(); 
			driver.findElement(By.id("PaUserId")).click();
			//new Select(driver.findElement(By.id("PaUserId"))).selectByVisibleText("LNDODTWO, FNDODTWO");

			String mULast = avtb.getElementsByXpath(allEApplications, "//PA_ModuleUser/LastName").item(0).getTextContent(); // Provider Applicant or Moduler User
			String mUFirst = avtb.getElementsByXpath(allEApplications, "//PA_ModuleUser/FirstName").item(0).getTextContent(); // Provider Applicant or Moduler User
			String modulerUser = mULast + ", " + mUFirst;

			new Select(driver.findElement(By.id("PaUserId"))).selectByVisibleText(modulerUser);

			driver.findElement(By.id("PaUserId")).click();
		}
		//		driver.findElement(By.id("submitButton")).click();
		//		avtb.waitForPageLoad(driver);
	} // end of Credentialer_toRoute()


	public void LevelOne_toReview(Document allEApplications, boolean onlineReal) throws Exception {
		funcName="LevelOne_toReview";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		// login as Level 1 Reviewer and Recommend
		String[] myLogin = avtb.myLoginFromXML(allEApplications, "L1Reviewer_ModuleUser");
		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);
		locateProvider(allEApplications,  onlineReal);
		//findElement(By.linkText("W0Q1AA")).click(); // TODO: need to change to config.



		// step 21, Route to Level 1 Reviewer, and PA
		avtb.waitForPageLoad(driver);
		driver.findElement(By.linkText("Application Ready for Review")).click();
		avtb.waitForPageLoad(driver);

		driver.findElement(By.id("btnRecommend")).click();
		driver.findElement(By.name("SubmitComments")).click();
		avtb.waitForPageLoad(driver);
	} // end of LevelOne_toReview()
	public void PA_toApprove(Document allEApplications, boolean onlineReal) throws Exception {
		// login as PA and approves
		funcName="PA_toApprove";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		//
		//		//driver.findElement(By.linkText("W0Q1AA")).click(); // TODO: need to change to config.
		//		String selItem = allEApplications[1] + ", " + allEApplications[2]; // e.g. "LNMAENQA, FNMAENQA";
		//		appWorkListProviderFilter(selItem);
		String[] myLogin = avtb.myLoginFromXML(allEApplications, "PA_ModuleUser");
		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);
		locateProvider(allEApplications,  onlineReal);
		// step 21, Route to Level 1 Reviewer, and PA
		avtb.waitForPageLoad(driver);
		//		driver.findElement(By.linkText("Application Workflow")).click();
		//		driver.findElement(By.linkText("Work List")).click();
		driver.findElement(By.linkText("Application Ready for Review")).click();
		avtb.waitForPageLoad(driver);

		acceptNextAlert = true;
		driver.findElement(By.id("reviewCompleted")).click();
		assertTrue(avtb.closeAlertAndGetItsText().matches("^Are you sure you have completed the review at this Facility/UIC[\\s\\S]$"));
		driver.findElement(By.id("btnApprove")).click();
		avtb.waitForPageLoad(driver);

		driver.findElement(By.name("SubmitComments")).click();
		avtb.waitForPageLoad(driver);

		driver.findElement(By.xpath("//div[@id='navbar']/ul/li[2]/a/span")).click();
		avtb.waitForPageLoad(driver);
	}
	public void PAC_toNotification(Document allEApplications, boolean onlineReal) throws Exception {
		//step 36, login as PAC to Notifications
		funcName="PAC_toNotification";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		String[] myLogin = avtb.myLoginFromXML(allEApplications, "PAC_ModuleUser");
		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);
		locateProvider(allEApplications,  onlineReal);
		//driver.findElement(By.linkText("W0Q1AA")).click(); // TODO: need to change to config.
		//		String selItem = ssnNname[1] + ", " + ssnNname[2]; // e.g. "LNMAENQA, FNMAENQA";
		//		appWorkListProviderFilter(selItem);

		// step 21, Route to Level 1 Reviewer, and PA
		avtb.waitForPageLoad(driver);
		driver.findElement(By.linkText("Decision Complete/Action Required")).click();
		driver.findElement(By.linkText("Notifications")).click();
		driver.findElement(By.id("NotifyProvider1")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection | id=Level1ReviewerIdsAvailable | label=LNDODTWO, FNDODTWO]]
		driver.findElement(By.xpath("//option[@value='4133']")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
		driver.findElement(By.id("submitButton")).click();
		avtb.waitForPageLoad(driver);

		driver.findElement(By.xpath("//form[@id='frmWorklist']/div")).click();
		driver.findElement(By.xpath("//div[@id='alert-success']/button/span")).click();
		driver.findElement(By.xpath("//div[@id='navbar-profile']/ul/li[3]/a/i[2]")).click();
		driver.findElement(By.linkText("Logout")).click();
		//driver.close();

	}
	public void Provider_toAcknowledge(Document allEApplications, boolean onlineReal) throws Exception {
		// step 40, login as a Provider
		funcName="Provider_toAcknowledge";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		String[] myLogin = avtb.providerLogonFromXML(allEApplications);
		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);

		//		driver.findElement(By.linkText("Logon")).click();
		//		driver.findElement(By.linkText("Username Logon")).click();
		//		driver.findElement(By.linkText("Yes, I understand the contents of the above Privacy Act Statements and Warnings.")).click();
		//
		//		//Might need to do more than one time to login
		//		driver.findElement(By.id("username")).clear();
		//
		//		driver.findElement(By.id("username")).sendKeys(username);
		//		driver.findElement(By.id("password")).click();
		//		driver.findElement(By.id("password")).clear();
		//		driver.findElement(By.id("password")).sendKeys(username);
		//		driver.findElement(By.xpath("//button[@type='submit']")).click();
		//		driver.findElement(By.id("WorkListStatusText")).click();
		//		driver.findElement(By.xpath("//form[@id='frmWorkList']/div/div/div/div[2]/div")).click();
		//		driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
		//		driver.findElement(By.xpath("//html")).click();
		avtb.waitForPageLoad(driver);
		driver.findElement(By.linkText("Application Notification")).click();
		avtb.waitForPageLoad(driver);
		driver.findElement(By.linkText("Acknowledge")).click();
		avtb.waitForPageLoad(driver);

		driver.findElement(By.xpath("//form[@id='AcknowledgeForm']/div/div[2]/div[2]/div/div/fieldset/div/label")).click();
		driver.findElement(By.id("btnSubmit")).click();
		avtb.waitForPageLoad(driver);
//		
//		driver.findElement(By.xpath("//div[@id='navbar-profile']/ul/li/a/i[2]")).click();
//		driver.findElement(By.linkText("Logout")).click();
	} // end of Provider_toAcknowledge()
	public void PAC_toCompleteApp(Document allEApplications, boolean onlineReal) throws Exception {
		funcName="PAC_toCompleteApp";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		String[] myLogin = avtb.myLoginFromXML(allEApplications, "PAC_ModuleUser");
		String username = myLogin[0];
		myTest.username_Login(driver, logger, username, onlineReal);
		locateProvider(allEApplications,  onlineReal);

		//		driver.findElement(By.linkText("Logon")).click();
		//		driver.findElement(By.linkText("Username Logon")).click();
		//		driver.findElement(By.linkText("Yes, I understand the contents of the above Privacy Act Statements and Warnings.")).click();
		//		driver.findElement(By.id("username")).clear();
		//		driver.findElement(By.id("username")).sendKeys(login_name);
		//		driver.findElement(By.id("password")).clear();
		//		driver.findElement(By.id("password")).sendKeys(login_name);
		//		driver.findElement(By.xpath("//button[@type='submit']")).click();
		//		driver.findElement(By.linkText("Application Workflow")).click();
		//		driver.findElement(By.linkText("Work List")).click();
		driver.findElement(By.linkText("Acknowledgement Received")).click();
		avtb.waitForPageLoad(driver);

		driver.findElement(By.linkText("Complete Application")).click();
		avtb.waitForPageLoad(driver);

		// NOTE: should do this before the Complete Application; Once Complete Application, the Level 1 Reviewer task disappears along with it
		if(false) { 
			driver.findElement(By.xpath("//div[@id='navbar-profile']/ul/li[3]/a/i[2]")).click();
			driver.findElement(By.id("Roles")).click();
			new Select(driver.findElement(By.id("Roles"))).selectByVisibleText("Level 1 Reviewer");
			driver.findElement(By.id("Roles")).click();
			driver.findElement(By.id("filter")).click();
			driver.findElement(By.xpath("//div[@id='navbar-profile']/ul/li[3]/a/i[2]")).click();
		}
		//driver.findElement(By.linkText("Logout")).click();
	} // end of PAC_toCompleteApp()
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	String pageName;
	int uboundSubLinkNames;
	String xmlpath;
	// mandatory segment
	public void verifyLicense(boolean toRun, Document anEApplicationXMLData, String whatSub, boolean onlineReal){
		if(! toRun){ return;}
		funcName="verifyLicense";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		pageName="License";
		n = -1;
		n += 1; subLinkNames[n] = "State" ;
		n += 1; subLinkNames[n] = "Unlicensed" ;
		uboundSubLinkNames =n;

		// already in place;  aLib_JCCQAS.linksToReach(driver, statements, new String[] {pageName}, onlineReal);

		//for ( int sublnidx = 0; sublnidx <= uboundSubLinkNames; sublnidx ++){
		//switch( subLinkNames[sublnidx]){
		switch( whatSub) {
		case "State":
			xmlpath = "//" + pageName + "/State";
			Document stateDoc[] = avtb.documentGetNDocuments(anEApplicationXMLData, xmlpath);
			if ( null == stateDoc) { break; }
			for (int itemx = 0 ; itemx < stateDoc.length; itemx++){
				verifyLicense_State(stateDoc[itemx], onlineReal);
			}			
			break;
		case "Unlicensed":
			xmlpath= "//" + pageName + "/Unlicensed";
			Document ulDoc[] = avtb.documentGetNDocuments(anEApplicationXMLData, xmlpath);
			if ( null == ulDoc) { break; }

			for (int itemx = 0 ; itemx < ulDoc.length; itemx++){
				verifyLicense_Unlicensed(ulDoc[itemx], onlineReal);
			}				
			break;
		default:
			//System.out.println("????????????????? ! yet implemented: " + subLinkNames[sublnidx] + " within verifyLicense");
			System.out.println("????????????????? ! yet implemented: " + whatSub + " within verifyLicense");
		}

		//}
		//Thread.sleep(1000); //wait0, 100

	} // end of verifyLicense()
	public void verifyLicense_State(Document anXMLData, boolean onlineReal) {
		funcName="verifyLicense_State";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		//System.out.println(anXMLData.ToString
		pageName="License";

		xmlSearch="" ; //Note: anXMLData is actually XMLElement; can! use State/elementX to search must use /elmentX
		parentContainer="driver.";
		String lnkTxt = avtb.getElementsByXpath(anXMLData, "//NumberTxt").item(0).getTextContent();
		//	moved into the testSteps
		//		aLib_JCCQAS.linksToReach(driver, statements, new String[] {lnkTxt}, onlineReal);
		//		avtb.waitForPageLoad(driver);

		n = -1;
		//n += 1; testSteps[n][0] = "partialLinkText:DynamicLink:click" ; testSteps[n][1] = "UseThis:" + lnkTxt; // same as the below
		n += 1; testSteps[n][0] = "partialLinkText:" + lnkTxt +":click" ; testSteps[n][1] = null;

		n += 1; testSteps[n][0] = "::waitpage" ; testSteps[n][1] = null;

		n += 1; testSteps[n][0] = "id:VerifiedNumberTxt:sendkeys" ; testSteps[n][1] = "NumberTxt";
		n += 1; testSteps[n][0] = "id:VerifiedStateCode:select" ; testSteps[n][1] = "StateCode";
		n += 1; testSteps[n][0] = "id:VerifiedFieldId:select" ; testSteps[n][1] = "VerifiedFieldId";
		n += 1; testSteps[n][0] = "id:VerifiedIssuanceDate:sendkeys" ; testSteps[n][1] = "IssuanceDate";
		n += 1; testSteps[n][0] = "id:VerifiedExpirationDate:sendkeys" ; testSteps[n][1] = "ExpirationDate";
		//	n += 1; testSteps[n][0] = "name:VerifiedIndefinite:clickTF" ; testSteps[n][1] = "Indefinite";
		n += 1; testSteps[n][0] = "id:VerifiedLicStatusId:select" ; testSteps[n][1] = "LicStatusId";
		n += 1; testSteps[n][0] = "id:VerifiedRemarks:sendkeys" ; testSteps[n][1] = "Remarks";


		uboundTS = n ;

		uboundWhatSync = -1;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, "//", anXMLData, onlineReal, whatSync, uboundWhatSync);
		boolean extraFields = false;
		if(anXMLData.getElementsByTagName("StateCode").item(0).getTextContent().subSequence(0, 2).equals("CO")){
			extraFields = true;
			System.out.println("State==CO, need to handle extra fields");
		}
		n = -1;
		n += 1; testSteps[n][0] = "name:StandingBln:clickTF" ; testSteps[n][1] = "yes";
		if(extraFields) {
			n += 1; testSteps[n][0] = "name:VerifiedAdmWaiver:clickTF" ; testSteps[n][1] = "yes";
		}

		n += 1; testSteps[n][0] = "name:Current:clickTF" ; testSteps[n][1] = "yes";
		n += 1; testSteps[n][0] = "name:Unrestricted:clickTF" ; testSteps[n][1] = "yes";
		n += 1; testSteps[n][0] = "name:PreviousAA:clickTF" ; testSteps[n][1] = "no";
		if(extraFields) {
			n += 1; testSteps[n][0] = "name:InGoodStandingWhenExpiredActive:clickTF" ; testSteps[n][1] = "yes";
		}
		n += 1; testSteps[n][0] = "name:PendingAA:clickTF" ; testSteps[n][1] = "no";
		uboundTS = n ;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, null, null, onlineReal, whatSync, uboundWhatSync);
		psv_roc(onlineReal);
		save_psv_roc(onlineReal);
		//		acceptNextAlert = false;
		//		driver.findElement(By.id("btnSave")).click();
		//		Utility.waitTillAlert(driver, logger, 100);
		//		//		// If incomplete, the message is "The verification of the record is not yet complete. ...."
		//		//		//assertEquals(avtb.closeAlertAndGetItsText(), "You MUST quality control the scanned image. Do not select <Ok> to save this document to the permanent record before you have quality controlled it. Assure it is readable and confirm the data being saved is accurate. If you have not performed this quality review, select <Cancel> and complete the review. Once the review has been completed select <Ok> to continue saving the record.");
		//		//		acceptNextAlert = true;
		//		//		//driver.findElement(By.id("btnSave")).click();
		//		//		assertEquals(avtb.closeAlertAndGetItsText(), "You MUST quality control the scanned image. Do not select <Ok> to save this document to the permanent record before you have quality controlled it. Assure it is readable and confirm the data being saved is accurate. If you have not performed this quality review, select <Cancel> and complete the review. Once the review has been completed select <Ok> to continue saving the record.");
		//		//		avtb.waitForPageLoad(driver);
		//		//		try {
		//		//			Thread.sleep(100);
		//		//		} catch (InterruptedException e) {
		//		//			// TODO Auto-generated catch block
		//		//			e.printStackTrace();
		//		//		}
		//		driver.switchTo().alert().accept();
		//		avtb.waitForPageLoad(driver);

		//aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);

	} //verifyLicense_State()


	public void verifyLicense_Unlicensed(Document anXMLData, boolean onlineReal){
		// not yet work on this
		funcName="verifyLicense_Unlicensed";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		pageName="Unlicensed";

		xmlSearch="" ; //Note: anXMLData is actually XMLElement; can! use State/elementX to search must use /elmentX

		parentContainer="driver.";

		aLib_JCCQAS.linksToReach(driver, statements, new String[] {"Unlicensed"} , onlineReal);
		n = -1;
		n += 1; testSteps[n][0] = "id:UnlicensedReasonId:select" ; testSteps[n][1] = "UnlicensedReasonId";
		n += 1; testSteps[n][0] = "id:StateCd:select" ; testSteps[n][1] = "StateCd";
		n += 1; testSteps[n][0] = "WebElement(\"AddState:click"; ; testSteps[n][1] = "AddState";
		n += 1; testSteps[n][0] = "id:RemarkTxt:sendkeys" ; testSteps[n][1] = "RemarkTxt" ;

		uboundTS = n ;

		aLib_JCCQAS.stepSteadyPacer(driver,  uboundTS,  testSteps, parentContainer,  "//",  anXMLData, onlineReal);
		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);

	} //end of verifyLicense_Unlicensed()


	// mandatory segment
	public void verifySpecialty(boolean toRun, Document anXMLData, boolean onlineReal){
		if(! toRun){ return;}
		funcName="verifySpecialty";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		pageName="Specialty";
		subLinkName="Specialty";
		xmlSearch="Specialty/" ; //Note: anXMLData is actually XMLData; use Specialty/elelmentx,  if is an XMLElement /elmentX
		parentContainer="driver.";

		String lnkTxt = avtb.getElementsByXpath(anXMLData, "//HptcSpecialtyId").item(0).getTextContent();
		//		aLib_JCCQAS.stepSteadyStatements(driver, uboundStatements, statements, onlineReal);	
		aLib_JCCQAS.linksToReach(driver, statements, new String[] {lnkTxt}, onlineReal);
		avtb.waitForPageLoad(driver);

		n = -1;
		n += 1; testSteps[n][0] = "id:VerifiedPractitionerTypeId:select" ; testSteps[n][1] = "PractitionerTypeId" ; //1
		n += 1; testSteps[n][0] = "id:VerifiedHptcSpecialtyId:select" ; testSteps[n][1] = "HptcSpecialtyId" ; //2
		n += 1; testSteps[n][0] = "id:VerifiedHptcSubSpecialtyId:select" ; testSteps[n][1] = "HptcSubSpecialtyId" ; //3 NOTE: 20180727, SubSpecialty instead of Subspecialty in all XML, and WebPage
		//n += 1; testSteps[n][0] = "id:VerifiedSpecialtyLevelId:select" ; testSteps[n][1] = "SpecialtyLevelId" ; //4
		n += 1; testSteps[n][0] = "linkText:Find:click"; ; testSteps[n][1] = "Find" ; //NOTE special treatment forLink: if value=Click { Click otherwise do nothing //5
		n += 1; testSteps[n][0] = "id:boardSearchField:sendkeys" ; testSteps[n][1] = "boardSearchField" ; //9
		//n += 1; testSteps[n][0] = "id:selectedCertifyingBoard:select" ; testSteps[n][1] = "selectedCertifyingBoard" ; //6	
		n += 1; testSteps[n][0] = "id:SearchSubmit:click"; ; testSteps[n][1] = "Search" ; //6	
		n += 1; testSteps[n][0] = "linkText:DynamicLink:click"; ; testSteps[n][1] = "DynamicLinkClick" ; //NOTE special treatment forLink: if value=Click { Click otherwise do nothing //8	
		n += 1; testSteps[n][0] = "id:VerifiedCertificateNumberTxt:sendkeys" ; testSteps[n][1] = "CertificateNumberTxt" ; //9
		n += 1; testSteps[n][0] = "id:VerifiedCertificationDt:sendkeys" ; testSteps[n][1] = "CertificationDt" ; //10
		n += 1; testSteps[n][0] = "id:VerifiedLatestRecertificationDt:sendkeys" ; testSteps[n][1] = "LatestRecertificationDt";	//11
		n += 1; testSteps[n][0] = "id:VerifiedExpirationDt:sendkeys" ; testSteps[n][1] = "ExpirationDt" ; //12
		n += 1; testSteps[n][0] = "name:VerifiedExpirationIndefinite:clickTF" ; testSteps[n][1] = "IndefiniteBln" ; //13
		n += 1; testSteps[n][0] = "id:VerifiedMocReverificationDt:sendkeys" ; testSteps[n][1] = "MocReverificationDt" ; //14
		n += 1; testSteps[n][0] = "name:VerifiedMocParticipatingBln:clickTF" ; testSteps[n][1] = "MocParticipatingBln" ; //15
		n += 1; testSteps[n][0] = "name:MocRequirementBln:clickTF" ; testSteps[n][1] = "MocRequirementBln" ; //16		
		n += 1; testSteps[n][0] = "id:VerifiedRemarkTxt:sendkeys" ; testSteps[n][1] = "RemarkTxt" ; //17
		//

		uboundTS = n ;
		//		x=-1;
		//		x=x+1 ; whatSync[x] = 1 ;//synchronize on HptcSpecialtyId
		//		x=x+1 ; whatSync[x] = 2 ;//synchronize on HptcSubspecialtyId
		//		uboundWhatSync=x;
		uboundWhatSync = -1;
		xmlpath = "//" + pageName;
		Document sDoc = avtb.documentGetDocument(anXMLData, xmlpath, 0);
		aLib_JCCQAS.stepSteadyPacerSyncN(driver,  uboundTS,  testSteps, parentContainer,  "//",  sDoc, onlineReal, whatSync, uboundWhatSync);
		psv_roc(onlineReal);
		save_psv_roc(onlineReal);
		//	aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);
		//		n = -1;
		//		n += 1; statements[n]="driver.findElement(By.id(\"Save:click";
		//		n += 1; statements[n]="// need to sync page// driver.findElement().sync() ;";
		//		uboundStatements=n;
		//		aLib_JCCQAS.stepSteadyStatements(driver, uboundStatements, statements, onlineReal);	
		//Thread.sleep(1000); //wait0, 100

	} // end of verifySpecialty()
	// mandatory segment
	public void verifyEducationTraining(boolean toRun, Document anXMLData, String whatSub, boolean onlineReal) {
		if(! toRun) { return; }
		funcName="verifyEducationTraining";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		pageName="EducationTraining";

		subLinkNames[0] = "ProfessionalEducation";
		subLinkNames[1] = "ECFMG";
		subLinkNames[2] = "PostGraduateTraining";
		uboundSubLinkNames = 2;
		//aLib_JCCQAS.linksToReach(driver, statements, new String[] {"Education/Training"}, onlineReal);

		//for (int sublnIdx = 0; sublnIdx <= uboundSubLinkNames; sublnIdx++){
		switch(whatSub){
		case "ProfessionalEducation":
			xmlpath= "//" + pageName + "/ProfessionalEducation";
			//		NodeList aNL = avtb.getElementsByXpath(anXMLData, xmlpath);\n cnt = aNL.getLength() 
			//				aNL = avtb.getElementsByXpath(anXMLData, xmlpath);
			//				cnt = aNL.getLength();
			Document[] peDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
			if( peDoc == null) { break; }
			for (int  itemx = 0; itemx < peDoc.length; itemx++){
				verifyEducationTraining_ProfessionalEducation(peDoc[itemx], onlineReal);
			} 
			break;
		case "ECFMG":
			xmlpath= "//" + pageName + "/ECFMG";
			//		NodeList aNL = avtb.getElementsByXpath(anXMLData, xmlpath);\n cnt = aNL.getLength() 
			Document[] ecDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
			if( ecDoc == null) { break; }
			for (int  itemx = 0; itemx < ecDoc.length; itemx++){
				verifyEducationTraining_ECFMG(ecDoc[itemx], onlineReal);
			}
			break;
		case "PostGraduateTraining":
			xmlpath= "//" + pageName + "/PostGraduateTraining";
			Document[] ptDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
			if( ptDoc == null) { break; }
			for (int  itemx = 0; itemx < ptDoc.length; itemx++){
				verifyEducationTraining_PostGraduateTraining(ptDoc[itemx], onlineReal);
			}	
			break;
		default:
			System.out.print("????????????????? Not yet implemented: " + whatSub + " within " + pageName);
		}

		//}

	} //  end of verifyEducationTraining()

	public void verifyEducationTraining_ProfessionalEducation(Document anXMLData, boolean onlineReal){
		funcName="verifyEducationTraining_ProfessionalEducation";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		//System.out.println(anXMLData.ToString
		pageName="EducationTraining";

		xmlSearch="" ;//Note: anXMLData is actually XMLElement; can! use State/elementX to search must use /elmentX
		parentContainer = "driver.";
		String lnkTxt = avtb.getElementsByXpath(anXMLData, "//DegreeId").item(0).getTextContent();
		String[] noSpace = lnkTxt.split(" ");
		int descStartAt  = noSpace[0].length() + 3;
		lnkTxt = lnkTxt.substring(descStartAt); // "MD - Doctor of Medicine"; better to start at after the 2nd space

		aLib_JCCQAS.linksToReach(driver, statements, new String[] {lnkTxt}, onlineReal);
		avtb.waitForPageLoad(driver);
		n = -1;
		n += 1; testSteps[n][0] = "id:VerifiedDegreeType:select" ; testSteps[n][1] = "DegreeType"; //1
		n += 1; testSteps[n][0] = "name:VerifiedIsQualifying:clickTF" ; testSteps[n][1] = "QualifyingBln" ; //2
		n += 1; testSteps[n][0] = "name:VerifiedIsForeignTrained:clickTF" ; testSteps[n][1] = "ForeignTrainedBln"; //2-1
		n += 1; testSteps[n][0] = "name:VerifiedIsFifthPathway:clickTF" ; testSteps[n][1] = "FifthPathwayBln"; //2-2
		////////////????????????????????????????????????????????????
		// Need to //Thread.sleep(1000); //waitforthe table of DegreeId to be loaded/populated; otherwise the table is emptyp; No choices are available
		n += 1; testSteps[n][0] = "id:VerifiedDegreeId:select" ; testSteps[n][1] = "DegreeId";	//3
		//if USUHS { skip the search
		n += 1; testSteps[n][0] = "linkText:USUHS:click"; ; testSteps[n][1] = "USUHS";	//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4
		//} else {, i.e. ! USUSH
		n += 1; testSteps[n][0] = "linkText:Find:click"; ; testSteps[n][1] = "Find";	//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4			
		n += 1; testSteps[n][0] = "id:institutionSearchField:sendkeys" ; testSteps[n][1] = "institutionSearchField"; //5	
		n += 1; testSteps[n][0] = "id:SearchSubmit:click"; ; testSteps[n][1] = "Search" ;//5	
		//n += 1; testSteps[n][0] = "linkText:DynamicLink:click" ; testSteps[n][1] = "DynamicLinkClick"	;//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4	
		n += 1; testSteps[n][0] = "linkText:DynamicLink:click" ; testSteps[n][1] = "DynamicLinkClick"	;//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4	
		//}; usually 

		//		n += 1; testSteps[n][0] = "id:InstitutionAddress1Txt:sendkeys" ; testSteps[n][1] = "InstitutionAddress1Txt"; //5
		//		n += 1; testSteps[n][0] = "id:InstitutionAddress2Txt:sendkeys" ; testSteps[n][1] = "InstitutionAddress2Txt";	//6
		//		n += 1; testSteps[n][0] = "id:InstitutionCityTxt:sendkeys" ; testSteps[n][1] = "InstitutionCityTxt"; //7
		//		n += 1; testSteps[n][0] = "id:InstitutionStateCd:select" ; testSteps[n][1] = "InstitutionStateCd"; //8
		//		n += 1; testSteps[n][0] = "id:InstitutionPostalTxt:sendkeys" ; testSteps[n][1] = "InstitutionPostalTxt"; //9
		//		n += 1; testSteps[n][0] = "id:InstitutionCountryCd:select" ; testSteps[n][1] = "InstitutionCountryCd"; //10	
		n += 1; testSteps[n][0] = "id:VerifiedStartDate:sendkeys" ; testSteps[n][1] = "StartDt"; //11
		n += 1; testSteps[n][0] = "id:VerifiedCompletionDate:sendkeys" ; testSteps[n][1] = "CompletionDt"; //12

		n += 1; testSteps[n][0] = "id:VerifiedCompletedId:select" ; testSteps[n][1] = "CompletedId"; //13
		n += 1; testSteps[n][0] = "id:VerifiedGraduationDt:sendkeys" ; testSteps[n][1] = "GraduationDt"; //14
		//		n += 1; testSteps[n][0] = "id:RemarkTxt:sendkeys" ; testSteps[n][1] = "RemarkTxt" ; //15

		//driver.findElement(By.linkText("Education/Training")).click();

		uboundTS = n ;
		//		x=-1;
		//		x=x+1 ; whatSync[x] = 0 ;//n=0 is forDegreeType
		//		x=x+1 ; whatSync[x] = 4; //n=0 is DegreeID
		//		uboundWhatSync=x;
		uboundWhatSync = -1;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver,  uboundTS,  testSteps, parentContainer,  "//",  anXMLData, onlineReal, whatSync, uboundWhatSync)	;
		psv_roc(onlineReal);
		save_psv_roc(onlineReal);
		//aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, "id:InstitutionCityTxt:click", onlineReal); // extra step to clean up the Date pop-ups
		//		n = -1;
		//		n += 1; statements[n]=statementSaveClick(1);
		//		uboundStatements=n;
		//		aLib_JCCQAS.stepSteadyStatements(driver, uboundStatements, statements, onlineReal);
		////Thread.sleep(1000); //wait0, 100
		//driver.findElement(By.linkText("Position")).click();
		//driver.findElement().WebList("PractitionerTypeId").Select "Physician"
		//driver.findElement().WebRadioGroup("RequestingPrivilegesBln").Select "true"
		//driver.findElement().WebList("PrivilegeTypeCd").Select "Regular"
		//driver.findElement().WebList("AppointmentTypeCd").Select "Consultant"
		//driver.findElement().WebCheckBox("Mtfs[0].RequestedBln").sendkey( "ON"
		//driver.findElement(By.id("Save")).click();
	} // end of verifyEducationTraining_ProfessionalEducation()

	public void verifyEducationTraining_ECFMG(Document anXMLData, boolean onlineReal){
		funcName="verifyEducationTraining_ECFMG";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		//System.out.println(anXMLData.ToString
		pageName="EducationTraining";

		xmlSearch=""; //Note: anXMLData is actually XMLElement; can! use State/elementX to search must use /elmentX
		parentContainer="driver.";

		aLib_JCCQAS.linksToReach(driver, statements, new String[] {"ECFMG", "Add"}, onlineReal);
		n = -1;
		n += 1; testSteps[n][0] = "id:CertificateNumber:sendkeys" ; testSteps[n][1] = "CertificateNumber" ;//4
		n += 1; testSteps[n][0] = "name:IndefiniteBln:clickTF" ; testSteps[n][1] = "IndefiniteBln" ; //5	
		n += 1; testSteps[n][0] = "id:DateTaken:sendkeys" ; testSteps[n][1] = "DateTaken" ; //7
		n += 1; testSteps[n][0] = "id:CertifiedDate:sendkeys" ; testSteps[n][1] = "CertifiedDate"	; //8
		n += 1; testSteps[n][0] = "id:ExpirationDate:sendkeys" ; testSteps[n][1] = "ExpirationDate" ; //9
		n += 1; testSteps[n][0] = "id:Remarks:sendkeys" ; testSteps[n][1] = "Remarks" ; //10

		//driver.findElement(By.id("Remarks").sendkey( "Remarks forECFMG" + vbLf + \"
		//driver.findElement(By.linkText("Education/Training")).click(); //1
		//driver.findElement(By.linkText("ECFMG")).click(); //2
		//driver.findElement(By.linkText("Add")).click(); //3
		//driver.findElement(By.id("CertificateNumber").sendkey( "01122111" ; //4
		//driver.findElement().WebRadioGroup("IndefiniteBln").Select "true" ; //5
		//driver.findElement().WebRadioGroup("IndefiniteBln").Select "false" ; //6
		//driver.findElement(By.id("DateTaken").sendkey( "1/1/2017" ; //7
		//driver.findElement(By.id("CertifiedDate").sendkey( "1/5/2017" ; //8
		//driver.findElement(By.id("ExpirationDate").sendkey( "1/5/2020" ; //9
		//driver.findElement(By.id("Remarks").sendkey( "remakr ECFMG" ; //10
		//driver.findElement(By.id("Save")).click(); //11

		uboundTS = n ;
		aLib_JCCQAS.stepSteadyPacer(driver,  uboundTS,  testSteps, parentContainer,  "//",  anXMLData, onlineReal);	

		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);
		//		if(onlineReal) {
		//			//		statement99 = parentContainer + ".id:Save:click";
		//			//		execute statement99
		//			driver.findElement(By.id("Save")).click(); //WebButton
		//			// need to sync // need to sync page// driver.findElement().sync() ;	
		//		} else {
		//			statement99 = "driver.findElement(By.id(\"Save:click";
		//			System.out.println("??? Not Online, Skip this: " + statement99);
		//		}
		////Thread.sleep(1000); //wait0, 100

	} // end of verifyEducationTraining_ECFMG()

	public void verifyEducationTraining_PostGraduateTraining(Document anXMLData, boolean onlineReal){
		funcName="verifyEducationTraining_PostGraduateTraining";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		pageName="EducationTraining";
		subLinkName = "Post Graduate Training";

		xmlSearch=""; //Note: anXMLData is actually XMLElement; can! use State/elementX to search must use /elmentX
		//String parentContainer="driver.";
		String parentContainer="driver.";

		String lnkTxt = avtb.getElementsByXpath(anXMLData, "//FieldOfStudy").item(0).getTextContent();
		//		String[] noSpace = lnkTxt.split(" ");
		//		int descStartAt  = noSpace[0].length() + 3;
		//		lnkTxt = lnkTxt.substring(descStartAt); // "MD - Doctor of Medicine"; better to start at after the 2nd space

		aLib_JCCQAS.linksToReach(driver, statements, new String[] {lnkTxt}, onlineReal);
		avtb.waitForPageLoad(driver);
		//aLib_JCCQAS.linksToReach(driver, statements, new String[] {subLinkName, "Add"}, onlineReal);
		n = -1;
		n += 1; testSteps[n][0] = "id:VerifiedOtherEducationTypeId:select" ; testSteps[n][1] = "EducationType"; //idx=0
		n += 1; testSteps[n][0] = "id:VerifiedFieldOfStudy:sendkeys" ; testSteps[n][1] = "FieldOfStudy"; //idx=1
		//if USUHS { skip the search
		n += 1; testSteps[n][0] = "linkText:USUHS:click"; ; testSteps[n][1] = "USUHS"	;//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4
		//} else {, i.e. ! USUSH
		n += 1; testSteps[n][0] = "linkText:Find:click"; ; testSteps[n][1] = "Find";	//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4			
		n += 1; testSteps[n][0] = "id:institutionSearchField:sendkeys" ; testSteps[n][1] = "institutionSearchField"; //5	
		n += 1; testSteps[n][0] = "id:SearchSubmit:click"; ; testSteps[n][1] = "Search"; //5	
		n += 1; testSteps[n][0] = "linkText:DynamicLink:click" ; testSteps[n][1] = "DynamicLinkClick";	//NOTE special treatment forLink: if value=Click { Click otherwise do nothing //4	
		//}; usually 
		//		n += 1; testSteps[n][0] = "id:InstitutionAddress1Txt:sendkeys" ; testSteps[n][1] = "InstitutionAddress1Txt"; //idx=7
		//		n += 1; testSteps[n][0] = "id:InstitutionAddress2Txt:sendkeys" ; testSteps[n][1] = "InstitutionAddress2Txt";	//6
		//		n += 1; testSteps[n][0] = "id:InstitutionCityTxt:sendkeys" ; testSteps[n][1] = "InstitutionCityTxt"; //7
		//		n += 1; testSteps[n][0] = "id:InstitutionStateCd:select" ; testSteps[n][1] = "InstitutionStateCd"; //8
		//		n += 1; testSteps[n][0] = "id:InstitutionPostalTxt:sendkeys" ; testSteps[n][1] = "InstitutionPostalTxt"; //9
		//		n += 1; testSteps[n][0] = "id:InstitutionCountryCd:select" ; testSteps[n][1] = "InstitutionCountryCd"; //10	
		n += 1; testSteps[n][0] = "id:VerifiedStartDt:sendkeys" ; testSteps[n][1] = "StartDt"; //11
		n += 1; testSteps[n][0] = "id:VerifiedEndDt:sendkeys" ; testSteps[n][1] = "EndDt"; //differs from ProfessionalTraining

		n += 1; testSteps[n][0] = "id:VerifiedCompletedId:select" ; testSteps[n][1] = "CompletedId"; //13
		n += 1; testSteps[n][0] = "id:VerifiedCompletionDt:sendkeys" ; testSteps[n][1] = "CompletionDt"; //differs too
		//		n += 1; testSteps[n][0] = "id:RemarksTxt:sendkeys" ; testSteps[n][1] = "RemarksTxt"; //15 //differs too
		//

		uboundTS = n ;
		x=-1;
		x=x+1 ; whatSync[x] = 0; //n=0 is forEducationType
		uboundWhatSync = x;

		aLib_JCCQAS.stepSteadyPacerSyncN(driver,  uboundTS,  testSteps, parentContainer,  "//",  anXMLData, onlineReal, whatSync, uboundWhatSync)	;

		psv_roc(onlineReal);
		save_psv_roc(onlineReal);
		//		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);
		//		n = -1;
		//		n += 1; statements[n]= statementSaveClick(1) ; //driver.findElement(By.id(\"Save:click";
		//		//n += 1; statements[n]="// need to sync page// driver.findElement().sync() ;";
		//		uboundStatements=n;
		//		aLib_JCCQAS.stepSteadyStatements(driver, uboundStatements, statements, onlineReal);
		////Thread.sleep(1000); //wait0, 100
	} // end of verifyEducationTraining_PostGraduateTraining()

	public void verifyReferences(boolean toRun, Document anXMLData, String whatSub,  boolean onlineReal) {
		if(! toRun) { return; }
		funcName="verifyReferences";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		pageName="References";
		//aLib_JCCQAS.linksToReach(driver, statements, new String[] {pageName}, onlineReal);	


		n = -1;
		n += 1; subLinkNames[n] = "Reference";
		uboundSubLinkNames = n;
		//for (int sublnIdx = 0; sublnIdx <= uboundSubLinkNames; sublnIdx++){
		switch(whatSub){
		case "Reference":
			xmlpath= "//" + pageName + "/Reference";
			Document[] refDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
			if( null == refDoc) { break; }
			for (int  itemx = 0; itemx < refDoc.length; itemx++){
				verifyReferences_Reference(refDoc[itemx], onlineReal);
			} 
			break;

		default:
			System.out.print("????????????????? Not yet implemented: " + whatSub + " within " + pageName);
		}

		//}

	} // 'addEducationTraining

	public void verifyReferences_Reference(Document anXMLData, boolean onlineReal) {
		funcName="verifyReferences_Reference";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		pageName="Reference";
		xmlSearch="" ;//Note: anXMLData is actually XMLElement; can! use State/elementX to search must use /elmentX
		parentContainer = "driver.";
		subLinkName = "Reference";

		String lnkTxt = avtb.getElementsByXpath(anXMLData, "//NameTxt").item(0).getTextContent();
		aLib_JCCQAS.linksToReach(driver, statements, new String[] {lnkTxt}, onlineReal);

		n = -1;
		//		n += 1; testSteps[n][0] = "name:CurrentBln:clickTF" ; testSteps[n][1] = "CurrentBln" ; //1

		n += 1; testSteps[n][0] = "id:VerifiedTitleTxt:select" ; testSteps[n][1] = "TitleTxt" ; //2
		n += 1; testSteps[n][0] = "id:VerifiedNameTxt:sendkeys" ; testSteps[n][1] = "NameTxt" ; //3
		n += 1; testSteps[n][0] = "id:VerifiedBusinessOccupationTxt:sendkeys" ; testSteps[n][1] = "BusinessOccupationTxt" ; //4
		n += 1; testSteps[n][0] = "id:VerifiedReferenceTypeId:select" ; testSteps[n][1] = "ReferenceTypeId" ; //5
		//		n += 1; testSteps[n][0] = "id:OtherPositionTxt:sendkeys" ; testSteps[n][1] = "OtherPositionTxt" ; //6
		//		n += 1; testSteps[n][0] = "id:Address1:sendkeys" ; testSteps[n][1] = "Address1" ; //7
		//		n += 1; testSteps[n][0] = "id:Address2:sendkeys" ; testSteps[n][1] = "Address2" ; //8
		//		n += 1; testSteps[n][0] = "id:Address3:sendkeys" ; testSteps[n][1] = "Address3" ; //9
		//		n += 1; testSteps[n][0] = "id:Address4:sendkeys" ; testSteps[n][1] = "Address4" ; //10
		//		n += 1; testSteps[n][0] = "id:City:sendkeys" ; testSteps[n][1] = "City" ; //11
		//		n += 1; testSteps[n][0] = "id:Province:sendkeys" ; testSteps[n][1] = "Province" ; //12
		//		n += 1; testSteps[n][0] = "id:State:select" ; testSteps[n][1] = "State" ; //13	
		//		n += 1; testSteps[n][0] = "id:Zip:sendkeys" ; testSteps[n][1] = "Zip" ; //14
		//		n += 1; testSteps[n][0] = "id:CountryCd:select" ; testSteps[n][1] = "CountryCd" ; //15
		//		n += 1; testSteps[n][0] = "id:PhoneTxt:sendkeys" ; testSteps[n][1] = "PhoneTxt" ; //16
		//		n += 1; testSteps[n][0] = "id:FaxTxt:sendkeys" ; testSteps[n][1] = "FaxTxt" ; //17
		//		n += 1; testSteps[n][0] = "id:EmailTxt:sendkeys" ; testSteps[n][1] = "EmailTxt" ; //18


		uboundTS = n ;
		uboundWhatSync = -1;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, "//", anXMLData, onlineReal, whatSync, uboundWhatSync);
		n = -1;
		n += 1; testSteps[n][0] = "name:PeriodCoverUnavailable:clickTF" ; testSteps[n][1] = "True" ;
		uboundTS = n ;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, null, null, onlineReal, whatSync, uboundWhatSync);
		psv_roc(onlineReal);
		save_psv_roc(onlineReal);

		//		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);

	} // end of verifyReferences_Reference()

	public void verifyDatabank(boolean toRun, Document anXMLData, boolean onlineReal) {
		if(! toRun) { return; }
		funcName="verifyDatabank";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		pageName="Databank";

		subLinkNames[0] = "NPDB";
		uboundSubLinkNames = 0;
		//The remaining two are not mandatory
		//		subLinkNames[1] = "FSMB";
		//		subLinkNames[2] = "Other Agency";
		//		uboundSubLinkNames = 2;
		//aLib_JCCQAS.linksToReach(driver, statements, new String[] {"Education/Training"}, onlineReal);

		for (int sublnIdx = 0; sublnIdx <= uboundSubLinkNames; sublnIdx++){
			switch(subLinkNames[sublnIdx]){
			case "NPDB":
			case "FSMB":
				//			xmlpath= "//" + pageName + "/ProfessionalEducation";
				//			//		NodeList aNL = avtb.getElementsByXpath(anXMLData, xmlpath);\n cnt = aNL.getLength() 
				//			//				aNL = avtb.getElementsByXpath(anXMLData, xmlpath);
				//			//				cnt = aNL.getLength();
				//			Document[] peDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
				//			if( peDoc == null) { break; }
				//			for (int  itemx = 0; itemx < peDoc.length; itemx++){
				verifyDatabank_NPDB_FSMB(subLinkNames[sublnIdx], onlineReal);
				//			} 

				break;

			case "Other Agency":
				//			xmlpath= "//" + pageName + "/PostGraduateTraining";
				//			Document[] ptDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
				//			if( ptDoc == null) { break; }
				//			for (int  itemx = 0; itemx < ptDoc.length; itemx++){
				verifyDatabank_OtherAgency(subLinkNames[sublnIdx], onlineReal);
				//			}	
				break;
			default:
				System.out.print("????????????????? Not yet implemented: " + subLinkNames[sublnIdx] + " within " + pageName);
			}

		}

	} //  end of verifyDatabank()
	public void verifyDatabank_NPDB_FSMB(String whatSub, boolean onlineReal) {
		funcName="verifyDatabank_NPDB_FSMB";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		aLib_JCCQAS.linksToReach(driver, statements, new String[] {whatSub}, onlineReal);

		n = -1;
		n += 1; testSteps[n][0] = "id:RequestedDt:sendkeys" ; testSteps[n][1] = avtb.todayString(-3) ;
		n += 1; testSteps[n][0] = "id:ResultsDt:sendkeys" ; testSteps[n][1] = avtb.todayString(-1) ;
		n += 1; testSteps[n][0] = "name:ResultsCd:clickvalue" ; testSteps[n][1] = "2" ;

		uboundTS = n ;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, null, null, onlineReal, whatSync, uboundWhatSync);
		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);

		//	psv_roc(onlineReal);
		//	save_psv_roc(onlineReal);
	}
	public void verifyDatabank_OtherAgency(String whatSub, boolean onlineReal) {
		funcName="verifyDatabank_OtherAgency";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		aLib_JCCQAS.linksToReach(driver, statements, new String[] {whatSub}, onlineReal);

		n = -1;
		n += 1; testSteps[n][0] = "id:RequestedDt:sendkeys" ; testSteps[n][1] = avtb.todayString(-3) ;
		n += 1; testSteps[n][0] = "id:ResultsDt:sendkeys" ; testSteps[n][1] = avtb.todayString(-1) ;
		n += 1; testSteps[n][0] = "name:ResultsCd:clickvalue" ; testSteps[n][1] = "2" ;
		n += 1; testSteps[n][0] = "id:OtherDatabankName:sendkeys" ; testSteps[n][1] = "Test Agency of Know It All" ;
		n += 1; testSteps[n][0] = "id:Remark:sendkeys" ; testSteps[n][1] = "Nothing wrong found" ;

		uboundTS = n ;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, null, null, onlineReal, whatSync, uboundWhatSync);
		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);

		//	psv_roc(onlineReal);
		//	save_psv_roc(onlineReal);
	}

	public void verifyWorkHistory(boolean toRun, Document anEApplicationXMLData, boolean onlineReal){
		funcName="verifyWorkHistory";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());

		subLinkNames[0] = "GAP";
		//		subLinkNames[1] = ;
		//		subLinkNames[2] = ;
		uboundSubLinkNames = 0;
		//aLib_JCCQAS.linksToReach(driver, statements, new String[] {"Education/Training"}, onlineReal);


		for (int sublnIdx = 0; sublnIdx <= uboundSubLinkNames; sublnIdx++){
			switch(subLinkNames[sublnIdx]){
			case "GAP":
				List <WebElement> allGAPs = driver.findElements(By.linkText("GAP")) ;
				int gapSize = allGAPs.size();
				if (gapSize > 2 ) {
					System.out.printf("WARNING: (may have some TODO for this provider) find %d GAPs, but work on the first 2\n", gapSize);

					gapSize = 2 ;
				}
				else {
					System.out.printf("find %d GAPs\n", gapSize);

				}
				System.out.printf("find %d GAPs\n", gapSize);

				//		case "FMSB":
				//			xmlpath= "//" + pageName + "/ProfessionalEducation";
				//			//		NodeList aNL = avtb.getElementsByXpath(anXMLData, xmlpath);\n cnt = aNL.getLength() 
				//			//				aNL = avtb.getElementsByXpath(anXMLData, xmlpath);
				//			//				cnt = aNL.getLength();
				//			Document[] peDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
				//			if( peDoc == null) { break; }
				for (int  itemx = 0; itemx < gapSize; itemx++){
					// need to get fresh allGAPs because the links get updated by the verifyWorkHistory_GAP();
					allGAPs = driver.findElements(By.linkText("GAP")) ; 
					verifyWorkHistory_GAP(allGAPs.get(itemx), onlineReal);
				} 

				break;

				//		case "Other Agency":
				//			xmlpath= "//" + pageName + "/PostGraduateTraining";
				//			Document[] ptDoc = avtb.documentGetNDocuments(anXMLData, xmlpath);
				//			if( ptDoc == null) { break; }
				//			for (int  itemx = 0; itemx < ptDoc.length; itemx++){
				//				verifyDatabank_OtherAgency(subLinkNames[sublnIdx], onlineReal);
				//			}	
				//			break;
			default:
				System.out.print("????????????????? Not yet implemented: " + subLinkNames[sublnIdx] + " within " + pageName);
			}
		}
	} // end of verifyWorkHistory()

	public void verifyWorkHistory_GAP(WebElement whatSub, boolean onlineReal) {
		funcName="verifyWorkHistory_GAP";
		System.out.printf( "\n\n%s Entered at delta seconds=%d  <==================================================\n", funcName, avtb.tDelta());
		//		GapReasonId	getText
		//		VerifiedGapReasonId select the value
		//		GapCommentTxt == VerifiedGapCommentTxt
		//		psv_roc, save_psv_roc
		if(onlineReal) {
			whatSub.click();
			avtb.waitForPageLoad(driver);
			//NOTE: some attribute are considered boolean and returns true/false instead values shown in "Inspect Element"
			String disabled = driver.findElement(By.id("VerifiedGapReasonId")).getAttribute("disabled");
			if((disabled != null) && disabled.equalsIgnoreCase("true")) {
				//TODO: it's better the check first that this GAP is already verified in the future.  do this for now
				driver.findElement(By.linkText("Close")).click();
				System.out.println("Already verified, Close and try next one");
				return ;
			}
			else {
				System.out.printf("continue to fill, attribute=%s\n", 	driver.findElement(By.id("VerifiedGapReasonId")).getAttribute("disabled"));
			}
		}
		//aLib_JCCQAS.linksToReach(driver, statements, new String[] {whatSub}, onlineReal);


		n = -1;
		n += 1; testSteps[n][0] = "id:VerifiedGapReasonId:select" ; testSteps[n][1] = driver.findElement(By.xpath("//select[@id='GapReasonId']/option[@selected]")).getText() ;
		n += 1; testSteps[n][0] = "id:VerifiedGapCommentTxt:sendkeys" ; testSteps[n][1] = driver.findElement(By.id("GapCommentTxt")).getText() ; ;
		uboundTS = n ;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, null, null, onlineReal, whatSync, uboundWhatSync);
		//		aLib_JCCQAS.saveToMoveOn(driver, statements, SaveType.dataLoadingSaving, onlineReal);

		psv_roc(onlineReal);
		save_psv_roc(onlineReal);
	} // end of verifyWorkHistory_GAP()

	public void psv_roc(boolean onlineReal) {
		n = -1;
		n += 1; testSteps[n][0] = "name:PsvMethodId:clickvalue" ; testSteps[n][1] = "2";
		n += 1; testSteps[n][0] = "id:PsvDate:sendkeys" ; testSteps[n][1] = avtb.todayString(-1);
		n += 1; testSteps[n][0] = "id:PsvContactName:sendkeys" ; testSteps[n][1] = "BAI-JOU FU";
		n += 1; testSteps[n][0] = "id:PsvContactPhone:sendkeys" ; testSteps[n][1] = "7035755052";
		uboundTS = n ;
		uboundWhatSync = -1;
		aLib_JCCQAS.stepSteadyPacerSyncN(driver, uboundTS, testSteps, parentContainer, null, null, onlineReal, whatSync, uboundWhatSync);
	}

	public void save_psv_roc(boolean onlineReal) {
		if(!onlineReal) {
			return;
		}
		acceptNextAlert = false;
		driver.findElement(By.id("btnSave")).click();
		Utility.waitTillAlert(driver, logger, 100);
		//		// If incomplete, the message is "The verification of the record is not yet complete. ...."
		//		//assertEquals(avtb.closeAlertAndGetItsText(), "You MUST quality control the scanned image. Do not select <Ok> to save this document to the permanent record before you have quality controlled it. Assure it is readable and confirm the data being saved is accurate. If you have not performed this quality review, select <Cancel> and complete the review. Once the review has been completed select <Ok> to continue saving the record.");
		//		acceptNextAlert = true;
		//		//driver.findElement(By.id("btnSave")).click();
		//		assertEquals(avtb.closeAlertAndGetItsText(), "You MUST quality control the scanned image. Do not select <Ok> to save this document to the permanent record before you have quality controlled it. Assure it is readable and confirm the data being saved is accurate. If you have not performed this quality review, select <Cancel> and complete the review. Once the review has been completed select <Ok> to continue saving the record.");
		//		avtb.waitForPageLoad(driver);
		//		try {
		//			Thread.sleep(100);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.switchTo().alert().accept();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		avtb.waitForPageLoad(driver);
	}

}

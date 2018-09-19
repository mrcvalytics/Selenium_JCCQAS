package com.valytics.SDDAutomation.JCCQAS;

	import java.util.regex.Pattern;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
	import org.testng.annotations.*;
	import static org.testng.Assert.*;
	import org.openqa.selenium.*;
	import org.openqa.selenium.firefox.FirefoxDriver;
	import org.openqa.selenium.support.ui.Select;

	public class Privileging_2_Position_with_Privileging_with_Katalon_Chrome {

	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @BeforeClass(alwaysRun = true)
	  public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    baseUrl = "https://www.katalon.com/";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

	  @Test
	  public void testPrivileging2EApp0726() throws Exception {
	    driver.get("https://ccqasvld.csd.disa.mil/Account/Splash?ReturnUrl=%2f");
	    driver.findElement(By.linkText("Username Logon")).click();
	    driver.findElement(By.linkText("Yes, I understand the contents of the above Privacy Act Statements and Warnings.")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("LNDODTWOF");
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("LNDODTWOF");
	    driver.findElement(By.xpath("//button[@type='submit']")).click(); //logon
	    driver.findElement(By.xpath("//div[@id='navbar-profile']/ul/li[3]/a/i[2]")).click(); //click to pop-up for logout
	    driver.findElement(By.linkText("Logout")).click();
	    driver.findElement(By.linkText("Logon")).click();
	    driver.findElement(By.linkText("Username Logon")).click();
	    driver.findElement(By.id("privacyYes")).click();
//	    driver.findElement(By.id("username")).clear();
//	    driver.findElement(By.id("username")).sendKeys("LNDODTWOF");
//	    driver.findElement(By.id("password")).clear();
//	    driver.findElement(By.id("password")).sendKeys("LNDODTWOF");
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("LNMAENQAF");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("LNMAENQAF");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.linkText("Complete Application (Military)")).click(); 
	    driver.findElement(By.linkText("Position")).click();
	    driver.findElement(By.id("PractitionerTypeId")).click();
	    new Select(driver.findElement(By.id("PractitionerTypeId"))).selectByVisibleText("Physician");
	    driver.findElement(By.id("PractitionerTypeId")).click();
	    driver.findElement(By.id("RequestingPrivilegesBln1")).click();
	    driver.findElement(By.id("Mtfs_0__RequestedBln")).click();
	    driver.findElement(By.id("Mtfs_0__AdmittingPrivBln")).click();
	    driver.findElement(By.xpath("//div[@id='mainContent']/form/div/div[3]/button")).click(); //Save button
	  }

	  @AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
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
	}

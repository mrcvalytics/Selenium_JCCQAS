package com.valytics.SDDAutomation.JCCQAS;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
public class ConnectExistingSession {
	public static void Login(WebDriver driver) throws InterruptedException, Exception {
		WebDriverWait wait = new WebDriverWait(driver,25);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("")));
		driver.findElement(By.cssSelector("")).sendKeys("");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("")));
		
driver.findElement(By.xpath("")).sendKeys("");
	}
}


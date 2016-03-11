package com.exp;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class sele {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver","D:\\EclipseWS\\Workspace\\scrape\\chromedriver.exe");
		WebDriver driver = new ChromeDriver ();
		
		for(int i=1;i<=1;i++)
		{
			
		System.out.println(i);
		System.out.println("hi");
//		driver.
		driver.get("http://www.bigbasket.com/ps/?q=DARK%20FANTASY%20CHOCO%20FILLS");
		System.out.println("hi");
//		browser.findElement(By.className("mapLock")).findElement(By.className("mapLockDisplay")).click();

//		WebElement findElement = driver.findElement(By.tagName("div")).findElement(By.className("bigContainer"));
		System.out.println(driver.getTitle());
		String web1=driver.getWindowHandle();
		System.out.println(web1);
//		List<WebElement> web=driver.findElements(By.tagName("div").id("products-container"));
//		List<WebElement> we=driver.findElement(By.className("bigContainer")).findElements(By.className("details"));
		
		//List<WebElement> we=driver.findElements(By.className("img-description"));
//		for(WebElement e:web)
//		{
//			System.out.println(e.getText());
//		}
//		for
		System.out.println("hi");
		}
		driver.close();
		System.exit(0);

	}

}

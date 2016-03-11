package com.exp;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.soap.Text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class exp {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		System.out.println(new Date());
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
	    Document doc = Jsoup.connect("http://purplle.com/product/fiama-di-wills-exotic-dream-bathing-bar-125-3-gm")
//	    		.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
	    		.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    		.timeout(10*1000).get();
//	    System.out.println(doc.select("div#search_product_list").select("ul").select("li").text());
//	    System.out.println(doc.select("div.bigContainer").text());
//	   
////	    String product_ID=doc.select("div.product").select("div.product-about").select("span").get(0).select("span").get(0).text().split(" ")[3];	    
////	    String stock=doc.select("div.stock-exchange-cont").get(0).text();
////	    System.out.println(stock);
////		float product_Id=Float.parseFloat(prod.text().replaceAll("[\\D]",""));
////		System.out.println(product_Id);
//	    Elements  pp=doc.select("div#searchTemplate").select("div#atfResults").select("ul");
//	    System.out.println(pp.size());
//	    System.out.println(pp.get(0).text());
//		System.out.println(doc.title());
		System.out.println("hi");
	
		
		
		
		
		//purplle
		System.out.println(doc.select("div#product-items").select("ul").text());		
		System.out.println(doc.select("div.pp-7-12.pdl25").select("strike").text());
		System.out.println(doc.select("div.pp-7-12.pdl25").select("div.price").text());

		
		
		
		
		
		
		//nykaa
//	    System.out.println(doc.select("div.product-shop").select("span.price").text());


		
		
//		selenium-paytm
		System.setProperty("webdriver.chrome.driver","D:\\EclipseWS\\Workspace\\scrape\\chromedriver.exe");
		WebDriver driver = new ChromeDriver ();
		
		for(int i=1;i<=1;i++)
		{
			
		System.out.println(i);
		System.out.println("hi");
		driver.get("https://paytm.com/shop/search/?q=fiama%20di%20wills%20CLEAR%20SPRINGS%20125GMS&from=organic");
		System.out.println("hi");
//		browser.findElement(By.className("mapLock")).findElement(By.className("mapLockDisplay")).click();

//		WebElement findElement = driver.findElement(By.tagName("div")).findElement(By.className("bigContainer"));
		System.out.println(driver.getTitle());
		List<WebElement> we=driver.findElement(By.className("bigContainer")).findElements(By.className("details"));
		
		//List<WebElement> we=driver.findElements(By.className("img-description"));
		for(WebElement e:we)
		{
			System.out.println(e.getText());
		}
//		for
		System.out.println("hi");
		}
		driver.close();
		System.exit(0);
		
		
	
		
		
		//amazon use user agent
//	    Elements products = doc.select(".s-result-list-parent-container > ul > li");
//	    System.out.println(doc.select("div#fkmr-results0").select("ul").select("li").size());
//	    Element pric=doc.select("product-pricing").get(0);
//	    String s="Fiama Di Wills Men Invigorating Musk Shower Gel, 250ml with Free Shower Gel, 100ml";
//	    System.out.println(s.split("with")[0]);
	}

}

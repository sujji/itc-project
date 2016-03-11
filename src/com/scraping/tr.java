package com.scraping;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class tr {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		paytm();
		// TODO Auto-generated method stub

	}
	public static void paytm() throws IOException{

//		String search_url = "https://paytm.com/shop/p/vivel-cell-renew-all-year-light-body-lotion-BEAVIVEL-CELL-RKAPO402105D48A753?src=search-grid&tracker=organic%7Cundefined%7CCell%20Renew%20All%20Year%20Light%20Body%20Lotion%20250ml%7Cgrid%7CSearch%7C1";
//				   Document doc = Jsoup.connect(search_url).timeout(1000000).get();  
//				   Elements results = doc.getAllElements();
//				   System.out.println(doc.title());
//				   //---Title---//
//				   String title = results.select("h1.fm.f24.brb1.bc-e8.pdb10").text();
//		           System.out.println("Product name: " + title);            
		         
		          //---Price---//
		           System.setProperty("webdriver.chrome.driver","D:\\EclipseWS\\Workspace\\scrape\\chromedriver.exe");
		           ChromeDriver driver = new ChromeDriver();

		driver.get("https://paytm.com/shop/p/vivel-cell-renew-all-year-light-body-lotion-BEAVIVEL-CELL-RKAPO402105D48A753?src=search-grid&tracker=organic%7Cundefined%7CCell%20Renew%20All%20Year%20Light%20Body%20Lotion%20250ml%7Cgrid%7CSearch%7C1");

		           WebElement price = driver.findElement(By.cssSelector("span[ng-if='!product product isCarCategory']"));
//		           driver.findElement(By.className("bigContainer")).findElements(By.className("details"));
		           //assertThat(cityField.getAttribute("price"));
		           System.out.println(price.getText());
		           //Elements outerDiv = doc.getElementsByClass("ng-if=!product.product.isCarCategory");
		           //System.out.println(outerDiv.text());
		        
		           //---Availability---//
		           //Since availability in meta-tags which are unextractable.So logic to extract availability information is below//
//		          if((results.select("div.out-of-stock").attr("style")).matches("(.*)none(.*)"))
//		       	   System.out.println("In-stock");
//		          else
//		       	   System.out.println("Out-of-stock");
		          
		          System.out.println();
			  }  


}

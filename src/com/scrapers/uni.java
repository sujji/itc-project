package com.scrapers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class uni {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
		
		
		Document doc = Jsoup.connect("http://www.snapdeal.com/product/hp-v228g-16gb-pen-drive/80126#bcrumbSearch:pendrive").timeout(10*1000).get();
		 
//		Element e=doc.select("div#price_feature_div").get(0);
//		String e=doc.select("div.product-pricing").get(0).select("meta").get(0).attr("content");
//		System.out.println(e);
		
		//snapdeal div itemprop="offers" regex mrp,pay
		Element e=doc.select("div[itemprop=offers]").get(0);
		String mydata = e.text()+" ";
	    System.out.println(mydata);
		Pattern pattern = Pattern.compile("MRP Rs  (.*?) ");
		Matcher matcher = pattern.matcher(mydata);
		if(matcher.find())
			{
			String pr_Id=matcher.group(1);			
			System.out.println(pr_Id);
			}
		
		
		Pattern pattern1 = Pattern.compile("pay Rs (.*?) ");
		Matcher matcher1 = pattern1.matcher(mydata);
		if(matcher1.find())
		{
		String pr_Id1=matcher1.group(1);
		System.out.println(pr_Id1);
		}		
		
		
		//shopclues div.product-pricing regex selling price mrp
//		Element e1=doc.select("div[itemprop=offers]").get(0);
//		System.out.println(e1.text());
		

		//amazon div#price_feature_div
//		Element e2=doc.select("div#price_feature_div").get(0);
	}

}

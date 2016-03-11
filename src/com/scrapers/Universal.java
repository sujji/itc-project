package com.scrapers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Universal {
	static String url="";
	public Universal() throws IOException
	{
		
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
	}
	public void productIDFinder() throws IOException
	{
		//		String url= "http://www.amazon.in/Jbl-Headphones-For-All-Devices/dp/B019S3B9X6/ref=sr_1_16?s=electronics&ie=UTF8&qid=1457525965&sr=1-16&keywords=jbl";
		Document doc = Jsoup.connect(url).timeout(10*1000).get();
		String id_tag="";
		String regex="";
		if(url.contains("amazon"))
		{
			id_tag="div#detail_bullets_id";
			regex="ASIN: (.*?) ";
		}
		else if(url.contains("shopclues"))
		{
			id_tag="div.product-about";
			regex="Product ID : (.*?) ";
		}
		else if(url.contains("snapdeal"))
		{
			id_tag="div.comp.comp-product-specs";
			regex="SUPC: (.*?) ";
		}
		String mydata = doc.select(id_tag).get(0).text()+" ";
		System.out.println(mydata);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mydata);
		matcher.find();
		String pr_Id=matcher.group(1);
		System.out.print("Product ID: ");
		System.out.println(pr_Id);
	}
	public void priceFinder()
	{

	}
	public static void main(String[] args) throws IOException {
		//		new Universal().productIDFinder();
		Universal uni=new Universal();
		BufferedReader br = new BufferedReader(new FileReader("D:\\EclipseWS\\Workspace\\scrape\\testf4.csv"));  //csv file to be read
		String line = br.readLine();
		line = br.readLine();
		int i=0;
		while(line!=null)                         //traversing the csv file 
		{
			i++;
			System.out.println("number:"+i);
			String[] b = line.split(",");	
			url=b[6];
			if(!url.contains("not found"))
			{
				

//				uni.productIDFinder(url);
				System.out.println(url);
				Document doc = Jsoup.connect(url).timeout(10*1000).get();
				
				
				String price_data = doc.select("div#price_feature_div").text();
				System.out.println(price_data);

				ArrayList<Double> prices= new ArrayList<Double>();

				Pattern pattern = Pattern.compile("Price:    (.*?)00");
				Matcher matcher = pattern.matcher(price_data);

				Pattern pattern1 = Pattern.compile("Sale:    (.*?)00");
				Matcher matcher1 = pattern1.matcher(price_data);

				Pattern pattern2 = Pattern.compile("Deal Price:Rs.(.*?) ");
				Matcher matcher2 = pattern2.matcher(price_data);

				Double mrp=0.0;
				Double sp=0.0;
				Double dp=0.0;
				if (matcher.find())
				{

					prices.add((double) Float.parseFloat(matcher.group(1).replaceAll("[\\D]","")));

				}

				if (matcher1.find())
				{

					prices.add((double) Float.parseFloat(matcher1.group(1).replaceAll("[\\D]","")));
					mrp=sp;
				}

				if (matcher2.find())
				{
					prices.add((double) Float.parseFloat(matcher2.group(1).replaceAll("[\\D]","")));
				}


//				System.out.println(prices.size());

				Collections.sort(prices);

				if(prices.size()==3)
				{
					mrp=prices.get(prices.size()-1);
					sp=prices.get(prices.size()-2);
					dp=prices.get(prices.size()-3);
				}
				if(prices.size()==2)
				{
					mrp=prices.get(prices.size()-1);
					sp=prices.get(prices.size()-2);
				}
				if(prices.size()==1)
				{
					mrp=prices.get(prices.size()-1);
					sp=mrp;
				}
				System.out.println(mrp);
				System.out.println(sp);
				System.out.println(dp);

				
			}
			line = br.readLine();
		}
	}
}



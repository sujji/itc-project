package com.scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FinalURLFinder {
	Connection con;
	String[] myStrings = new String[] {"fiama di wills","vivel love nourish","vivelcell renew","vivel","superia silk","superia","savlon","engage","shower to shower"};
	ArrayList<Product> products;
	List<String> brands =Arrays.asList(myStrings);
	

	public FinalURLFinder() throws IOException
	{
		products=new ArrayList<Product>();
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\project\\data.csv"));
		String line = br.readLine();
		line = br.readLine();
		while(line!=null)
		{

			String[] b = line.split(",");
			Product product= new Product();
			product.setCategory(b[0]);
			product.setSubCategory(b[1]);
			product.setSKU_description(b[2]);
			product.setPrice(b[4]);
			products.add(product);
			line = br.readLine();
		}
		br.close();
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");

	}
	public void amazonFinder() throws ClassNotFoundException, SQLException, IOException
	{
		
		BufferedWriter bw=new BufferedWriter(new FileWriter("new.csv"));
		int num=0; 
		int found=0;
		for(Product product:products) 
		{
			num++;
			System.out.println(product);
			System.out.println("number: "+num);
			bw.newLine();
			bw.write(Integer.toString(num)+',');
			bw.write(product.getCategory()+',');
			bw.write(product.getSubCategory()+',');
			bw.write(product.getSKU_description()+',');
			bw.write(product.getPrice()+',');
			
//			if(num==4)
//				break;
			Random randomGenerator = new Random();
			double timeout=Math.random();
			try {
				Thread.sleep(1000*randomGenerator.nextInt(3));
				System.out.println("slept for "+timeout+" seconds");
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			double price_approx=Double.parseDouble(product.getPrice());
			String product_desc=product.getSKU_description()
					.toLowerCase()	    		
					.replaceAll("(?<!x)x3", " pack of 3")
					.replaceAll("(?<!x)x4", " pack of 4")
					.replaceAll("\\*3", " pack of 3")
					.replaceAll("\\*4", " pack of 4")	
					.replaceAll("-", " ")
					.replaceAll("gms", "g")
					.replaceAll("gm", "g")	    		 		
					.replaceAll("fdw", "fiama di wills")
					.replaceAll("showergel", "shower gel")
					.replaceAll("fw","face wash")
					.replaceAll("mix", "mixed")
					.replaceAll("\\(carton\\)", "")
					.replaceAll("detoilette", "de toilette")
					.replaceAll("moisturizer", "moisturiser")
					.replaceAll("aloevera", "aloe vera")
					.replaceAll("aleovera", "aloe vera")
					.replaceAll("\\(", " ")
					.replaceAll("\\)", " ")
					.replaceAll("fdw", "fiama di wills")
					.replaceAll("vln", "vivel love nourish")
					.replaceAll("obutter", "olive butter") 
					.replaceAll("sbutter", "shea butter")
					.replaceAll("\\+", " ")
					.replaceAll("\\%", " ")
					.replaceAll("facewash", "face wash")
					.replaceAll("fw", "face wash")
					.replaceAll("cdr", "Colour Damage Repair")
					.replaceAll("ahf", "Anti Hair Fall")
					.replaceAll("tdc", "Total Damage Control ")
					.replaceAll("vcr", "vivel cell renew")
					.replaceAll("vivel cr", "vivel cell renew")						
					.replaceAll("hw", "hand wash")
					.toLowerCase();
					
//			System.out.println(price_approx);
			String search_url="http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords="+product_desc.replaceAll(" ", "\\+")+"&rh=i%3Aaps%2Ck%3A"+product_desc.replaceAll(" ", "\\+");
			System.out.println(search_url);
			System.out.println(product_desc);
			Document doc = Jsoup.connect(search_url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
		    		.timeout(10*1000).get(); 
			Elements results=doc.select("div#atfResults");
//			if(results.size()==0)
//			{
//				results=doc.select("div#fkmr-results0");
//			}
			String brand="";
			System.out.println("results:"+results.size());
			if(results.size()!=0)
			{
				System.out.println("results found");
				int length=product_desc.split(" ").length;
				String[] tokens=product_desc.split(" ");
				String url_final="";
				Double price_final=0.0;
				int max_score=0;
				Elements productsList=results.select("ul").select("li");
				for (Element link : productsList)
				{ 
					int mrp=0;	
					int score=0;
					String s;
					if(link.select("span.currencyINR").size()==4)
					{
						s=link.select("span.currencyINR").get(1).parent().text();
						System.out.println(Double.parseDouble(s.replaceAll("[\\D]",""))/100);
//						System.out.println(link.select("span.currencyINR").get(1).parent().text());
					}
					else if(link.select("span.currencyINR").size()==2||link.select("span.currencyINR").size()==1)
					{
						s=link.select("span.currencyINR").get(0).parent().text();
//						System.out.println("hi");
//						System.out.println(link.select("span.currencyINR").get(0).parent().text());
					}
					else
					{
						System.out.println("broken");
						break;
					}
					
					Double j=Double.parseDouble(s.replaceAll("[\\D]",""))/100;
					Double i=price_approx;
					System.out.println(i);
					if((j>=(i*0.95)&&j<=i)   || (j<=(i*1.05)&&j>=i ))
					{ 	                    	 
						mrp++;
					}
					System.out.println("mrp"+mrp);
					if(mrp!=0)
					{
//						System.out.println("hello");
						String desc=link.select("h2").text().split("with")[0];	
						System.out.println(desc);
						for(String token:tokens)
						{
//							System.out.println(token);
							if(desc.toLowerCase().contains(token.toLowerCase()))
							{score++;}
						}

						if(score>max_score)
						{
							max_score=score;
							price_final=j;
							url_final=link.select("a").attr("href");							
						}
					}
					System.out.println(score);
				}
				System.out.print("max_score:");
				System.out.println(max_score);
				if(max_score>=length*0.5)
				{
					System.out.print("found:");
					System.out.println(product_desc);
					bw.write(price_final.toString());
					bw.write(",");
					bw.write(url_final);
					found++;  
				}
				else
				{
					bw.write("not found,");
					bw.write("not found");
				}
			}
			bw.flush();
			
		}
		System.out.println(found);
		
//		con.close();
	}
}
	
				
	



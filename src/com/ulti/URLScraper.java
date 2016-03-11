package com.ulti;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class URLScraper {
	Connection con;
	Statement stmt;
	public URLScraper() throws ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");	
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");

	}
	public void scrapedDataEntry(ScrapedData scrapedData) throws SQLException
	{
		Statement stmt1= con.createStatement();
		String scrapedDataInsert="insert into Scraped_Data(url_id,url_version,mrp,price,stock,created_time,date) values("           //inserting into Scraped_Data table
				+scrapedData.getUrlId()+","
				+scrapedData.getUrlVersion()+","
				+scrapedData.getMrp()+","
				+scrapedData.getSellingPrice()+","
				+scrapedData.getStock()+",'"
				+scrapedData.getCreatedTime()+"','"
				+scrapedData.getStartDate()+"')"
			;
		stmt1.execute(scrapedDataInsert);
	}
	public  void amazonScraper() throws IOException, ClassNotFoundException, SQLException {
		//		new Universal().productIDFinder();

		con = DriverManager.getConnection("jdbc:mysql://10.6.116.134:3306/sqoop", "20590", "login@123");
		stmt = con.createStatement();
		String selectPortal="select portal_id from Portal where name like '%amazon%'";
		ResultSet portals=stmt.executeQuery(selectPortal);
		
		int portalId=0;
		while(portals.next())
		{
			portalId=portals.getInt("portal_id");
			System.out.println("portal:"+portalId);
		}
		
		String urlSelect="select * from(select * from URL where portal_id="+portalId+" order by url_version desc) alias group by url_id";     //getting the latest version of urls
		ResultSet urls=stmt.executeQuery(urlSelect);
				
		int i=0;
		String url="";	
		
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = 
				new java.text.SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(dt);
		
		while(urls.next())                         //traversing the csv file 
		{

			i++;

			System.out.println("number:"+urls.getInt("url_id"));
			url=urls.getString("url");
			System.out.println(url);
			
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
					.timeout(10*1000).get();
					
			Double mrp=0.0;
			Double sp=0.0;
			
			String price_data = doc.select("div#price_feature_div").text();
			if(price_data.equals(""))
			{
				price_data = doc.select("div#olp_feature_div").text();
				String[] s=price_data.split(" ");
				mrp=Double.parseDouble(s[s.length-1].replaceAll("[\\D]",""))/100;
				sp=mrp;
			}
			else
			{
				ArrayList<Double> prices= new ArrayList<Double>();

				Pattern pattern = Pattern.compile("Price:    (.*?)00");
				Matcher matcher = pattern.matcher(price_data);

				Pattern pattern1 = Pattern.compile("Sale:    (.*?)00");
				Matcher matcher1 = pattern1.matcher(price_data);

				Pattern pattern2 = Pattern.compile("M.R.P.:    (.*?)00");
				Matcher matcher2 = pattern2.matcher(price_data);

				if (matcher2.find())
				{
					prices.add(Double.parseDouble(matcher2.group(1).replaceAll("[\\D]","")));
				}		
				if (matcher.find())
				{
					prices.add(Double.parseDouble(matcher.group(1).replaceAll("[\\D]","")));
				}

				if (matcher1.find())
				{
					prices.add(Double.parseDouble(matcher1.group(1).replaceAll("[\\D]","")));
				}

				Collections.sort(prices);

				if(prices.size()==3)
				{
					mrp=prices.get(prices.size()-1);
					sp=prices.get(prices.size()-3);
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
			}
			String availability=doc.select("div#availability").text().toLowerCase();
			int stock=0;
			if(!availability.contains("out of stock")&&!availability.contains("unavailable"))
			{
				stock=1;
			}
			System.out.println("mrp:"+mrp);
			System.out.println("sp:"+sp);
			
			java.util.Date dt1 = new java.util.Date();
			java.text.SimpleDateFormat sdf1 = 
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf1.format(dt1);
			
			ScrapedData scrapedData=new ScrapedData();		
			scrapedData.setUrlId(urls.getLong("url_id"));
			scrapedData.setUrlVersion(urls.getInt("url_version"));
			scrapedData.setMrp(mrp);
			scrapedData.setSellingPrice(sp);
			scrapedData.setStock(stock);
			scrapedData.setCreatedTime(currentTime);
			scrapedData.setStartDate(startDate);
			
			scrapedDataEntry(scrapedData);
		}
	}

}



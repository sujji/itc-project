package com.scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CopyOfURLFinder {
	ArrayList<Product> products;
	Connection con;
	public CopyOfURLFinder() throws IOException
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

//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//
//			con = DriverManager.getConnection("jdbc:mysql://10.6.116.134:3306/sqoop", "20590", "login@123");
//
//			//				ResultSet rs = stmt.executeQuery("insert into itc values (2,sujji)");
//
//			//				while (rs.next())
//			//					System.out.println(rs.getInt(1) + "  " + rs.getString(2));
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
	}

	public void shopclues_finder() throws IOException, SQLException
	{
//		Statement stmt = con.createStatement();
//		System.out.println(products);
//		ResultSet rs = stmt.executeQuery("select * from BRAND_METADATA");
//		HashMap<String, String> keywords=new HashMap<String, String>();
//		while (rs.next())
//		{
//			keywords.put(rs.getString(2),rs.getString(3));
//		}
//		System.out.println(keywords);
		BufferedWriter bw=new BufferedWriter(new FileWriter("sss.csv"));
		bw.write("No,SKU Description,MRP,current price,url");
		int num=0; 
		int found=0;
		for(Product product:products)
		{
			String product_desc=product.getSKU_description()
			.toLowerCase()
			.replaceAll("fdw", "fiama di wills")
			.replaceAll("vln", "vivel love nourish")
			.replaceAll("obutter", "olive butter") 
			.replaceAll("sbutter", "shea butter")
			.replaceAll("-", " ")
			.replaceAll("\\(", " ")
			.replaceAll("\\)", " ")
			.replaceAll("\\+", " ")
			.replaceAll("\\*", "x")
			.replaceAll("\\%", " ")
			.replaceAll("showergel", "shower gel")
			.replaceAll("facewash", "face wash")
			.replaceAll("fw", "face wash")
			.replaceAll("cdr", "Colour Damage Repair")
			.replaceAll("ahf", "Anti Hair Fall")
			.replaceAll("tdc", "Total Damage Control ")
			.replaceAll("vcr", "vivel cell renew")
			.replaceAll("cr", "cell renew")	
			.replaceAll("hw", "hand wash")	
			
			;	
//			System.out.println(product_desc);
			num++;
			System.out.println(num);
			bw.newLine();
			bw.write(Integer.toString(num)+',');
			bw.write(product.getCategory()+',');
			bw.write(product.getSubCategory()+',');
			bw.write(product.getSKU_description()+',');
			bw.write(product.getPrice()+',');
			String search_url="http://search.shopclues.com/?q="+product_desc.replace(' ','+').replace("\'","")+"&auto_suggest=0&cid=0&z=1&sc_z=";
			Document doc = Jsoup.connect(search_url).timeout(10*1000).get(); 
			Elements links=doc.select("div.products_list").select("ul").select("li");
			String prod_desc=product.getSKU_description().toLowerCase()+" "
							.replaceAll("fdw", "fiama di wills")
							.replaceAll("vln", "vivel love nourish");
//			String synonym="";
			int length=product_desc.split(" ").length;
//			for(String key:keywords.keySet())
//			{
//				if(key!=null)
//				{
//					if(prod_desc.contains(key.toLowerCase()))
//					{
//						synonym= keywords.get(key).toLowerCase();
//					}					
//				}
//			}
//			System.out.println(prod_desc);
			String[] tokens=product_desc.split(" ");
			String url_final="";
			String price_final="";
			int max_score=0;
			for (Element link : links)
			{ 
				int mrp=0;	
				int score=0;
				Elements price=link.select("span.old-price");
				Elements price1=link.select("div.p_price");

				if(price.size()>0)
				{
					for(Element pric: price)
					{ 
						int j=Integer.parseInt(pric.text());
						int i=Integer.parseInt(product.getPrice());
						if((j>=(i*0.95)&&j<=i)   || (j<=(i*1.05)&&j>=i ))
						{ 	                    	 
							mrp++;
						}	                    	 
					}
				}
				else
				{
					for(Element pric: price1)
					{  
						int j=Integer.parseInt(pric.text());
						int i=Integer.parseInt(product.getPrice());
						if((j>=(i*0.95)&&j<=i)   || (j<=(i*1.05)&&j>=i ))
						{ 	                    	 
							mrp++;
						}
					}
				} 	
				if(mrp!=0)
				{
					Elements product_url=link.select("h5").select("a");
					String url1=product_url.attr("href");
//					System.out.println(url1);
					
					String[] desc=url1.split("/");
//					System.out.println(desc[desc.length-1].replaceAll("-", " ").replaceAll(".html", "").replaceAll("125gm x 3", "125gmx3"));
					String prod_desc1=desc[desc.length-1].replaceAll("-", " ").replaceAll(".html", "").replaceAll("125gm x 3", "125gmx3");
					System.out.println(prod_desc1);
					for(String token:tokens)
					{
						if(token.length()!=1)
						{
							if(prod_desc1.toLowerCase().contains(token.toLowerCase()))
							{score++;}
						}
					}
//					if(prod_desc1.toLowerCase().contains(synonym))
//						{score++;}
					if(score>max_score)
					{
						max_score=score;
						url_final=url1;
						if(price.size()>0)
						{
							for(Element pric: price)
							{ 
								price_final=pric.text();	                    	 
							}
						}
						else
						{
							for(Element pric: price1)
							{  
								price_final=pric.text();
							}
						} 

					}
				}
			}
			if(max_score>=length/2.0)
			{
				bw.write(price_final+',');
				bw.write(url_final);
				found++;  
			}
			else
			{
				bw.write("not found,");
				bw.write("not found");
			}
//			Random randomGenerator = new Random();
//			double timeout=Math.random();
//			try {
//				Thread.sleep(1000*randomGenerator.nextInt(10));
//				System.out.println("slept for "+timeout+" seconds");
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			bw.flush();         
		}

		bw.close();
//		con.close();
		System.out.println(found);
	}
	public void amazon_finder() throws IOException
	{
		String produc="FDW CLEAR SPRINGS 125 GMS";
		String url="http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=NIVEA+cream+150+ml&rh=i%3Aaps%2Ck%3ANIVEA+cream+150+ml";
		System.out.println(url);
		Document doc = Jsoup.connect(url).timeout(10*1000).get(); 
		Elements links=doc.select("div.searchTemplate").select("ul");
		System.out.println(links.size());
		for(Element link:links)
		{
			Elements lis=link.select("li");
			System.out.println(lis.size());
			//	   		 	for(Element l:lis)
			//	   		 	{
			//	   		 		System.out.println(l.text());
			//	   		 	}

		}
	}
	public void snapdeal_finder(ArrayList<Product> products)
	{

	}
	public void paytm_finder() throws IOException
	{
		String produc="FDW CLEAR SPRINGS 125 GMS";
		String url="https://paytm.com/shop/search/?q=FDW%20CLEAR%20SPRINGS%20125%20GMS"+produc.replaceAll(" ", "%20");
		Document doc = Jsoup.connect(url).timeout(10*1000).get(); 

		Elements links=doc.select("div.details");
		System.out.println(links.size());


	}
	public void nykaa_finder(ArrayList<Product> products)
	{

	}

}

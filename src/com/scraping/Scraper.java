package com.scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Scraper {
	ArrayList<Product> products;
	Connection con;
	public Scraper() throws IOException
	{
		products=new ArrayList<Product>();
		BufferedReader br = new BufferedReader(new FileReader("D:\\EclipseWS\\Workspace\\scrape\\shopclues_dump1.csv"));
		String line = br.readLine();
		line = br.readLine();
		while(line!=null)
		{
			String[] b = line.split(",");
			Product product= new Product();
			product.setCategory(b[1]);
			product.setSubCategory(b[2]);
			product.setSKU_description(b[3]);
			product.setPrice(b[4]);
			product.setUrl(b[6]);
			products.add(product);
			line = br.readLine();
		}
		br.close();
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://10.6.116.134:3306/sqoop", "20590", "login@123");

			//				ResultSet rs = stmt.executeQuery("insert into itc values (2,sujji)");

			//				while (rs.next())
			//					System.out.println(rs.getInt(1) + "  " + rs.getString(2));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void shopClues_scraper() throws IOException, SQLException 
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("shopclues_scraped1.csv"));
		bw.write("No,TimeStamp,Category,Sub Category,SKU Description,url,Product ID,Price,MRP,Selling Price,In Stock");
		int num=0;
		for(Product product:products)
		{
			num++;			
			System.out.println(num);
			bw.newLine();
			bw.write(Integer.toString(num)+',');

			bw.write(product.getCategory()+',');
			bw.write(product.getSubCategory()+',');
			bw.write(product.getSKU_description()+',');


			String product_url=product.getUrl();

			
			
			
			if(!product_url.toLowerCase().contains("not "))
			{
				bw.write(product_url+',');
				
				//				System.out.println(product_url);
				Response response = Jsoup.connect(product_url).followRedirects(true).execute();
				URL resp=response.url();
				if(!resp.toString().equals("http://www.shopclues.com/"))
				{
					int stock=0;
					float mrp=0;
					float sp=0;
					float dp=0;			
					String productId="";
					
					String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
					Document doc = Jsoup.connect(product_url).timeout(10*1000).get();
					System.out.println(product_url);

					String price_data = doc.select("div.product-pricing").text();
					
					Element prod=doc.select("div.product-about").select("span").get(0).select("span").get(0);
					productId=prod.text().split(" ")[3];

					
					Element stoc=doc.select("div.stock-exchange-cont").get(0);
					if(stoc.text().toLowerCase().contains("sold out"))
						bw.write("No");
					else
						stock=1;
					
					Pattern pattern = Pattern.compile("List Price: Rs.(.*?) ");
					Matcher matcher = pattern.matcher(price_data);
					
					Pattern pattern1 = Pattern.compile("Selling Price :Rs.(.*?) ");
					Matcher matcher1 = pattern1.matcher(price_data);
					
					Pattern pattern2 = Pattern.compile("Deal Price:Rs.(.*?) ");
					Matcher matcher2 = pattern2.matcher(price_data);
						
					if (matcher.find())
					{
						mrp=Float.parseFloat(matcher.group(1).replaceAll("[\\D]",""));
						if (matcher1.find())
						{
							sp=Float.parseFloat(matcher1.group(1).replaceAll("[\\D]",""));
						}
					}
					else
					{
						if (matcher1.find())
						{
							sp=Float.parseFloat(matcher1.group(1).replaceAll("[\\D]",""));
							mrp=sp;
						}
					}
					if (matcher2.find())
					{
						dp=Float.parseFloat(matcher2.group(1).replaceAll("[\\D]",""));

					}
		
					System.out.println("yes");
					Statement stmt = con.createStatement();
					String query="insert into shopclues(time,shopclues_product_id,MRP,selling_price,stock,url) values ('"+date+"',"+"'"+productId+"',"+mrp+","+sp+","+stock+",'"+product_url+"')";
					System.out.println(query);
					stmt.execute(query);
					
					Statement stmt1 = con.createStatement();
					String query1="update products set shopclues_product_id='"+productId+"' where SKU_description='"+product.getSKU_description()+"'";
					System.out.println(query1);
					stmt1.execute(query1);
					
				}
				else
				{
					System.out.println("no");
				}				
			}	
			else
			{
				bw.write("-,");
			}
			Random randomGenerator = new Random();
			double timeout=Math.random();
			try {
				Thread.sleep(1000*randomGenerator.nextInt(10));
				System.out.println("slept for "+timeout+" seconds");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw.flush();
		}
		
	}

	//	public void amazon_scraper()
	//	{
	//		String product_url="";
	//		Document doc = Jsoup.connect(product_url).timeout(10*1000).get();
	//	    
	//	    Element prod=doc.select("div.product-about").select("span").get(0).select("span").get(0);
	//	    float product_Id=Float.parseFloat(prod.text().replaceAll("[\\D]",""));
	//
	//	    Element pric=doc.select("div.product-pricing").get(0);	   
	//	    
	//	    if(pric.select("span#sec_list_price_"+product_Id).text().equals(""))
	//	    {
	//	    	bw.write(pric.select("div.price").get(0).text().split("\\.")[1]+',');
	//	    	bw.write(pric.select("div.price").get(0).text().split("\\.")[1]+',');
	////	    	System.out.println(pric.select("div.price").get(0).text().split("\\.")[1]);
	////	    	System.out.println(pric.select("div.price").get(0).text().split("\\.")[1]);
	//	    }
	//	    else
	//	    {
	//	    	bw.write(pric.select("span#sec_list_price_"+product_Id).text()+',');
	//	    	bw.write(pric.select("div.price").get(0).text().split("\\.")[1]+',');
	////	    	System.out.println(pric.select("span#sec_list_price_"+product_Id).text());
	////	    	System.out.println(pric.select("div.price").get(0).text().split("\\.")[1]);
	//	    }
	//	    Element stoc=doc.select("div.stock-exchange-cont").get(0);
	////	    if(stoc.text().toLowerCase().contains("sold out"))
	////	    	bw.write("No");
	////	    else
	////	    	bw.write("Yes");
	//	    
	//	    System.out.println(product_Id);	
	//	}

}

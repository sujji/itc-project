package com.scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
//import com.connection.MYSQLConn;
public class BigBasket {
	ArrayList<Product> products;
//	public static java.sql.Connection conn = MYSQLConn.connectionObj();
//	ReadProperties rp ; 
	public BigBasket() throws IOException
	{
		products=new ArrayList<Product>();
		BufferedReader br = new BufferedReader(new FileReader("D:\\project\\exp.csv"));
		String line = br.readLine();
		line = br.readLine();
		while(line!=null)
		{  
			String[] b = line.split(",");
			Product product= new Product();
			product.setSKU_description(b[2]);
//			product.setPrice(b[4]);
			products.add(product);
			line = br.readLine();
		}
		br.close();
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");		   
	}

	public void bigBasket_finder() throws IOException, SQLException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("bigBasket.csv"));
		bw.write("No,SKU Description,MRP,url");
		int num=0;  
		int title_score;
		//String timeStamp = null;
		String href = null,scraped_price = null;	

		for(Product product:products)
		{
			boolean match = false;
			num++;
			bw.newLine();									
			bw.write(Integer.toString(num)+',');	    	//write No
			bw.write(product.getSKU_description()+',');	//write SKU_description
			bw.write(product.getPrice()+',');				//write MRP

			//http://www.bigbasket.com/ps/?q=DARK+FANTASY+CHOCO+FILLS+75G
			System.out.println(product.getSKU_description());
			String search_url="http://www.bigbasket.com/ps/?q="+product.getSKU_description().replace(' ','+');

			Document doc = Jsoup.connect(search_url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").timeout(10*1000000).get(); 
			System.out.println("yello");
			System.out.println(doc.select("div#products-container").text());
			
				Elements productCells = doc.select("div#products-container");
				System.out.println(productCells.size());
				String scraped_title = null;
				System.out.println("Product No. :"+product.getSKU_description()+"\n");
				
				//split title of product from CSV 
				String[] tokens = product.getSKU_description().split(" "); 

				int reached_cell = 1;
				title_score = 0;
				for(Element productCell : productCells)
				{
					//prints the current result cell for comparison
					System.out.println("reached_cell : "+ reached_cell);
					int tokens_matched = 0;
					
					//1. Comparing titles
					//get title of product from web page
					scraped_title = productCell.select("p.product-title").text().toLowerCase();
					System.out.println(scraped_title);
					String[] str =null;
					if(scraped_title.contains("+"))
					{
						str = scraped_title.split("\\+");
						scraped_title = str[0];
						//					        		 tokens_matched -= brand_tokens.length;
					}
					System.out.println("-----------scraped_title---------------"+scraped_title+"\n");

					//finding best match 
					//matching brand name
					if(tokens[0].length()<=3){
//						String brand = getBrand(tokens[0]);
//						String[] brand_tokens = brand.split(" ");

//						for(String b : brand_tokens)
//						{
//							if(scraped_title.contains(b.toLowerCase()))
//							{
//								++tokens_matched;
//							}
//						}
					}
					
					for(String tok : tokens)
					{
						if(scraped_title.contains(tok.toLowerCase()))
						{
							++tokens_matched;				        			 
						}
					}
					//1. Comparing prices first     	
					scraped_price = productCell.select("span.WebRupee").text();
					//			        	 scraped_price = scraped_price.replaceAll("[^0-9]", "");
					System.out.println("scraped_price : "+scraped_price+"\n");
					if(inRange(scraped_price,product.getPrice()))
					{
						//2. Comparing prices if first condition is true 
						if(tokens_matched > title_score)
						{
							title_score = tokens_matched;
							href = productCell.select("div.uiv2-list-box-img-title").select("a").attr("href"); 
							bw.write(scraped_price+",");						//write curr_price 
							bw.write(href);
							bw.flush();										//write href of the best matched product
							match = true;
						}

					}
					reached_cell++;
				}
					
			if(match == false)
			{
				scraped_price = "NA";
				href = "Product not found"; 	
				System.out.println("Product not found");
				bw.write(scraped_price+",");						//write curr_price 
				bw.write(href);
				bw.flush();
			}
		}
		System.out.println("File write completed!!\n");
	}

//	public static String getBrand(String Abbreviation) throws SQLException{
//		java.sql.Statement st = conn.createStatement();
//		String sql = ("SELECT NAME FROM sqoop.BRAND_METADATA where ABBREVIATION='"+Abbreviation+"';");
//		java.sql.ResultSet rs = st.executeQuery(sql);
//
//		rs.next();
//		String str = rs.getString("NAME");
//		System.out.println(str);
//
//		//conn.close();
//		return str;
//
//	}
	public static boolean inRange(String scraped_price,String file_price){
		//		System.out.println("file_price:"+file_price);
		double min = Double.parseDouble(file_price);
		min = min-min*0.10;
		//			System.out.println("min:"+min);
		double max = Double.parseDouble(file_price);
		max = max+max*0.10;
		//		    System.out.println("max:"+max);
		double price = Double.parseDouble(scraped_price);
		return (price>=min && price<=max);
	}
	public static void main(String[] args) throws IOException, SQLException {
		new BigBasket().bigBasket_finder();
	}
}

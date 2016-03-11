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
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class purp {
	ArrayList<Product> products;
	//	public static java.sql.Connection conn = MYSQLConn.connectionObj();

	public purp() throws IOException
	{
		products=new ArrayList<Product>();
		BufferedReader br = new BufferedReader(new FileReader("D:\\project\\data.csv"));
		String line = br.readLine();
		line = br.readLine();
		while(line!=null)
		{  
			String[] b = line.split(",");
			Product product= new Product();
			// product.setCategory(b[1]);
			// product.setSubCategory(b[2]);
			product.setSKU_description(b[2]);
			product.setPrice(b[3]);
			products.add(product);
			line = br.readLine();
		}
		br.close();
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");		   
	}

	public void purplle_finder() throws IOException, SQLException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("purplle.csv"));
		//bw.write("No,SKU Description,MRP,Actual price,Deal price,url");
		bw.write("No,SKU Description,MRP,url");
		int num=0;  
		int title_score;
		//String timeStamp = null;
		String href = null,scraped_deal_price = null,scraped_actual_price = null;	

		for(Product product:products)
		{
			boolean match = false;
			num++;
			bw.newLine();									
			bw.write(Integer.toString(num)+',');	    	//write No
			bw.write(product.getSKU_description()+',');	//write SKU_description
			bw.write(product.getPrice()+',');				//write MRP	    	 

			Random randomGenerator = new Random();
			double timeout=Math.random();
			try {
				Thread.sleep(1000*randomGenerator.nextInt(4));
				System.out.println("slept for "+timeout+" seconds");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String search_url="http://purplle.com/search?q="+product.getSKU_description().replace(" ","%20");

			//	    	 Document doc = Jsoup.connect(search_url).timeout(10*60000000).get(); 
			Document doc = Jsoup.connect(search_url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").timeout(10*1000000).get();
			Elements productCells = doc.select("div#product-items").select("ul").select("li");
			//	    	 System.out.println("size of productCells-->"+productCells.size());

			String scraped_title = null;
			System.out.println("Product No. :"+product.getSKU_description()+"\n");

			//get brand of product from CSV 
			String[] tokens = product.getSKU_description().split(" "); 
			String brand = null;
			switch(tokens[0]){
			case "FDW":
				brand = Brand.FDW.toString();
				break;
			case "VLN":
				brand = Brand.VLN.toString();
				break;
			default:
				brand = tokens[0];
			}
			String[] brand_tokens = brand.split(" ");
			int reached_cell = 1;
			title_score = tokens.length/2;
			//	       	 boolean in_stock= true;
			//	       	 boolean itc_brand = true;
			for(Element productCell : productCells)
			{

				//prints the current result cell for comparison
				System.out.println("reached_cell : "+ reached_cell);
				int tokens_matched = 0;
				//1. Comparing titles
				//get title of product from web page
				scraped_title = productCell.select("a.el2.mrt5.tx-b.h45.t-center.gatrk.f16.pdl5.pdr5.itemname").text().toLowerCase();

				/*//Availability
				         String[] title_tokens = scraped_title.split(" ");
				         for(String tok : title_tokens)
				         	{
					        	 if(tok.equals("add"))
					        	 {
					        		 in_stock = true;
					        	 }
					        	 else if(tok.equals("out"))
					        	 {
					        		 in_stock = false;
					        	 }

					        }*/

				/*String[] str =null;
				         if(scraped_title.contains("+"))
				         		{
					        		 str = scraped_title.split("\\+");
					        		 scraped_title = str[0];
				         		}*/
				//				         System.out.println("-----------scraped_title---------------"+scraped_title+"\n");

				//finding best match 
				//matching brand name
				for(String b : brand_tokens)
				{
					if(scraped_title.contains(b.toLowerCase()))
					{
						++tokens_matched;
					}
				}
				//				         System.out.println("1. tokens_matched"+ tokens_matched);
				for(String tok : tokens)
				{
					if(scraped_title.contains(tok.toLowerCase()))
					{
						++tokens_matched;				        			 
					}
				}
				//				         System.out.println("1. tokens_matched"+ tokens_matched);

				//1. Comparing prices first     	
				scraped_deal_price = productCell.select("div.h25.t-center").select("strong").text();
				scraped_actual_price = productCell.select("div.h25.t-center").select("strike").text();

				scraped_deal_price = scraped_deal_price.replaceAll("[^0-9]", "");
				scraped_actual_price = scraped_actual_price.replaceAll("[^0-9]", "");

				//			        	 System.out.println("scraped_deal_price : "+scraped_deal_price+"\n");
				//			        	 System.out.println("scraped_actual_price : "+scraped_actual_price+"\n");
				if(!scraped_actual_price.isEmpty())
				{
					if(inRange(scraped_actual_price,product.getPrice()))
					{
						//2. Comparing prices if first condition is true 
						if((tokens_matched > title_score))
						{
							title_score = tokens_matched;
							href = "http://purplle.com"+productCell.select("a").attr("href"); 
							//						        		 bw.write(scraped_actual_price+",");					//write actual_price
							//							        	 bw.write(scraped_deal_price+",");						//write deal_price 	
							//							    		 bw.write(href);
							//bw.flush();										//write href of the best matched product
							match = true;
						} 
					} 
				}else
				{
					if(inRange(scraped_deal_price,product.getPrice()))
					{
						//2. Comparing prices if first condition is true 
						if((tokens_matched > title_score))
						{
							title_score = tokens_matched;
							href = "http://purplle.com"+productCell.select("a").attr("href"); 
							//						        		 bw.write("NA"+",");									//write actual_price
							//							        	 bw.write(scraped_deal_price+",");						//write deal_price 
							//							    		 bw.write(href);
							//bw.flush();										//write href of the best matched product
							match = true;
						} 
					}  
				}

				reached_cell++;
			}
			if(match == false)
			{
				//			 scraped_actual_price = "NA";
				//			 scraped_deal_price = "NA";
				href = "Product not found"; 	
				System.out.println("Product not found");
				//       		 bw.write(scraped_actual_price+",");
				//       		 bw.write(scraped_deal_price+",");						//write curr_price 
				//	         bw.write(href);
				//	    	 bw.flush();
			}
			bw.write(href);
			bw.flush();	 
		}
		System.out.println("File write completed!!\n");
	}

	public void shopclues_finder() throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("shopclues.csv"));
		bw.write("No,SKU Description,MRP,current price,url");
		int num=0;  
		for(Product product:products)
		{
			num++;
			bw.newLine();
			bw.write(Integer.toString(num)+',');
			bw.write(product.getSKU_description()+',');
			bw.write(product.getPrice()+',');
			String search_url="http://search.shopclues.com/?q="+product.getSKU_description().replace(' ','+')+"&auto_suggest=0&cid=0&z=1&sc_z=";
			Document doc = Jsoup.connect(search_url).get(); 
			Elements links=doc.select("div.products_list").select("ul").select("li");
			int score=0;
			int mrp=0;
			for (Element link : links)
			{ 
				score =0;
				String[] tokens=product.getSKU_description().split(" ");


				Elements price=link.select("span.old-price");
				Elements price1=link.select("div.p_price");
				mrp=0;
				if(price.size()>0)
				{
					for(Element pric: price)
					{ 
						if(Integer.parseInt(pric.text())==Integer.parseInt(product.getPrice()))
						{
							mrp++;
						}

					}
				}
				else
				{
					for(Element pric: price1)
					{  
						if(Integer.parseInt(pric.text())==Integer.parseInt(product.getPrice()))
						{ 	                    	 
							mrp++;
						}
					}
				} 	        	 
				Elements product_url=link.select("h5").select("a");
				for(Element pro_url : product_url)
				{ 
					for(String token:tokens)
					{
						String prod=pro_url.text();
						if(prod.contains(token));
						score++;
					}
				}

				int y=0;
				if(score>=2&&mrp>0)
				{
					for(Element pric: price1)
					{ 
						if(y==0)
						{
							bw.write(pric.text()+',');
						}
					}
					for(Element pro_url : product_url)
					{
						bw.write(pro_url.attr("href"));		                 
					}


					break;
				}	        	 
			}
			if(score<2 || mrp==0)
			{
				bw.write("Not Found,");
				bw.write("Not Found");
			}

		}
		bw.flush();
	}
	public void amazon_finder(ArrayList<Product> products)
	{

	}
	//	public void snapdeal_finder() throws IOException, SQLException
	//	{
	//		 BufferedWriter bw=new BufferedWriter(new FileWriter("snapdeal.csv"));
	//		 bw.write("No,SKU Description,MRP,current price,url");
	//		 int num=0;  
	//		 int title_score;
	//		 //String timeStamp = null;
	//		 String href = null,scraped_price = null;	
	//		 
	//	     for(Product product:products)
	//	     {
	//	    	 boolean match = false;
	//	    	 num++;
	//	    	 bw.newLine();									
	//	    	 bw.write(Integer.toString(num)+',');	    	//write No
	//	    	 bw.write(product.getSKU_description()+',');	//write SKU_description
	//	    	 bw.write(product.getPrice()+',');				//write MRP
	//	    	 String search_url="http://www.snapdeal.com/search?keyword="+product.getSKU_description()+"&santizedKeyword="+product.getSKU_description().replace(' ','+')+"&catId=&categoryId=&suggested=false&vertical=p&noOfResults=48&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&url=&utmContent=&dealDetail=&sort=rlvncy";
	//		     
	//	    	 Document doc = Jsoup.connect(search_url).timeout(10*60000000).get(); 
	//	    	 Elements productCells = doc.select("div#products").select("div.product-tuple-description");
	////	    	 System.out.println("size of productCells-->"+productCells.size());
	//	         
	//	    	 String scraped_title = null;
	//	         System.out.println("Product No. :"+product.getSKU_description()+"\n");
	//	         
	//	         //split title of product from CSV 
	//        	 String[] tokens = product.getSKU_description().split(" "); 
	////        	 String brand = getBrand(tokens[0]);
	//        	 String[] brand_tokens = brand.split(" ");
	//        	 
	//        	 int reached_cell = 1;
	//        	 title_score = 0;
	//        	 
	//			 for(Element productCell : productCells)
	//			 {
	//				 		
	//						 //prints the current result cell for comparison
	//			        	 System.out.println("reached_cell : "+ reached_cell);
	//			        	 int tokens_matched = 0;
	//						 //1. Comparing titles
	//						 //get title of product from web page
	//				         scraped_title = productCell.select("p.product-title").text().toLowerCase();
	//				         String[] str =null;
	//				         if(scraped_title.contains("+"))
	//				         		{
	//					        		 str = scraped_title.split("\\+");
	//					        		 scraped_title = str[0];
	//					        		 tokens_matched -= brand_tokens.length;
	//				         		}
	//				         System.out.println("-----------scraped_title---------------"+scraped_title+"\n");
	//				         
	//				         //finding best match 
	//				         //matching brand name
	//				         for(String b : brand_tokens)
	//				         	{
	//					        	 if(scraped_title.contains(b.toLowerCase()))
	//					        	 {
	//					        		 ++tokens_matched;
	//					        	 }
	//					        }
	//				         for(String tok : tokens)
	//				        	{
	//				        		 if(scraped_title.contains(tok.toLowerCase()))
	//				        		 {
	//				        			 ++tokens_matched;				        			 
	//				        		 }
	//				        	}
	//				         //1. Comparing prices first     	
	//						 scraped_price = productCell.select("span.product-price").text();
	//			        	 scraped_price = scraped_price.replaceAll("[^0-9]", "");
	//			        	 System.out.println("scraped_price : "+scraped_price+"\n");
	//				         if(inRange(scraped_price,product.getPrice()))
	//						 {
	//				        	//2. Comparing prices if first condition is true 
	//				        	 if(tokens_matched > title_score)
	//					          {
	//					        		 title_score = tokens_matched;
	//					        		 href = productCell.select("div.product-desc-rating.title-section-expand").select("a").attr("href"); 
	//						        	 bw.write(scraped_price+",");						//write curr_price 
	//						    		 bw.write(href);
	//						    		 bw.flush();										//write href of the best matched product
	//						    		 match = true;
	//					          }
	//				        	 	 
	//						 }
	//		        		 reached_cell++;
	//				}
	//			 if(match == false)
	//			 {
	//				 scraped_price = "NA";
	//        		 href = "Product not found"; 	
	//        		 System.out.println("Product not found");
	//        		 bw.write(scraped_price+",");						//write curr_price 
	//	    		 bw.write(href);
	//	    		 bw.flush();
	//			 }
	//        }
	//	     System.out.println("File write completed!!\n");
	//	}
	//	
	//	public void paytm_finder(ArrayList<Product> products)
	//	{
	//		
	//	}
	//	public void nykaa_finder(ArrayList<Product> products)
	//	{
	//		
	//	}
	//	public void bigBasket_finder() throws IOException, SQLException
	//	{
	//		 BufferedWriter bw=new BufferedWriter(new FileWriter("bigBasket.csv"));
	//		 bw.write("No,SKU Description,MRP,current price,url");
	//		 int num=0;  
	//		 int title_score;
	//		 //String timeStamp = null;
	//		 String href = null,scraped_price = null;	
	//		 
	//	     for(Product product:products)
	//	     {
	//	    	 boolean match = false;
	//	    	 num++;
	//	    	 bw.newLine();									
	//	    	 bw.write(Integer.toString(num)+',');	    	//write No
	////	    	 bw.write(product.getCategory()+',');
	//	    	 bw.write(product.getSKU_description()+',');	//write SKU_description
	//	    	 bw.write(product.getPrice()+',');				//write MRP
	//	    	 String search_url="http://www.bigbasket.com/ps/?q="+product.getSKU_description().replace(' ','+');
	//		     
	//	    	 Document doc = Jsoup.connect(search_url).timeout(10*60000000).get(); 
	//	    	 Elements productCells = doc.select("div#products-container");
	////	    	 System.out.println("size of productCells-->"+productCells.size());
	//	         
	//	    	 String scraped_title = null;
	//	         System.out.println("Product No. :"+product.getSKU_description()+"\n");
	//	         
	//	         //split title of product from CSV 
	//        	 String[] tokens = product.getSKU_description().split(" "); 
	////        	 String brand = getBrand(tokens[0]);
	////        	 String[] brand_tokens = brand.split(" ");
	//        	 
	//        	 int reached_cell = 1;
	//        	 title_score = 0;
	//        	 
	//			 for(Element productCell : productCells)
	//			 {
	//				 		
	//						 //prints the current result cell for comparison
	//			        	 System.out.println("reached_cell : "+ reached_cell);
	//			        	 int tokens_matched = 0;
	//						 //1. Comparing titles
	//						 //get title of product from web page
	//				         scraped_title = productCell.select("p.product-title").text().toLowerCase();
	//				         String[] str =null;
	//				         if(scraped_title.contains("+"))
	//				         		{
	//					        		 str = scraped_title.split("\\+");
	//					        		 scraped_title = str[0];
	////					        		 tokens_matched -= brand_tokens.length;
	//				         		}
	//				         System.out.println("-----------scraped_title---------------"+scraped_title+"\n");
	//				         
	//				         //finding best match 
	//				         //matching brand name
	////				         for(String b : brand_tokens)
	////				         	{
	////					        	 if(scraped_title.contains(b.toLowerCase()))
	////					        	 {
	////					        		 ++tokens_matched;
	////					        	 }
	////					        }
	//				         for(String tok : tokens)
	//				        	{
	//				        		 if(scraped_title.contains(tok.toLowerCase()))
	//				        		 {
	//				        			 ++tokens_matched;				        			 
	//				        		 }
	//				        	}
	//				         //1. Comparing prices first     	
	//						 scraped_price = productCell.select("span.WebRupee").text();
	////			        	 scraped_price = scraped_price.replaceAll("[^0-9]", "");
	//			        	 System.out.println("scraped_price : "+scraped_price+"\n");
	//				         if(inRange(scraped_price,product.getPrice()))
	//						 {
	//				        	//2. Comparing prices if first condition is true 
	//				        	 if(tokens_matched > title_score)
	//					          {
	//					        		 title_score = tokens_matched;
	//					        		 href = productCell.select("div.uiv2-list-box-img-title").select("a").attr("href"); 
	//						        	 bw.write(scraped_price+",");						//write curr_price 
	//						    		 bw.write(href);
	//						    		 bw.flush();										//write href of the best matched product
	//						    		 match = true;
	//					          }
	//				        	 	 
	//						 }
	//		        		 reached_cell++;
	//				}
	//			 if(match == false)
	//			 {
	//				 scraped_price = "NA";
	//        		 href = "Product not found"; 	
	//        		 System.out.println("Product not found");
	//        		 bw.write(scraped_price+",");						//write curr_price 
	//	    		 bw.write(href);
	//	    		 bw.flush();
	//			 }
	//        }
	//	     System.out.println("File write completed!!\n");
	//	}
	//	
	//	public static String getBrand(String Abbreviation) throws SQLException{
	//		java.sql.Statement st = conn.createStatement();
	//		String sql = ("SELECT NAME FROM sqoop.BRAND_METADATA where ABBREVIATION='"+Abbreviation+"';");
	//		java.sql.ResultSet rs = st.executeQuery(sql);
	//		
	//		rs.next();
	//		 String str = rs.getString("NAME");
	//		 System.out.println(str);
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
}

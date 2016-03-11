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

public class FinalF {
	Connection con;
	ArrayList<Product> products;                           // list of products
	String[] myStrings = new String[] {"fiama di wills","vivel love nourish","vivelcell renew","vivel","superia silk","superia","savlon","engage","shower to shower"};	
	List<String> brands =Arrays.asList(myStrings);         // list of brands

	public FinalF() throws IOException
	{
		products=new ArrayList<Product>();

		BufferedReader br = new BufferedReader(new FileReader("D:\\project\\data.csv"));  //csv file to be read
		String line = br.readLine();
		line = br.readLine();
		while(line!=null)                         //traversing the csv file 
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
		BufferedWriter bw=new BufferedWriter(new FileWriter("testf5.csv"));		
		//		Statement stmt = con.createStatement();
		//		ResultSet rs = stmt.executeQuery("select SKU_description from products");
		int num=0; 
		int found=0;
		for(Product product:products)           //traversing the entire list of products
		{

			Random randomGenerator = new Random();          // searching for products at random intervals
			double timeout=Math.random();
			try {
				Thread.sleep(1000*randomGenerator.nextInt(3));
				System.out.println("slept for "+timeout+" seconds");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
						
			num++;
			System.out.println("number: "+num);                  //printing the product number which is processed
			bw.newLine();
			bw.write(Integer.toString(num)+',');
			bw.write(product.getCategory()+',');
			bw.write(product.getSubCategory()+',');
			bw.write(product.getSKU_description()+',');
			bw.write(product.getPrice()+',');

			double price_approx=Double.parseDouble(product.getPrice());   //mrp given to us by itc
			String product_desc=product.getSKU_description()       		  //reconstructing the msku description given to us by itc
					.toLowerCase()
					.replaceAll(" x ", "x")			
					.replaceAll("-", " ")			
					.replaceAll(" gms", "gms")	
					.replaceAll("gms", "gm")					
					.replaceAll("gm", "g")	
					.replaceAll("\\*2g", "g\\*2")
					.replaceAll("\\*3g", "g\\*3")
					.replaceAll("\\*4g", "g\\*4")
					.replaceAll("x2g", "gx2")
					.replaceAll("x3g", "gx3")
					.replaceAll("x4g", "gx4")
					.replaceAll("(?<!x)x3", " pack of 3 ")
					.replaceAll("(?<!x)x4", " pack of 4 ")
					.replaceAll("\\*2", " pack of 2 ")
					.replaceAll("\\*3", " pack of 3 ")
					.replaceAll("\\*4", " pack of 4 ")					
					.replaceAll("showergel", "shower gel")
					.replaceAll("fw","face wash")
					.replaceAll("mix", "mixed")
					.replaceAll("carton", "")
					.replaceAll("cart", "")
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
					.replaceAll("  ", " ")
					.toLowerCase();


			String search=product_desc.replaceAll("\\d+ml", "")
					.replaceAll("\\d+g", "");            // we wont include quantity(ml+gms) while searching
			
			String search_url="http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords="+search.replaceAll(" ", "\\+")+"&rh=i%3Aaps%2Ck%3A"+search.replaceAll(" ", "\\+");
														 //forming the search url
			System.out.println("desc:"+product_desc);    //printing product description that we will tokenize for scoring
			Document doc = Jsoup.connect(search_url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
					.timeout(10*1000).get();             //connecting to the website using the search url and retrieving the complete html
			
			Elements results=doc.select("div#atfResults");
			if(results.size()==0)						//checking if the results are printed using javascript
			{
				results=doc.select("div#fkmr-results0");
			}
			String brand="";

			for(String s:brands)                         //brand validation and replacing in product description
			{
				if(product_desc.contains(s))
				{
					brand=s;
					product_desc=product_desc.replaceAll(s+" ", "");
					break;
				}
			}
			
			System.out.println(product_desc);
			if(results.size()!=0)						 //checking if the no search results are returned
			{
				String[] tokens=product_desc.split(" ");
				int length=tokens.length;
				String url_final="";					//storing the url of the product if found
				Double price_final=0.0;					//storing the price of the product if found
				int max_score=0;						//variable used for storing maximum score
				Elements productsList=results.select("ul").select("li");     //selecting the list of search results 

				for (Element link : productsList)       //traversing the search results
				{ 
					int mrp=0;							//variable used for checking mrp range 
					int score=0;						//variable used for scoring

					String s;
					if(link.select("span.currencyINR").size()==4)    //case where four prices (mrp,selling price,save price,offer price) are listed
					{
						s=link.select("span.currencyINR").get(1).parent().text();
					}
					else if(link.select("span.currencyINR").size()==2||link.select("span.currencyINR").size()==1)  
					{												//case where either two prices (mrp,offer price) or one price (offer price) are listed
						s=link.select("span.currencyINR").get(0).parent().text();
					}
					else								//case where no prices are listed
					{
						System.out.println("broken");
						s="0";
					}		
					Double j=Double.parseDouble(s.replaceAll("[\\D]",""))/100;   //converting the price found from string to double
					Double i=price_approx;
					System.out.println(i);
					
					if((j>=(i*0.90)&&j<=i)   || (j<=(i*1.1)&&j>=i ))             //check if mrp is in 5% range of mrp given to us by itc
					{ 	                    	 
						mrp++;
					}
					String desc=link.select("h2").text().split("with")[0].toLowerCase();  //product description
//					desc=desc.replaceAll("fliama", "fiama")
//							.replaceAll("gm", "g")                                //next phase of string matching
//							.replaceAll("\\*3 g", "g\\*3")						  //basically reconstructing the product description on website to match our description
//							.replaceAll("\\*3", " pack of 3")
//							.replaceAll("3x", "pack of 3 ");
					if(desc.contains(brand))                              //include in scoring if it contains brand
					{
						if(!product_desc.contains("pack"))				  // if its not a pack of products dont pick pack of products from search results
						{					    	
							if(!desc.contains("pack"))
							{						    		
								if(product_desc.contains("ml"))			  // if quantity is in ml dont check for products from search results whose quantity is in gms
								{						    			
									if(desc.contains("ml"))
									{
										for(String token:tokens)         //scoring based on tokens
										{
											if(desc.contains(token.toLowerCase()))
											{score++;}	
										}
									}
								}
								else									 // if quantity is in gms
								{
									for(String token:tokens)			 //scoring based on tokens
									{
										if(desc.contains(token.toLowerCase()))
										{score++;}	
									}
								}
							}
						}
						else                                             // if its a pack of products
						{
							if(product_desc.contains("ml"))
							{
								if(desc.contains("ml"))
								{
									for(String token:tokens)			//scoring based on tokens
									{
										if(desc.contains(token.toLowerCase()))
										{score++;}	
									}
								}
							}
							else
							{
								for(String token:tokens)				//scoring based on tokens
								{
									if(desc.contains(token.toLowerCase()))
									{score++;}	
								}
							}
						}

					}
					
					if(score==tokens.length)                  // if its a 100% string match dont consider mrp range
					{
						System.out.println("yes");
						System.out.println(tokens.length+" "+score);
						max_score=score;
						price_final=j;
						url_final=link.select("a").attr("href");
						break;
					}
					else
					{
						if(brand.equals("engage"))           // engage special case product descriptions are wrong mainly the quantity(e.g.: 150ml,165ml)
						{
							score++;
						}
						if(mrp!=0)							// if mrp is in range then start consider it for selection based on score
						{
							if(score>max_score)				// update max_score basically keeping track of most probable product
							{
								System.out.println("heloooo");
								max_score=score;
								price_final=j;
								url_final=link.select("a").attr("href");							
							}
						}
					}
					System.out.println(score);
				}
				System.out.print("max_score:");
				System.out.println(max_score);
				double threshold=length*0.75;             //defining the threshold
				if(length<=3)							  // length less than 3 will never have 75% it will either be 67% or 100%
				{
					threshold=length*0.66;
				}
				
				if(max_score>=threshold)					// if max_score grater than threshold then fix the product
				{
					System.out.print("found:");
					
					System.out.println(product_desc);
					bw.write(price_final.toString());
					bw.write(",");
					bw.write(url_final);
					found++;  
				}
				else									//product not found
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







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

public class finalffff {

	Connection con;
	Statement stmt;
	ArrayList<Product> products;                           // list of products
	String[] myStrings = new String[] {"fiama di wills","vivel love nourish","vivelcell renew","vivel","superia silk","superia","savlon","engage","shower to shower"};	
	List<String> brands =Arrays.asList(myStrings);         // list of brands

	public finalffff() throws IOException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");	
		
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
	}

	public void amazonFinder() throws ClassNotFoundException, SQLException, IOException
	{	
		con = DriverManager.getConnection("jdbc:mysql://10.6.116.134:3306/sqoop", "20590", "login@123");
		stmt = con.createStatement();
		//		Statement stmt1=con.createStatement();
		
		String selectPortal="select portal_id from Portal where name like '%amazon%'";              //getting portal_id
		ResultSet portals=stmt.executeQuery(selectPortal);
		//		System.out.println("portal:"+portal.getInt("portal_id"));
		int portalId=0;
		while(portals.next())
		{
			portalId=portals.getInt("portal_id");
			System.out.println("portal:"+portalId);
		}
		
		String selectProducts="select * from Product where type='PCP'";                            //getting the list of products
		ResultSet products=stmt.executeQuery(selectProducts);
		
		//		Statement stmt = con.createStatement();
		//		ResultSet products = stmt.executeQuery("select SKU_description from products");
		int num=0; 
		int found=0;
		while(products.next())		//traveproductsing the entire list of products
		{
			System.out.println(products.getString("msku_description"));
			//			System.out.println(products.getDouble("mrp"));

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

			//			bw.write(products.getDouble("mrp")+',');
			double price_approx=products.getDouble("mrp");   //mrp given to us by itc
			String product_desc=products.getString("msku_description")      		  //reconstructing the msku description given to us by itc
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
				String urlFinal="";						//storing the url of the product if found
				Double price_final=0.0;					//storing the price of the product if found
				int max_score=0;						//variable used for storing maximum score
				Elements productsList=results.select("ul").select("li");     //selecting the list of search results 

				for (Element link : productsList)       //traversing the product search results
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
					//						desc=desc.replaceAll("fliama", "fiama")
					//								.replaceAll("gm", "g")                                //next phase of string matching
					//								.replaceAll("\\*3 g", "g\\*3")						  //basically reconstructing the product description on website to match our description
					//								.replaceAll("\\*3", " pack of 3")
					//								.replaceAll("3x", "pack of 3 ");
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
						//						System.out.println("yes");
						//						System.out.println(tokens.length+" "+score);
						max_score=score;
						price_final=j;
						urlFinal=link.select("a").attr("href");
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
								//								System.out.println("heloooo");
								max_score=score;
								price_final=j;
								urlFinal=link.select("a").attr("href");							
							}
						}
					}
					//					System.out.println(score);
				}
				//				System.out.print("max_score:");
				System.out.println(max_score);
				double threshold=length*0.75;             //defining the threshold
				if(length<=3)							  // length less than 3 will never have 75% it will either be 67% or 100%
				{
					threshold=length*0.66;
				}

				if(max_score>=threshold)					// if max_score grater than threshold then fix the product
				{
					urlFinal=urlFinal.split("ref=")[0];
					int urlId=0;
					int productId=products.getInt("product_id");
					
					
					java.util.Date dt = new java.util.Date();
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentTime = sdf.format(dt);
					
					URL urlData=new URL();
					urlData.setProductId(productId);
					urlData.setPortalId(portalId);
					urlData.setUrl(urlFinal);
					urlData.setCreatedTime(currentTime);
									
					urlEntry(urlData);
					

					System.out.println("url id:"+urlId);
					found++;  
				}
				else									//product not found
				{

				}			
			}
		}
		System.out.println(found);
		con.close();
	}

	public void urlEntry(URL urlData) throws SQLException
	{
		Statement stmt1 = con.createStatement();
		String urlExist="select * from URL where product_id="+urlData.getProductId()+" and portal_id="+urlData.getPortalId()+" ORDER BY url_version desc";
		ResultSet urls=stmt1.executeQuery(urlExist);
		System.out.println(urlExist);
		Statement stmt2 = con.createStatement();
		String urlCount="select max(url_id) from URL";
		ResultSet urls1=stmt2.executeQuery(urlCount);
		int urlId=0;
		while(urls1.next())
		{
			urlId=urls1.getInt(1)+1;
		}

		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = 
				new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String currentTime = sdf.format(dt);
		if(urls.next())                                     //if the link had been found previously
		{
			System.out.println(urlData.getUrl());
			System.out.println(urls.getString("url"));
			if(!urlData.getUrl().equals(urls.getString("url")))      // insert new url if the previous url has changed
			{
				System.out.println("equal");
				int urlVersion=urls.getInt("url_version")+1;
				urlId=urls.getInt("url_id");
				String urlUpdate="insert into URL values("+urlId+","+urlVersion+","+urlData.getProductId()+","+urlData.getPortalId()+",'"+urlData.getUrl()+"','"+urlData.getCreatedTime()+"')";
				stmt1.execute(urlUpdate);
			}				
		}
		else                                                      // if we found the link for fiproductst time
		{
			String urlInsert="insert into URL values("+urlId+","+1+","+urlData.getProductId()+","+urlData.getPortalId()+",'"+urlData.getUrl()+"','"+urlData.getCreatedTime()+"')";
			System.out.println(urlInsert);
			stmt1.execute(urlInsert);
		}
		System.out.println("url id:"+urlId);
	}
}







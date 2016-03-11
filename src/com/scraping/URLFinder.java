package com.scraping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLFinder {
	ArrayList<Product> products;
	public URLFinder() throws IOException
	{
	   products=new ArrayList<Product>();
	   BufferedReader br = new BufferedReader(new FileReader("D:\\project\\data.csv"));
	   String line = br.readLine();
	   line = br.readLine();
       while(line!=null)
       {
             String[] b = line.split(",");
             Product product= new Product();
             product.setSKU_description(b[2]);
             product.setPrice(b[3]);
             products.add(product);
             line = br.readLine();
       }
       br.close();
	   System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
	   System.setProperty("http.proxyPort", "8080");
	}

	public void shopclues_finder() throws IOException
	{
		 BufferedWriter bw=new BufferedWriter(new FileWriter("shopclues.csv"));
		 bw.write("No,SKU Description,MRP,current price,url");
		 int num=0; 
		 int found=0;
		 int len=0;
	     for(Product product:products)
	     {
	    	 num++;
	    	 System.out.println(num);
	    	 bw.newLine();
	    	 bw.write(Integer.toString(num)+',');
	    	 bw.write(product.getSKU_description()+',');
	    	 bw.write(product.getPrice()+',');
	    	 String search_url="http://search.shopclues.com/?q="+product.getSKU_description().replace(' ','+')+"&auto_suggest=0&cid=0&z=1&sc_z=";
		     Document doc = Jsoup.connect(search_url).timeout(10*1000).get(); 
	    	 Elements links=doc.select("div.products_list").select("ul").select("li");
	    	 String[] tokens=product.getSKU_description().split(" ");
	    	 len=tokens.length;
	    	 int score=0;
	    	 int mrp=0;
	         for (Element link : links)
	         { 
	        	 score =0;
	        	 mrp=0;	        	 
	        	 
	        	 Elements price=link.select("span.old-price");
	        	 Elements price1=link.select("div.p_price");
	        	 
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
	                	 if(prod.toLowerCase().contains(token.toLowerCase()))
	                		 score++;
	                 }
	        	 }
	        	 
	        	 int y=0;
	        	 if(mrp>0&&score>=(len))
	        	 {
	        		 found++;
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
	         System.out.println(score);
	         System.out.println(len);
	         System.out.println();
	         if(score<(len) || mrp==0)
	         {
	        	 bw.write("Not Found,");
	        	 bw.write("Not Found");
	         }	         
	     }
	     bw.flush();
	     bw.close();
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

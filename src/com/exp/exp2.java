package com.exp;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class exp2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		String g="http://www.shopclues.com/vivel-love-n-nourish-olive-butter-125gm-x-3-soap--shea-butter-75gm-soap-free.html";
//		String[] desc=g.split("/");
//		System.out.println(desc[desc.length-1].replaceAll("-", " ").replaceAll(".html", "").replaceAll("125gm x 3", "125gmx3"));
//		
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
//	    Document doc = Jsoup.connect("http://www.itcportal.com/businesses/fmcg/personal-care-products/vivel-cell-renew.aspx#frbl")
////	    		.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
//	    		.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//	    		.timeout(10*1000).get();
//		
//	    for(Element e:doc.select("div.rightside-content-div").select("a"))
//	    {
//	    	System.out.println(e.text());
//	    }

//		
//		String[] myStrings = new String[] {"fiama di wills","vivel love nourish","vivelcell renew","vivel","superia silk","superia","savlon","engage","shower to ahower"};
//		System.out.println(myStrings);
		purplle();
	}
	
	public static void purplle() throws IOException{

        String search_url = "http://purplle.com/product/fiama-di-wills-exotic-dream-bathing-bar-125-3-gm";
             Document doc = Jsoup.connect(search_url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").timeout(100000).get();  
             Elements results = doc.getAllElements();
             
             ///////
//              System.out.println(doc.select("div#product-items").select("ul").text());
//              
                System.out.println(doc.select("div.pp-7-12.pdl25").select("strike").text());
                System.out.println(doc.select("div.pp-7-12.pdl25").select("div.price").text());

             
             ///////
             //---Title---//
             String title = results.select("div.pp-7-12 pdl25").text();
         System.out.println("Product name: " + title);            
       
        //---Price---//
        String price = results.select("div.pp-g.pdb15").select("div.pp-2-5").select("div.price").text();
        System.out.println(price);
      
        //---Availability---//
        //Since availability in meta-tags which are unextractable.So logic to extract availability information is below//
        if((results.select("div.out-of-stock").attr("style")).matches("(.*)none(.*)"))
             System.out.println("In-stock");
        else
             System.out.println("Out-of-stock");
        
      }




}

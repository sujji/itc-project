package com.exp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class xx {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");
		Document doc = Jsoup.connect("http://www.amazon.in/gp/product/B00N2BWF6Q/ref=s9_simh_gw_p364_d3_i1?pf_rd_m=A1VBAL9TL5WCBF&pf_rd_s=desktop-2&pf_rd_r=1SRV335ZR59JX04REBPM&pf_rd_t=36701&pf_rd_p=817656687&pf_rd_i=desktop").timeout(10*1000).get();


		//shopclues
//				String price_data = doc.select("div.product-pricing").text();
		//		System.out.println(price_data);
		//		
//				Pattern pattern = Pattern.compile("List Price: Rs.(.*?) ");
//				Matcher matcher = pattern.matcher(price_data);
//				
//				Pattern pattern1 = Pattern.compile("Selling Price :Rs.(.*?) ");
//				Matcher matcher1 = pattern1.matcher(price_data);
//				
//				Pattern pattern2 = Pattern.compile("Deal Price:Rs.(.*?) ");
//				Matcher matcher2 = pattern2.matcher(price_data);
		//		
		//		float mrp=0;
		//		float sp=0;
		//		float dp=0;
		//		if (matcher.find())
		//		{
		//			mrp=Float.parseFloat(matcher.group(1).replaceAll("[\\D]",""));
		//			if (matcher1.find())
		//			{
		//				sp=Float.parseFloat(matcher1.group(1).replaceAll("[\\D]",""));
		//			}
		//		}
		//		else
		//		{
		//			if (matcher1.find())
		//			{
		//				sp=Float.parseFloat(matcher1.group(1).replaceAll("[\\D]",""));
		//				mrp=sp;
		//			}
		//		}
		//		if (matcher2.find())
		//		{
		//			dp=Float.parseFloat(matcher2.group(1).replaceAll("[\\D]",""));
		//
		//		}
		//		
		//		


		//amazon
		String price_data = doc.select("div#price_feature_div").text();
		System.out.println(price_data);

		ArrayList<Double> prices= new ArrayList<Double>();

		Pattern pattern = Pattern.compile("Price:    (.*?)00");
		Matcher matcher = pattern.matcher(price_data);

		Pattern pattern1 = Pattern.compile("Sale:    (.*?)00");
		Matcher matcher1 = pattern1.matcher(price_data);

		Pattern pattern2 = Pattern.compile("Deal Price:Rs.(.*?) ");
		Matcher matcher2 = pattern2.matcher(price_data);

		Double mrp=0.0;
		Double sp=0.0;
		Double dp=0.0;
		if (matcher.find())
		{

			prices.add((double) Float.parseFloat(matcher.group(1).replaceAll("[\\D]","")));

		}

		if (matcher1.find())
		{

			prices.add((double) Float.parseFloat(matcher1.group(1).replaceAll("[\\D]","")));
			mrp=sp;
		}

		if (matcher2.find())
		{
			prices.add((double) Float.parseFloat(matcher2.group(1).replaceAll("[\\D]","")));
		}


		System.out.println(prices.size());

		Collections.sort(prices);
		
		if(prices.size()==3)
		{
			mrp=prices.get(prices.size()-1);
			sp=prices.get(prices.size()-2);
			dp=prices.get(prices.size()-3);
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
		System.out.println(prices);
		System.out.println(mrp);
		System.out.println(sp);
		System.out.println(dp);
	}

}

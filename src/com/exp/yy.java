package com.exp;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class yy {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		System.setProperty("http.proxyHost", "10.6.13.11"); // set proxy server
		System.setProperty("http.proxyPort", "8080");			
		String search_url="http://purplle.com/search?q="+"VIVEL Cell Renew Fortify + Repair Night Creme 50gm".replace(" ","%20");
		System.out.println(search_url);
		Document doc = Jsoup.connect(search_url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").timeout(10*1000000).get();

	}

}

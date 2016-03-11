package com.ulti;

import java.io.IOException;
import java.sql.SQLException;

public class tool {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		// TODO Auto-generated method stub
//		URLFinder urlFinder=new URLFinder();
//		urlFinder.amazonFinder();
		
		URLScraper urlScraper=new URLScraper();
		urlScraper.amazonScraper();
	}

}

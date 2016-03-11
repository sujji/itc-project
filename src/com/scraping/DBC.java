package com.scraping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class DBC {
	Connection con;
	public DBC() throws ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");	
	}
	
	public void urlEntry(int productId,int portalId,String urlFinal) throws SQLException
	{
		Statement stmt1 = con.createStatement();
		String urlExist="select * from URL where product_id="+productId+" and portal_id="+portalId+" ORDER BY url_version desc";
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
			System.out.println(urlFinal);
			System.out.println(urls.getString("url"));
			if(!urlFinal.equals(urls.getString("url")))      // insert new url if the previous url has changed
			{
				System.out.println("equal");
				int urlVersion=urls.getInt("url_version")+1;
				urlId=urls.getInt("url_id");
				String urlUpdate="insert into URL values("+urlId+","+urlVersion+","+productId+","+portalId+",'"+urlFinal+"','"+currentTime+"')";
				stmt1.execute(urlUpdate);
			}				
		}
		else                                                      // if we found the link for first time
		{
			String urlInsert="insert into URL values("+urlId+","+1+","+productId+","+portalId+",'"+urlFinal+"','"+currentTime+"')";
			System.out.println(urlInsert);
			stmt1.execute(urlInsert);
		}
		System.out.println("url id:"+urlId);
	}
	
	}


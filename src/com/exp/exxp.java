package com.exp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;


public class exxp {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, BiffException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://10.6.116.134:3306/sqoop", "20590", "login@123");
		Statement stmt = con.createStatement();
		File inputWorkbook = new File("D:\\project\\sku file shared.xls");
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		Workbook w;

		w = Workbook.getWorkbook(inputWorkbook);
		for(Sheet sheet:w.getSheets())
		{
			System.out.println(sheet.getName());
			for (int i = 1; i < sheet.getRows(); i++)
			{
				Cell cell = sheet.getCell(0, i);
				System.out.println(cell.getContents());
				String productInsert="insert into Product(type,category,sub_category,msku_description,ean_code,mrp) values('"+
						sheet.getName()+"','"+
						sheet.getCell(0, i).getContents().replaceAll("'", "''")+"','"+
						sheet.getCell(1, i).getContents().replaceAll("'", "''")+"','"+
						sheet.getCell(2, i).getContents().replaceAll("'", "''")+"','"+
						sheet.getCell(3, i).getContents().replaceAll("'", "''")+"',"+
						Double.parseDouble(sheet.getCell(4, i).getContents())+")";	
				System.out.println(productInsert);
				stmt.execute(productInsert);																
				}
			}
		}
		
		
	
}
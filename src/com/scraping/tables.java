package com.scraping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class tables {
	public static void main(String args[]) throws ClassNotFoundException, SQLException, BiffException, IOException {

		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://10.6.116.134:3306/sqoop", "20590", "login@123");
		Statement stmt = con.createStatement();

		String productTable="CREATE TABLE IF NOT EXISTS `sqoop`.`Product` ("+
				"`product_id` INT NOT NULL AUTO_INCREMENT,"+
				"`type` VARCHAR(100) NULL,"+
				"`category` VARCHAR(100) NULL,"+
				"`sub_category` VARCHAR(100) NULL,"+
				"`msku_description` VARCHAR(100) NULL,"+
				"`ean_code` VARCHAR(100) NULL,"+
				"`mrp` DOUBLE NULL,"+
				"`product_description` TEXT NULL,"+
				"PRIMARY KEY (`product_id`))"+
				"ENGINE = InnoDB "
				;
		stmt.execute(productTable);
		String portalTable="CREATE TABLE IF NOT EXISTS `sqoop`.`Portal` ("+
				"`portal_id` INT NOT NULL AUTO_INCREMENT,"+
				"`name` VARCHAR(45) NULL,"+
				"`url` VARCHAR(100) NULL,"+
				"`portal_description` TEXT NULL,"+
				"PRIMARY KEY (`portal_id`))"+
				"ENGINE = InnoDB "
				;
		stmt.execute(portalTable);
		String urlTable="CREATE TABLE IF NOT EXISTS `sqoop`.`URL` ("+
				"`url_id` BIGINT NOT NULL,"+
				"`url_version` INT NOT NULL,"+
				"`product_id` INT NULL,"+
				"`portal_id` INT NULL,"+
				"`url` TEXT NULL,"+
				"`created_time` DATETIME NULL,"+
				"PRIMARY KEY (`url_id`, `url_version`),"+
				"INDEX `product_id_fk_idx` (`product_id` ASC),"+
				"INDEX `portal_id_fk_idx` (`portal_id` ASC),"+
				"CONSTRAINT `product_id_fk`"+
				" FOREIGN KEY (`product_id`)"+
				" REFERENCES `sqoop`.`Product` (`product_id`)"+
				" ON DELETE NO ACTION"+
				" ON UPDATE NO ACTION,"+
				"CONSTRAINT `portal_id_fk`"+
				" FOREIGN KEY (`portal_id`)"+
				" REFERENCES `sqoop`.`Portal` (`portal_id`)"+
				" ON DELETE NO ACTION"+
				" ON UPDATE NO ACTION)"+
				"ENGINE = InnoDB "
				;
		stmt.execute(urlTable);		
		String scrapedTable="CREATE TABLE IF NOT EXISTS `sqoop`.`Scraped_Data` ("+
				"`scraped_data_id` BIGINT NOT NULL AUTO_INCREMENT,"+
				"`url_id` BIGINT NULL,"+
				"`url_version` INT NULL,"+
				"`mrp` DOUBLE NULL,"+
				"`price` DOUBLE NULL,"+
				"`stock` INT NULL,"+
				"`created_time` DATETIME NULL,"+
				"`date` DATE NULL,"+
				"`error_code` INT NULL,"+
				"PRIMARY KEY (`scraped_data_id`),"+
				"INDEX `url_id_version_fk_idx` (`url_version` ASC, `url_id` ASC),"+
				"CONSTRAINT `url_id_version_fk`"+
				" FOREIGN KEY (`url_id` , `url_version`)"+
				" REFERENCES `sqoop`.`URL` (`url_id` , `url_version`)"+
				" ON DELETE NO ACTION"+
				" ON UPDATE NO ACTION)"+
				"ENGINE = InnoDB";
		stmt.execute(scrapedTable);

		File inputWorkbook = new File("D:\\project\\sku file shared.xls");               // product insertion
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		Workbook w;
		w = Workbook.getWorkbook(inputWorkbook);
		for(Sheet sheet:w.getSheets())
		{
			System.out.println(sheet.getName());
			for (int i = 1; i < sheet.getRows(); i++)
			{
				String productInsert="insert into sqoop.Product(type,category,sub_category,msku_description,ean_code,mrp) values('"+
						sheet.getName()+"','"+
						sheet.getCell(0, i).getContents().replaceAll("'", "''")+"','"+
						sheet.getCell(1, i).getContents().replaceAll("'", "''")+"','"+
						sheet.getCell(2, i).getContents().replaceAll("'", "''")+"','"+
						sheet.getCell(3, i).getContents().replaceAll("'", "''")+"',"+
						Double.parseDouble(sheet.getCell(4, i).getContents())+")";	
				stmt.execute(productInsert);																
			}
		}
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\project\\portals.txt"));         //portal insertion
		String line = br.readLine();
		while(line!=null)                         //traversing the csv file 
		{
			String[] b = line.split(",",3);
			System.out.println(b[2]);
			line = br.readLine();
			String portalInsert="insert into sqoop.Portal(name,url,portal_description) values('"+
					b[0]+"','"+
					b[1].replaceAll("'", "''")+"','"+
					b[2].replaceAll("'", "''")+"')";	
			stmt.execute(portalInsert);
		}	
		br.close();		
		con.close();
	}
}

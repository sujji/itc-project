package com.scraping;

public class Product {
	String category;
	String subCategory;
	String SKU_description;
	String price;
	String url;
	public String getSKU_description() {
		return SKU_description;
	}
	public void setSKU_description(String sKU_description) {
		SKU_description = sKU_description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	
}

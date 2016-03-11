package com.scraping;

public class URL {

	public long urlId;
	public int urlVersion;
	public int productId;
	public int portalId;
	public String url;
	public String createdTime;
	public long getUrlId() {
		return urlId;
	}
	public void setUrlId(long urlId) {
		this.urlId = urlId;
	}
	public int getUrlVersion() {
		return urlVersion;
	}
	public void setUrlVersion(int urlVersion) {
		this.urlVersion = urlVersion;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getPortalId() {
		return portalId;
	}
	public void setPortalId(int portalId) {
		this.portalId = portalId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

}

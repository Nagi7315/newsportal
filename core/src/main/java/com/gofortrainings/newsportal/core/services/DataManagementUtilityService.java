package com.gofortrainings.newsportal.core.services;


public interface DataManagementUtilityService {
	
	public void downloadAssets(String filePath, String archivalPath);
	
	public boolean unpublishAssets(String filePath);
	
	public void DeleteAssets(String filePath);

}

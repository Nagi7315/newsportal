package com.gofortrainings.newsportal.core.services;

import java.util.Map;

import org.apache.sling.api.SlingHttpServletResponse;

public interface GetListOfAssetsService {
	
	public Map<String,String> getListOfAssets(String folderPath, SlingHttpServletResponse response);
	
	public String getAsstesMetaData(String assetPath);
	
}

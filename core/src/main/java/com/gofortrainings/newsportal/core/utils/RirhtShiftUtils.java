package com.gofortrainings.newsportal.core.utils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;


public class RirhtShiftUtils {
	
	public static List<NameValuePair> createNameValuePair(SlingHttpServletRequest request){
		 Enumeration<String> params = request.getParameterNames();
		 List<NameValuePair> nvPair = new ArrayList<>();
		 while(params.hasMoreElements()) {
			 String key = params.nextElement();
			 nvPair.add(new BasicNameValuePair(key,request.getParameter(key)));
		 }
		return nvPair;		
	}
	
	public static List<NameValuePair> createNameValuePairsFromJson(SlingHttpServletRequest request){
		String line = null;
		List<NameValuePair> nvPair = new ArrayList<>();
		try {
		    StringBuffer jb = new StringBuffer();		
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
			try(JsonReader jsonReader = Json.createReader(new StringReader(jb.toString()))){
				JsonObject responseJson = jsonReader.readObject();
				responseJson.forEach((key,value)->{
					nvPair.add(new BasicNameValuePair(key,request.getParameter(key)));
					});
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return nvPair;
		
	}
	

}

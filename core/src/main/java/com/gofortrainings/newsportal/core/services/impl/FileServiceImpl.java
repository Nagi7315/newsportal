package com.gofortrainings.newsportal.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import com.gofortrainings.newsportal.core.configuration.FileConfiguration;
import com.gofortrainings.newsportal.core.services.FileService;

@Component(service = FileService.class)
@Designate(ocd = FileConfiguration.class)
public class FileServiceImpl implements FileService{
	
	private String csvFilePath;
	private String emailId;
	
	public String getCsvFilePath() {
		return csvFilePath;
	}

	public String getEmailId() {
		return emailId;
	}

	@Activate
	public void activate(FileConfiguration config) {
		csvFilePath = config.csvFilePath();
	}
	
	@Modified
	public void update(FileConfiguration config) {
		csvFilePath = config.csvFilePath();
	}



}

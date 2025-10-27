package com.gofortrainings.newsportal.core.services.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.gofortrainings.newsportal.core.services.AssetManagementService;
import com.gofortrainings.newsportal.core.services.FileService;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Component(service = AssetManagementService.class, immediate = true)
public class AssetManagementServiceImpl implements AssetManagementService {

	private static final Logger log = LoggerFactory.getLogger(AssetManagementServiceImpl.class);

	@Reference
	private Replicator replicator;

	@Reference
	private FileService fileService;

	@Reference
	ResourceResolverUtil resourceResolverUtil;

	private List<String> errorAssetPaths = new ArrayList<>();
	private List<String> errorMessages = new ArrayList<>();

	@Override
	public void processAssets(String filePath) {
		if (!StringUtils.isBlank(filePath)) {
			String[] split = filePath.split("\\.");
			String extension = split[1];
			if (extension.equals("csv") || extension.equals("xlxs")) {
				// String filePath = fileService.getCsvFilePath();
				try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
					Resource resource = resolver.getResource(filePath);
					Asset asset = resource.adaptTo(Asset.class);
					List<String> assetPaths = readFromCsv(asset);

					for (String assetPath : assetPaths) {
						try {
							unpublishAsset(assetPath, resolver);
							generateErrorReport(errorAssetPaths, errorMessages);
						} catch (PersistenceException e) {
							log.error("Error unpublishing and deleting asset at path: " + assetPath, e);
						}
					}
					log.info("Successfully processed assets");
				} catch (LoginException | IOException e) {
					log.error("Error processing assets", e);
				}
			} else {
				log.info("The asset extension must be csv or xlxs");
			}
		}
	}

	@Override
	public void processAssets1(String filePath) {
		if (!StringUtils.isBlank(filePath)) {
			String[] split = filePath.split("\\.");
			String extension = split[1];
			if (extension.equals("csv") || extension.equals("xlxs")) {
				// String filePath = fileService.getCsvFilePath();
				try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
					Resource resource = resolver.getResource(filePath);
					Asset asset = resource.adaptTo(Asset.class);
					List<String> assetPaths = readFromCsv(asset);

					for (String assetPath : assetPaths) {
						try {
							unpublishAssetAndDelete(assetPath, resolver);
							generateErrorReport(errorAssetPaths, errorMessages);
						} catch (PersistenceException e) {
							log.error("Error unpublishing and deleting asset at path: " + assetPath, e);
						}
					}
					log.info("Successfully processed assets");
				} catch (LoginException | IOException e) {
					log.error("Error processing assets", e);
				}
			} else {
				log.info("The asset extension must be csv or xlxs");
			}
		}
	}

	private List<String> readFromCsv(Asset asset) {
		List<String> assetPaths = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(asset.getOriginal().getStream()));) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				String assetPath = values[0].trim().toString();
				assetPaths.add(assetPath);
			}
		} catch (IOException e) {
			log.error("Error reading CSV file", e);
		}
		return assetPaths;
	}

	private void unpublishAsset(String assetPath, ResourceResolver resolver) throws IOException {
		log.debug("Entering unpublishAsset method");
		Session session = resolver.adaptTo(Session.class);
		Resource resource = resolver.getResource(assetPath);
		if (resource != null) {
			try {
				replicator.replicate(session, ReplicationActionType.DEACTIVATE, assetPath);
				/*
				 * ReplicationStatus replicationStatus =
				 * resource.adaptTo(ReplicationStatus.class); boolean status = replicationStatus
				 * != null && replicationStatus.isDeactivated(); if (status) {
				 * log.info("Successfully unpublished asset at path: {}", assetPath); //
				 * deleteAsset(assetPath, resolver, resource); }
				 */
			} catch (ReplicationException e) {
				log.error("Error unpublishing asset at path: " + assetPath, e);
			}
		} else {
			log.warn("Asset Path is Not available: {}", assetPath);
			captureError(assetPath, "Asset Path is Not available");
		}
	}

	private void unpublishAssetAndDelete(String assetPath, ResourceResolver resolver) throws IOException {
		log.debug("Entering unpublishAsset method");
		Session session = resolver.adaptTo(Session.class);
		Resource resource = resolver.getResource(assetPath);
		if (resource != null) {
			try {
				replicator.replicate(session, ReplicationActionType.DEACTIVATE, assetPath);
				ReplicationStatus replicationStatus = resource.adaptTo(ReplicationStatus.class);
				boolean status = replicationStatus != null && replicationStatus.isDeactivated();
				if (status) {
					log.info("Successfully unpublished asset at path: {}", assetPath);
					resolver.delete(resource);
					resolver.commit();
				}
			} catch (ReplicationException e) {
				log.error("Error unpublishing asset at path: " + assetPath, e);
			}
		} else {
			log.warn("Asset Path is Not available: {}", assetPath);
			captureError(assetPath, "Asset Path is Not available");
		}
	}
	/*
	 * private void deleteAsset(String assetPath, ResourceResolver resolver,
	 * Resource resource) throws IOException {
	 * log.debug("Entering deleteAsset method"); if (resource != null) { try {
	 * resolver.delete(resource); resolver.commit();
	 * log.info("Successfully deleted asset at path: {}", assetPath); } catch
	 * (PersistenceException e) { log.error("Error deleting asset at path: " +
	 * assetPath, e); } } else { log.warn("Asset Path is Not available: {}",
	 * assetPath); captureError(assetPath,"Asset Path is Not available"); } }
	 */

	private void captureError(String assetPath, String errorMessage) {
		log.info("Inside  ");
		errorAssetPaths.add(assetPath);
		errorMessages.add(errorMessage);
	}

	public void generateErrorReport(List<String> errorAssetPaths, List<String> errorMessages) {
		 String damFolderPath = "/content/dam/newsportal/";
		    String fileName = "error_report.xlsx";
		    String filePath = damFolderPath + fileName;
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Error Report");

			// Create header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Asset Path");
			headerRow.createCell(1).setCellValue("Error Message");

			// Populate data rows
			for (int i = 0; i < errorAssetPaths.size(); i++) {
				Row row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(errorAssetPaths.get(i));
				row.createCell(1).setCellValue(errorMessages.get(i));
			}

			// Save the workbook to a file
			 try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\j nagireddy\\OneDrive\\Desktop\\errorReport\\error_report.xlsx")) {
			 workbook.write(fileOut);
				/*
				 * try (ResourceResolver resolver =
				 * ResourceResolverUtil.newResolver(resourceResolverFactory)){
				 * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				 * workbook.write(outputStream);
				 * 
				 * ByteArrayInputStream inputStream = new
				 * ByteArrayInputStream(outputStream.toByteArray()); AssetManager assetManager =
				 * resolver.adaptTo(AssetManager.class); assetManager.createAsset(filePath,
				 * inputStream, "application/xlxs", true); Session session =
				 * resolver.adaptTo(Session.class); session.save();
				 */
	             }			
		} catch(Exception e) {
			log.error("Error generating error report", e);
		}
	}}
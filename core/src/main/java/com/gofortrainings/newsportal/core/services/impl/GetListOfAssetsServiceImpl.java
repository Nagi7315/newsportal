package com.gofortrainings.newsportal.core.services.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.query.Query;

import com.gofortrainings.newsportal.core.utils.Constants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.gofortrainings.newsportal.core.services.GetListOfAssetsService;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.SlingHttpServletResponse;

@Component(service = GetListOfAssetsService.class, immediate = true)
public class GetListOfAssetsServiceImpl implements GetListOfAssetsService {

	@Reference
	ResourceResolverUtil resourceResolverUtil;

	private static final Logger log = LoggerFactory.getLogger(GetListOfAssetsServiceImpl.class);

	@Override
	public Map<String, String> getListOfAssets(String folderPath, SlingHttpServletResponse response) {
		try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
			Map<String, String> listOfAssets = new HashMap<String, String>();
			if (folderPath != null) {
				List<String> assetPaths = new ArrayList<>();
				List<String> assetNames = new ArrayList<>();
				List<String> metaDataList = new ArrayList<>();
				String query = "SELECT * FROM [dam:Asset] AS asset WHERE ISDESCENDANTNODE(asset, '" + folderPath + "')";
				Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);
				while (result.hasNext()) {
					Resource assetResource = result.next();
					Asset asset = DamUtil.resolveToAsset(assetResource);
					if (asset != null) {
						String assetPath = asset.getPath();
						assetPaths.add(assetPath);
						String assetName = assetResource.getName();
						assetNames.add(assetName);
						listOfAssets.put(assetPath, assetName);
						String metaData = getAsstesMetaData(assetPath);
						metaDataList.add(metaData);
					} else {
						log.warn("Asset is null: {}");
					}
				}
				generateAssetReport(response, assetPaths, assetNames, metaDataList);
				return listOfAssets;
			}
		} catch (LoginException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAsstesMetaData(String assetPath) {
		String result = "";
		try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
			Resource resource = resolver.getResource(assetPath);
			Asset asset = resource.adaptTo(Asset.class);
			Map<String, Object> metadata = asset.getMetadata();
			for (Map.Entry<String, Object> entry : metadata.entrySet()) {
				result += entry.getKey() + "##" + entry.getValue().getClass().getSimpleName() + "##" + entry.getKey()
						+ "|";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void generateAssetReport(SlingHttpServletResponse response, List<String> assetPaths, List<String> assetNames,
			List<String> metaData) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Assets Report");

			// Create header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("File Path");
			headerRow.createCell(1).setCellValue("File Name");
			headerRow.createCell(1).setCellValue("Metadata");

			// Populate data rows
			for (int i = 0; i < assetPaths.size(); i++) {
				Row row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(assetPaths.get(i));
				row.createCell(1).setCellValue(assetNames.get(i));
				row.createCell(2).setCellValue(metaData.get(i));
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=assetsReport.xlsx");
			try (OutputStream out = response.getOutputStream()) {
				workbook.write(out);
			}
		} catch (Exception e) {
			log.error("Error generating error report", e);
		}
	}

}

package com.gofortrainings.newsportal.core.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import com.gofortrainings.newsportal.core.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.gofortrainings.newsportal.core.services.DataManagementUtilityService;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;

@Component(service = DataManagementUtilityService.class)
public class DataManagementUtilityServiceImpl implements DataManagementUtilityService{
	
	private static final Logger log = LoggerFactory.getLogger(DataManagementUtilityServiceImpl.class);

	@Reference
	ResourceResolverUtil resourceResolverUtil;
	@Reference
	Replicator replicator;
	
	@Override
	public void downloadAssets(String filePath, String archivalPath) {
		if (filePath != null) {
			try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
				Resource fileResource = resolver.getResource(filePath);
				if (fileResource != null) {
					Asset fileAsset = fileResource.adaptTo(Asset.class);
					List<String> assetPaths = readFromCsv(fileAsset);
					for (String assetPath : assetPaths) {
						Resource assetResource = resolver.getResource(assetPath);
						if (assetResource != null) {
							Resource folderResource = assetResource.getParent();
							if (folderResource != null) {
								String folderPath = folderResource.getPath();
								Asset asset = assetResource.adaptTo(Asset.class);
								String fileName = asset.getName();
								Rendition original = asset.getOriginal();
								if (original != null) {
									InputStream inputStream = original.adaptTo(InputStream.class);
									if (inputStream != null) { 									
										File targetFile = new File(archivalPath + folderPath, fileName);
										FileUtils.copyInputStreamToFile(inputStream, targetFile);										
										log.debug("Downloaded asset: " + targetFile.getAbsolutePath());
										markArchiveProperty(assetResource);
										
									} else {
										log.warn("InputStream is null for asset: {}", assetResource.getPath());
									}
								} else {
									log.warn("No valid rendition found for asset: {}", assetResource.getPath());
								}
							} else {
								log.info("Folder resource is null {} ");
							}
						} else {
							log.info("Asset resource is null {} ");
						}
					}
				} else {
					log.info("File resource is null {} ");
				}
			} catch (LoginException | IOException e) {
				log.error("Exception {}", e.getMessage());
			}
		} else {
			log.info("Filepath is null {} ");
		}
	}
	
	public void markArchiveProperty(Resource assetResource) {
		if (assetResource != null) {
			try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
				String assetPath = assetResource.getPath();
				Resource contentResource = resolver.getResource(assetPath + "/jcr:content");
				if (contentResource != null) {
					ModifiableValueMap properties = contentResource.adaptTo(ModifiableValueMap.class);
					if (properties != null) {
						properties.put("isArchived", true);
					}
				}
			} catch (LoginException e) {
				log.error("Exception {}", e.getMessage());
			}
		} else {
			log.info("Asset resource is null {} ");
		}
	}

	@Override
	public boolean unpublishAssets(String filePath) {
		boolean status = false;
		if (filePath != null) {
			try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
				Resource fileResource = resolver.getResource(filePath);
				if (fileResource != null) {
					Asset fileAsset = fileResource.adaptTo(Asset.class);
					List<String> assetPaths = readFromCsv(fileAsset);
					for (String assetPath : assetPaths) {
						if (assetPath != null) {
							Session session = resolver.adaptTo(Session.class);
							replicator.replicate(session, ReplicationActionType.DEACTIVATE, assetPath);
							Resource assetResource = resolver.getResource(assetPath);
							ReplicationStatus replicationStatus = assetResource.adaptTo(ReplicationStatus.class);
							status = replicationStatus != null && replicationStatus.isDeactivated();
							return status;
						} else {
							log.info("Asset path is null {} ");
							return status;
						}
					}
				}else {
					log.info("File Resource is null {} ");
				}
			} catch (LoginException | ReplicationException e) {
				log.error("Exception {}", e.getMessage());
			}
		}else {
			log.info("File path is null {} ");
		}
		return status;
	}

	@Override
	public void DeleteAssets(String filePath) {		
		if (filePath != null) {
			try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
				Resource fileResource = resolver.getResource(filePath);
				if (fileResource != null) {
					Asset fileAsset = fileResource.adaptTo(Asset.class);
					List<String> assetPaths = readFromCsv(fileAsset);
					for (String assetPath : assetPaths) {
						Resource assetResource = resolver.getResource(assetPath);
						if (assetResource != null) {
							ValueMap valueMap = assetResource.getValueMap();
							if (valueMap.containsKey("isArchived")) {
								Boolean isArchived = valueMap.get("isArchived", true);
								boolean status = unpublishAssets(assetPath);
								if (status && isArchived) {
									resolver.delete(assetResource);
									resolver.commit();
									log.info("Successfully deleted asset at path: {}", assetPath);
								} else {
									log.info("Asset is not unpunlished {}", assetPath);
								}
							} else {
								log.info("Asset is not Archived {}", assetPath);
							}
						} else {
							log.info("Asset Resource is null {} ");
						}
					}
				} else {
					log.info("File Resource is null {} ");
				}
			} catch (LoginException | PersistenceException e) {
				log.error("Exception {}", e.getMessage());
			}
		} else {
			log.info("File path is null {} ");
		}

	}
	
	 private List<String> readFromCsv(Asset asset) {
	        List<String> assetPaths = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(asset.getOriginal().getStream()));){
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

}

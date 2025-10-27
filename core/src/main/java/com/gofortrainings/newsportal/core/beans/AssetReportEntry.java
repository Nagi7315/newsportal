package com.gofortrainings.newsportal.core.beans;

public class AssetReportEntry {
	
	private String assetPath;
	
	private boolean success;

	private String errorMessage;

	public void setAssetPath(String assetPath) {
		this.assetPath = assetPath;
	}

	public void setSuccess(boolean b) {
		this.success = b;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getAssetPath() {
		return assetPath;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}

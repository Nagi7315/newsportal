package com.gofortrainings.newsportal.core.models;

public class CountryInfo {

    private final String countryCode;

    private final String countryName;

    private final String region;

    public CountryInfo(String countryCode, String countryName, String region) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.region = region;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegion() {
        return region;
    }
}

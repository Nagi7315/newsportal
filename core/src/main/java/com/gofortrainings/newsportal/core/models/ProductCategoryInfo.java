package com.gofortrainings.newsportal.core.models;

public class ProductCategoryInfo {
  private String categoryProductTitle;

  private int categoryProductCount;

  private String categoryProductImage;

  public String getCategoryProductTitle() {
    return categoryProductTitle;
  }

  public void setCategoryProductTitle(String categoryProductTitle) {
    this.categoryProductTitle = categoryProductTitle;
  }

  public int getCategoryProductCount() {
    return categoryProductCount;
  }

  public void setCategoryProductCount(int categoryProductCount) {
    this.categoryProductCount = categoryProductCount;
  }

  public String getCategoryProductImage() {
    return categoryProductImage;
  }

  public void setCategoryProductImage(String categoryProductImage) {
    this.categoryProductImage = categoryProductImage;
  }
}

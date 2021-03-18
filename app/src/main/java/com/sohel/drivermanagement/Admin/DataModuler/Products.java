package com.sohel.drivermanagement.Admin.DataModuler;

public class Products {
    String id;
    String categoryId;
    String productName;
    String productDescription;
    String image;
    String phoneNumber;

public Products(){}
    public Products(String id,String categoryId, String productName, String productDescription, String image,String phoneNumber) {
        this.id = id;
        this.categoryId=categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.image = image;
        this.phoneNumber=phoneNumber;
    }


    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getImage() {
        return image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

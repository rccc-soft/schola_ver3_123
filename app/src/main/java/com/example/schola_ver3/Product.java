package com.example.schola_ver3;
//1/15_egu
public class Product {
    private String productId;
    private String sellerId;
    private String url;
    private String image;
    private int price;
    private String name;
    private String category;
    private int region;
    private long date;
    private String description;
    private int sold;
    private String delivery;

    public Product(String productId, String sellerId, String url, String image, int price, String name, String category,
                   int region, long date, String description, int sold, String delivery) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.url = url;
        this.image = image;
        this.price = price;
        this.name = name;
        this.category = category;
        this.region = region;
        this.date = date;
        this.description = description;
        this.sold = sold;
        this.delivery = delivery;
    }

    // ゲッター
    public String getProductId() {
        return productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getRegion() {
        return region;
    }

    public long getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getSold() {
        return sold;
    }

    public String getDelivery() {
        return delivery;
    }

    public boolean isSold() {
        return sold == 1;
    }
}

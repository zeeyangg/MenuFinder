package com.inti.student.menufinder;


public class Item {
    private String itemName;
    private double price;
    private int thumbnail;
    private int quantity = 0;

    public Item() {
    }

    public Item (String itemName, double price, int thumbnail) {
        this.itemName = itemName;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

package com.hs_esslingen.insy.models;

import com.opencsv.bean.CsvBindByName;

public class InventoryItem {

    @CsvBindByName(column = "Kst / Lab")
    String costCenter;

    @CsvBindByName(column = "InventarNr")
    String inventoryNumber;

    @CsvBindByName(column = "Anzahl")
    Integer quantity;

    @CsvBindByName(column = "Gerätetyp / Software")
    String description;

    @CsvBindByName(column = "Firma")
    String company;

    @CsvBindByName(column = "Preis / €")
    Double price;

    @CsvBindByName(column = "Datum")
    String date;

    @CsvBindByName(column = "Seriennummer")
    String serialNumber;

    @CsvBindByName(column = "Standort/Nutzer")
    String location;

    @CsvBindByName(column = "Besteller")
    String orderer;

    // Keep this or OpenCSV will fail, instantiating objects
    public InventoryItem() {
    }

    @Override
    public String toString() {
        return "InventarNr: " + inventoryNumber + ", Quantity: " + quantity + ", Description: " + description + ", Company: " + company + ", Price: " + price;
    }

    public InventoryItem(String costCenter, String inventoryNumber, Integer quantity, String description, String company, Double price, String date, String serialNumber, String location, String orderer) {
        this.costCenter = costCenter;
        this.inventoryNumber = inventoryNumber;
        this.quantity = quantity;
        this.description = description;
        this.company = company;
        this.price = price;
        this.date = date;
        this.serialNumber = serialNumber;
        this.location = location;
        this.orderer = orderer;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderer() {
        return orderer;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }
}

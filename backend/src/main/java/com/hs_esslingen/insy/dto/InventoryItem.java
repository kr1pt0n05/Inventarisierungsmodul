package com.hs_esslingen.insy.dto;

import com.opencsv.bean.CsvBindByName;

public class InventoryItem {

    @CsvBindByName(column = "Kst / Lab")
    String costCenter;

    @CsvBindByName(column = "InventarNr")
    String inventoryNumber;

    @CsvBindByName(column = "Anzahl")
    String quantity;

    @CsvBindByName(column = "Gerätetyp / Software")
    String description;

    @CsvBindByName(column = "Firma")
    String company;

    @CsvBindByName(column = "Preis / €")
    String price;

    @CsvBindByName(column = "Datum")
    String date;

    @CsvBindByName(column = "Seriennummer")
    String serialNumber;

    @CsvBindByName(column = "Standort/Nutzer")
    String location;

    @CsvBindByName(column = "Besteller")
    String orderer;

    @CsvBindByName(column = "Kommentare")
    String comment;

    // Keep this or OpenCSV will fail, instantiating objects
    public InventoryItem() {
    }

    @Override
    public String toString() {
        return
                "Kostenstelle: " + costCenter + " InventarNr.: " + inventoryNumber + " Anzahl: " + quantity + " Gerätetyp/Software: " + description + " Firma: " + company + " Preis: " + price + " Datum: " + date + " Seriennummer: " + serialNumber + " Standort/Nutzer: " + location + " Besteller: " + orderer;
    }

    public InventoryItem(String costCenter, String inventoryNumber, String quantity, String description, String company, String price, String date, String serialNumber, String location, String orderer, String comment) {
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
        this.comment = comment;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

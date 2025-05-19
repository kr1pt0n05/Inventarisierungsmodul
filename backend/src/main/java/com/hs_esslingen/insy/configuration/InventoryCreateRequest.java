package com.hs_esslingen.insy.configuration;

import java.math.BigDecimal;
import java.util.List;

public class InventoryCreateRequest {
    public Integer inventories_id;
    public String cost_center;
    public String inventories_description;
    public String company;
    public BigDecimal inventories_price;
    public String inventories_serial_number;
    public String inventories_location;
    public Object orderer; // kann Integer (id) oder String (name) sein
    public List<Integer> tags;
}



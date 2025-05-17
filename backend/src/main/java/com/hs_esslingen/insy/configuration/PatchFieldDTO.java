package com.hs_esslingen.insy.configuration;


// Used to store attributes of a PATCH request
// to be used in the InventoriesController.updateInventory method
// to update only the specified fields of an inventory item
public class PatchFieldDTO {
    private String fieldName;
    private Object fieldValue;

    public PatchFieldDTO(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
    
}
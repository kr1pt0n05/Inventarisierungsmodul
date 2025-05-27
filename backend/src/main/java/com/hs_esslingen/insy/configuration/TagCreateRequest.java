package com.hs_esslingen.insy.configuration;

public class TagCreateRequest {
    private String name;

    public TagCreateRequest() {}

    public TagCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }       
}

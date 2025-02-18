package com.jumpcloud.util;

public class JumpCloudFilter {
    private final String attributeName;
    private final Object attributeValue;
    public JumpCloudFilter(String attributeName, Object attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public Object getAttributeValue() {
        return attributeValue;
    }
}
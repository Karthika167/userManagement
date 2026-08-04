package com.avio.dao.model;

public enum OrganizationType {
    
	AIRLINE("Airline"),
    
	AIRPORT("Airport"),
    
	OTHER("Other");

    private final String displayName;

    OrganizationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
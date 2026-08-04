package com.avio.dao.model;

public enum PersonnelRole {
	
    ACCOUNTABLE_MANAGER("Accountable Manager"),
    
    QUALITY_MANAGER("Quality Manager"),
    
    QA_AUDITOR("QA Auditor"),
    
    QC_INSPECTOR("QC Inspector"),
    
    CERTIFYING_STAFF("Certifying Staff"),
    
    MECHANIC("Mechanic"),
    
    PILOT("Pilot"),
    
    TRAINING_COORDINATOR("Training Coordinator"),
    
    DOCUMENT_CONTROLLER("Document Controller"),
    
    ADMIN("Admin"),
    
    OTHER("Other");

	private final String displayName;

    PersonnelRole(String displayName) {
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

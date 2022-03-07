package com.example.myuniversity.WorkPlace.Support.RecView;

public class Element {
    private String pairIndex;
    private String auditIndex;

    public Element(String pairIndex, String auditIndex) {
        this.pairIndex = pairIndex;
        this.auditIndex = auditIndex;
    }

    public String getPairIndex() {
        return pairIndex;
    }

    public void setPairIndex(String pairIndex) {
        this.pairIndex = pairIndex;
    }

    public String getAuditIndex() {
        return auditIndex;
    }

    public void setAuditIndex(String auditIndex) {
        this.auditIndex = auditIndex;
    }
}

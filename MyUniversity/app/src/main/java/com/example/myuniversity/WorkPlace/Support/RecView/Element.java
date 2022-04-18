package com.example.myuniversity.WorkPlace.Support.RecView;

import androidx.annotation.NonNull;

public class Element {
    private int number;
    private String pairIndex;
    private String auditIndex;

    public Element(int number, String pairIndex, String auditIndex) {

        if (pairIndex == null)
            pairIndex = "";

        if (auditIndex == null)
            auditIndex = "";

        this.number = number;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Element{ № " + String.valueOf(number) + " pairIndex = \'" + pairIndex + "\', auditIndex = \'" + auditIndex + "\'" + " }";
    }
}

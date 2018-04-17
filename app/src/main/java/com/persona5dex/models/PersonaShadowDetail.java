package com.persona5dex.models;

public class PersonaShadowDetail {
    public String shadowName;
    public int isPrimary;

    public PersonaShadowDetail(String shadowName, int isPrimary) {
        this.shadowName = shadowName;
        this.isPrimary = isPrimary;
    }

    public boolean isPrimary() {
        return this.isPrimary == 1;
    }
}

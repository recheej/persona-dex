package com.persona5dex.models;

public class PersonaShadowDetail {
    public String shadowName;
    public int primary;

    public PersonaShadowDetail(String shadowName, int primary) {
        this.shadowName = shadowName;
        this.primary = primary;
    }

    public boolean isPrimary() {
        return this.primary == 1;
    }
}

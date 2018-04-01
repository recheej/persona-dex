package com.persona5dex.models;

public class PersonaShadowDetail {
    public String shadowName;
    public int primary;

    public boolean isPrimary() {
        return this.primary == 1;
    }
}

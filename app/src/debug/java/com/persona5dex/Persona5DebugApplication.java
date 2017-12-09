package com.persona5dex;

import com.facebook.stetho.Stetho;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5DebugApplication extends Persona5Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}

package com.persona5dex;

import android.app.Activity;
import android.app.Application;

import com.persona5dex.dagger.ApplicationContextModule;
import com.persona5dex.dagger.DaggerPersona5ApplicationComponent;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.services.FusionCalculatorJobService;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5Application extends Application {
    private Persona5ApplicationComponent component;

    public static Persona5Application get(Activity activity){
        return (Persona5Application) activity.getApplication();
    }

    public static Persona5Application get(FusionCalculatorJobService service){
        return (Persona5Application) service.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = DaggerPersona5ApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(this))
                .build();
    }

    public Persona5ApplicationComponent getComponent() {
        return this.component;
    }
}

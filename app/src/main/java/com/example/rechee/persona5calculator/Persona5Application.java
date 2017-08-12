package com.example.rechee.persona5calculator;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.rechee.persona5calculator.dagger.ApplicationContextModule;
import com.example.rechee.persona5calculator.dagger.DaggerPersona5ApplicationComponent;
import com.example.rechee.persona5calculator.dagger.Persona5ApplicationComponent;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.services.FusionCalculatorService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5Application extends Application {
    private Persona5ApplicationComponent component;
    private RefWatcher refWatcher;

    public static Persona5Application get(Activity activity){
        return (Persona5Application) activity.getApplication();
    }

    public static Persona5Application get(FusionCalculatorService service){
        return (Persona5Application) service.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }

        refWatcher = LeakCanary.install(this);
        this.component = DaggerPersona5ApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(this))
                .build();
    }

    public Persona5ApplicationComponent getComponent() {
        return this.component;
    }

    public static RefWatcher getRefWatcher(Context context) {
        Persona5Application application = (Persona5Application) context.getApplicationContext();
        return application.refWatcher;
    }
}

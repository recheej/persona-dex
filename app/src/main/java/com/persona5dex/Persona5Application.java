package com.persona5dex;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.JobIntentService;

import com.facebook.stetho.Stetho;
import com.persona5dex.dagger.ApplicationContextModule;
import com.persona5dex.dagger.DaggerPersona5ApplicationComponent;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.room.PersonaDatabase;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5Application extends Application {
    private Persona5ApplicationComponent component;
    private PersonaDatabase database;

    public static Persona5Application get(Activity activity){
        return (Persona5Application) activity.getApplication();
    }

    public static Persona5Application get(JobIntentService service){
        return (Persona5Application) service.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = DaggerPersona5ApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(this))
                .build();

        if(BuildConfig.ENABLE_STETHO){
            Stetho.initializeWithDefaults(this);
        }
    }

    public static PersonaDatabase getPersonaDatabase(Context context) {
        Persona5Application application = (Persona5Application) context.getApplicationContext();
        return application.getPersonaDatabase();
    }

    public PersonaDatabase getPersonaDatabase() {
        return PersonaDatabase.getPersonaDatabase(this);
    }

    public Persona5ApplicationComponent getComponent() {
        return this.component;
    }
}

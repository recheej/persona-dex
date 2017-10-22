package com.persona5dex;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.persona5dex.dagger.ApplicationContextModule;
import com.persona5dex.dagger.DaggerPersona5ApplicationComponent;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.services.FusionCalculatorJobService;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5Application extends Application {
    private Persona5ApplicationComponent component;
    private PersonaDatabase database;

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

    public static PersonaDatabase getPersonaDatabase(Context context) {
        Persona5Application application = (Persona5Application) context.getApplicationContext();
        return application.getPersonaDatabase();
    }

    public PersonaDatabase getPersonaDatabase() {
        if(this.database == null){
            this.database = Room.databaseBuilder(this, PersonaDatabase.class, "persona-db").build();
        }

        return this.database;
    }

    public Persona5ApplicationComponent getComponent() {
        return this.component;
    }
}

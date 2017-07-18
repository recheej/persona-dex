package com.example.rechee.persona5calculator;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.rechee.persona5calculator.dagger.ApplicationContextModule;
import com.example.rechee.persona5calculator.dagger.DaggerPersona5ApplicationComponent;
import com.example.rechee.persona5calculator.dagger.Persona5ApplicationComponent;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.models.PersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaGraph;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5Application extends Application {
    private Persona5ApplicationComponent component;

    public static Persona5Application get(Activity activity){
        return (Persona5Application) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = DaggerPersona5ApplicationComponent.builder()
                .personaFileModule(new PersonaFileModule(this))
                .applicationContextModule(new ApplicationContextModule(this))
                .build();

    }

    public Persona[] getAllPersonas() {
        return this.component.allPersonas();
    }

    public Persona5ApplicationComponent getComponent() {
        return this.component;
    }
}

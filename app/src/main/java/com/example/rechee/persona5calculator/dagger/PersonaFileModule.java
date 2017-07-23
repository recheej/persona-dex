package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawPersona;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class PersonaFileModule {

    private final Context context;

    public PersonaFileModule(Context context){
        this.context = context;
    }

    @Provides
    @Named("personaFileContents")
    String personaFileContents() {
        InputStream stream = context.getResources().openRawResource(R.raw.person_data);
        return PersonaUtilities.getFileContents(stream);
    }
}

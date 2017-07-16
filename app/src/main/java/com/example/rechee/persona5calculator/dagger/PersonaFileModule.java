package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

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
        return this.fileContents(stream);
    }

    @Provides
    @Named("arcanaMapFileContents")
    String arcanaMapFileContents() {
        InputStream stream = context.getResources().openRawResource(R.raw.arcana_combo_data);
        return this.fileContents(stream);
    }

    private String fileContents(InputStream stream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String line;

        String fileContents = "";
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();

            fileContents = out.toString();

        } catch (IOException e) {
            fileContents = "";
        }

        return fileContents;
    }
}

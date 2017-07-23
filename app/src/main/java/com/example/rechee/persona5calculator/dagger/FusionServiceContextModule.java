package com.example.rechee.persona5calculator.dagger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesSharedPrefRepository;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.HashMap;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/3/2017.
 */

@Module
public class FusionServiceContextModule {

    private final Context context;

    public FusionServiceContextModule(Context context){
        this.context = context;
    }

    @Provides
    @FusionServiceScope
    @Named("fusionServiceGson")
    Gson gson() {
        return new Gson();
    }

    @FusionServiceScope
    @Provides
    RawArcanaMap[] rawArcanas(@Named("fusionServiceGson") Gson gson, @Named("arcanaMapFileContents") String arcanaMapFileContents) {
        return gson.fromJson(arcanaMapFileContents, RawArcanaMap[].class);
    }

    @Provides
    @FusionServiceScope
    @Named("arcanaMapFileContents")
    String arcanaMapFileContents() {
        InputStream stream = context.getResources().openRawResource(R.raw.arcana_combo_data);
        return PersonaUtilities.getFileContents(stream);
    }

    @FusionServiceScope
    @Provides
    SharedPreferences sharedPreferences(){
        return context.getSharedPreferences(Persona5Application.getPersonaFusionSharedPrefName(), Context.MODE_PRIVATE);
    }

    @FusionServiceScope
    @Provides
    PersonaEdgesRepository edgesRepository(SharedPreferences sharedPreferences, @Named("fusionServiceGson") Gson gson) {
        return new PersonaEdgesSharedPrefRepository(sharedPreferences, gson);
    }
}

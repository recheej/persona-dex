package com.example.rechee.persona5calculator.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesSharedPrefRepository;
import com.google.gson.Gson;

import java.io.InputStream;

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
    RawArcanaMap[] rawArcanas(@Named("applicationGson") Gson gson, @Named("arcanaMapFileContents") String arcanaMapFileContents) {
        return gson.fromJson(arcanaMapFileContents, RawArcanaMap[].class);
    }

    @Provides
    @FusionServiceScope
    @Named("arcanaMapFileContents")
    String arcanaMapFileContents() {
        InputStream stream = context.getResources().openRawResource(R.raw.arcana_combo_data);
        return PersonaUtilities.getFileContents(stream);
    }

    @Provides
    @FusionServiceScope
    @Named("fusionSharedPreferences")
    SharedPreferences sharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS, Context.MODE_PRIVATE);
    }

    @Provides
    @FusionServiceScope
    PersonaEdgesRepository edgesRepository(@Named("fusionSharedPreferences") SharedPreferences sharedPreferences, @Named("applicationGson") Gson gson) {
        return new PersonaEdgesSharedPrefRepository(sharedPreferences, gson);
    }
}

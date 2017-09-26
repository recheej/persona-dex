package com.example.rechee.persona5calculator.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.PersonaFileUtilities;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesSharedPrefRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

    @FusionServiceScope
    @Provides
    HashMap<Enumerations.Arcana, HashMap<Enumerations.Arcana, Enumerations.Arcana>> arcanaTable(Gson gson) {
        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(gson);
        InputStream stream = context.getResources().openRawResource(R.raw.arcana_combo_data);

        return personaFileUtilities.getArcanaTable(stream);
    }

    @FusionServiceScope
    @Provides
    Map<String, int[]> rarePersonaCombos(Gson gson) {
        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(gson);
        InputStream stream = context.getResources().openRawResource(R.raw.rare_combos);

        return personaFileUtilities.rareCombos(stream);
    }

    @Provides
    @FusionServiceScope
    PersonaRepository provideRepository(@Named("applicationGson") Gson gson) {
        InputStream stream = context.getResources().openRawResource(R.raw.person_data);
        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(gson);

        return new PersonaRepositoryFile(personaFileUtilities.allPersonas(stream));
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

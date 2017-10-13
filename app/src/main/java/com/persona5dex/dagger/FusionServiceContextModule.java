package com.persona5dex.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.PersonaUtilities;
import com.persona5dex.R;
import com.persona5dex.models.Enumerations;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesSharedPrefRepository;
import com.persona5dex.repositories.PersonaRepository;
import com.persona5dex.repositories.PersonaRepositoryFile;
import com.persona5dex.repositories.PersonaTransferRepository;
import com.persona5dex.repositories.PersonaTransferRepositorySharedPref;
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
    PersonaRepository provideRepository(Gson gson) {
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
    @Named("transferSharedPreferences")
    SharedPreferences transferSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_TRANSFER_CONTENT, Context.MODE_PRIVATE);
    }

    @Provides
    @FusionServiceScope
    @Named("dlcSharedPreferences")
    SharedPreferences dlcSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_DLC, Context.MODE_PRIVATE);
    }

    @Provides
    @FusionServiceScope
    @Named("defaultSharedPreferences")
    SharedPreferences defaultSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @FusionServiceScope
    PersonaEdgesRepository edgesRepository(@Named("fusionSharedPreferences") SharedPreferences sharedPreferences, Gson gson) {
        return new PersonaEdgesSharedPrefRepository(sharedPreferences, gson);
    }

    @Provides
    @FusionServiceScope
    PersonaTransferRepository transferRepository(@Named("transferSharedPreferences") SharedPreferences sharedPreferences,
                                                 @Named("dlcSharedPreferences") SharedPreferences dlcSharedPreferences,
                                                 @Named("defaultSharedPreferences") SharedPreferences defaultSharedPreferences,
                                                 Gson gson) {
        return new PersonaTransferRepositorySharedPref(sharedPreferences,
                dlcSharedPreferences,
                defaultSharedPreferences,
                gson,
                context.getString(R.string.pref_key_dlc),
                context.getString(R.string.pref_key_rarePersona));
    }
}
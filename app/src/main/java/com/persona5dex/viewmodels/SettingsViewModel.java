package com.persona5dex.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.room.Persona;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsViewModel extends ViewModel {

    @Inject
    MainPersonaRepository repository;

    private LiveData<List<Persona>> dlcPersonas;

    public SettingsViewModel(MainPersonaRepository repository){
        this.repository = repository;
        this.dlcPersonas = repository.getDLCPersonas();
    }

    public SettingsViewModel() {}

    public void init(Persona5ApplicationComponent component) {
        component
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .inject(this);

        this.dlcPersonas = repository.getDLCPersonas();
    }
    /**
     * Gets dlc per the settings
     * @return 2D string array with two columns: column one is persona name, column 2 is persona id
     */
    public LiveData<String[][]> getDLCPersonaForSettings(){

        //key: persona name, value persona id
        //Map<String, Integer> dlcPersonaMap = transferRepository.getDLCPersonaForSettings();

        return Transformations.map(dlcPersonas, new Function<List<Persona>, String[][]>() {
            @Override
            public String[][] apply(List<Persona> input) {

                if(input == null || input.size() == 0){
                    return new String[2][0];
                }

                Collections.sort(input, new Comparator<Persona>() {
                    @Override
                    public int compare(Persona p1, Persona p2) {
                        return p1.name.compareTo(p2.name);
                    }
                });

                String[][] output = new String[2][input.size()];

                for (int i = 0; i < input.size(); i++) {
                    Persona persona = input.get(i);
                    output[0][i] = persona.name;
                    output[1][i] = String.valueOf(persona.id);
                }

                return output;
            }
        });
    }
}

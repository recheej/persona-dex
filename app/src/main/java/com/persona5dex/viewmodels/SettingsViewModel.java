package com.persona5dex.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsViewModel extends ViewModel {

    private MainPersonaRepository repository;

    private LiveData<List<MainListPersona>> dlcPersonas;

    public SettingsViewModel(MainPersonaRepository repository){
        this.repository = repository;
        this.dlcPersonas = repository.getDLCPersonas();
    }

    /**
     * Gets dlc per the settings
     * @return 2D string array with two columns: column one is persona name, column 2 is persona id
     */
    public LiveData<String[][]> getDLCPersonaForSettings(){

        //key: persona name, value persona id
        //Map<String, Integer> dlcPersonaMap = transferRepository.getDLCPersonaForSettings();

        return Transformations.map(dlcPersonas, new Function<List<MainListPersona>, String[][]>() {
            @Override
            public String[][] apply(List<MainListPersona> input) {

                if(input == null || input.size() == 0){
                    return new String[2][0];
                }

                Collections.sort(input, new Comparator<MainListPersona>() {
                    @Override
                    public int compare(MainListPersona p1, MainListPersona p2) {
                        return p1.name.compareTo(p2.name);
                    }
                });

                String[][] output = new String[2][input.size()];

                for (int i = 0; i < input.size(); i++) {
                    MainListPersona persona = input.get(i);
                    output[0][i] = persona.name;
                    output[1][i] = String.valueOf(persona.id);
                }

                return output;
            }
        });
    }
}

package com.persona5dex.viewmodels;

import com.persona5dex.repositories.PersonaTransferRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Rechee on 10/7/2017.
 */

public class SettingsViewModel {
    private final PersonaTransferRepository transferRepository;

    @Inject
    public SettingsViewModel(PersonaTransferRepository transferRepository){
        this.transferRepository = transferRepository;
    }

    public String[][] getDLCPersonaForSettings(){
        Map<String, Integer> dlcPersonaMap = transferRepository.getDLCPersonaForSettings();

        if(dlcPersonaMap == null){
            return new String[2][0];
        }

        final Set<String> entries = dlcPersonaMap.keySet();
        String[] personaNamesSorted = entries.toArray(new String[entries.size()]);
        Arrays.sort(personaNamesSorted);

        String[][] output = new String[2][personaNamesSorted.length];

        for (int i = 0; i < personaNamesSorted.length; i++) {
            String personaName = personaNamesSorted[i];
            output[0][i] = personaName;
            output[1][i] = dlcPersonaMap.get(personaName).toString();
        }

        return output;
    }
}

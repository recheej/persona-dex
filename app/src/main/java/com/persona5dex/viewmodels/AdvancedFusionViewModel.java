package com.persona5dex.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.PersonaUtilities;
import com.persona5dex.R;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.RawAdvancedFusion;
import com.persona5dex.repositories.MainPersonaRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rechee on 1/7/2018.
 */

public class AdvancedFusionViewModel extends ViewModel {

    private final Application application;
    private final MainPersonaRepository mainPersonaRepository;
    private MutableLiveData<String> personaNameLiveData;

    public AdvancedFusionViewModel(Application application,
                                   MainPersonaRepository mainPersonaRepository){
        this.application = application;
        this.mainPersonaRepository = mainPersonaRepository;
        personaNameLiveData = new MutableLiveData<>();
    }

    public LiveData<List<MainListPersona>> getRecipesForAdvancedPersona(int personaID){
        MutableLiveData<List<MainListPersona>> output = new MutableLiveData<>();

        mainPersonaRepository.getPersonaName(personaID).observeForever(personaName -> {
            personaNameLiveData.setValue(personaName);

            new AdvancedRecipesTask().execute(new TaskParams(personaName, output, application,
                    mainPersonaRepository));
        });


        return output;
    }

    public LiveData<String> getPersonaName() {
        return personaNameLiveData;
    }

    private class TaskParams {
        public String personaName;
        public MutableLiveData<List<MainListPersona>> personas;
        public Context applicationContext;
        private MainPersonaRepository mainPersonaRepository;

        public TaskParams(String personaName, MutableLiveData<List<MainListPersona>> personas,
                          Context applicationContext, MainPersonaRepository mainPersonaRepository) {
            this.personaName = personaName;
            this.personas = personas;
            this.applicationContext = applicationContext;
            this.mainPersonaRepository = mainPersonaRepository;
        }
    }

    private static class AdvancedRecipesTask extends AsyncTask<TaskParams, Void, Void> {

        @Override
        protected Void doInBackground(TaskParams... taskParams) {
            TaskParams taskParam = taskParams[0];
            String personaNameNormalized = PersonaUtilities.normalizePersonaName(taskParam.personaName);

            MutableLiveData<List<MainListPersona>> personas = taskParam.personas;

            PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities();
            InputStream advancedFusionsFile = taskParam.applicationContext.getResources()
                    .openRawResource(R.raw.advanced_fusions);
            RawAdvancedFusion[] rawAdvancedFusions =  personaFileUtilities.parseJsonFile(advancedFusionsFile,
                    RawAdvancedFusion[].class);

            boolean foundPersona = false;
            for (RawAdvancedFusion rawAdvancedFusion : rawAdvancedFusions) {
                if(PersonaUtilities.normalizePersonaName(rawAdvancedFusion.result).equals(personaNameNormalized)){
                    foundPersona = true;

                    taskParam.mainPersonaRepository
                            .getAllPersonasForMainList().observeForever(mainListPersonas -> {
                        /**
                         * We could go to the database and get each specific persona for the sources.
                         * However, there's no guarentee the names in the fusion match match the db.
                         * Let's get all personas, normalize their names, and then find a match to the sources
                         */

                        List<MainListPersona> finalList;
                        if(mainListPersonas == null){
                            finalList = new ArrayList<>();
                            mainListPersonas = new ArrayList<>();
                        }
                        else{
                            finalList = new ArrayList<>(mainListPersonas.size());
                        }

                        Map<String, MainListPersona> mainListPersonaHashMap = new HashMap<>(mainListPersonas.size());
                        for (MainListPersona mainListPersona : mainListPersonas) {
                            mainListPersonaHashMap.put(PersonaUtilities.normalizePersonaName(mainListPersona.name),
                                    mainListPersona);
                        }


                        for (String source : rawAdvancedFusion.sources) {
                            final String normalizedName = PersonaUtilities.normalizePersonaName(source);
                            if(mainListPersonaHashMap.containsKey(normalizedName)){
                                finalList.add(mainListPersonaHashMap.get(normalizedName));
                            }
                        }

                        personas.postValue(finalList);
                    });
                    break;
                }
            }

            if(!foundPersona){
                throw new RuntimeException("could not find advanced persona:" + taskParam.personaName);
            }

            return null;
        }
    }

}

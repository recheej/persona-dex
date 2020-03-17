package com.persona5dex.viewmodels;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rechee on 1/7/2018.
 */

public class AdvancedFusionViewModel extends ViewModel {

    private final Application application;
    private final MainPersonaRepository mainPersonaRepository;
    private final PersonaFileUtilities personaFileUtilities;
    private MutableLiveData<String> personaNameLiveData;

    public AdvancedFusionViewModel(Application application,
                                   MainPersonaRepository mainPersonaRepository,
                                   PersonaFileUtilities personaFileUtilities){
        this.application = application;
        this.mainPersonaRepository = mainPersonaRepository;
        personaNameLiveData = new MutableLiveData<>();
        this.personaFileUtilities = personaFileUtilities;
    }

    public LiveData<List<MainListPersona>> getRecipesForAdvancedPersona(int personaID){
        MutableLiveData<List<MainListPersona>> output = new MutableLiveData<>();

        mainPersonaRepository.getPersonaName(personaID).observeForever(personaName -> {
            personaNameLiveData.setValue(personaName);

            new AdvancedRecipesTask().execute(new TaskParams(personaName, output, application,
                    mainPersonaRepository, personaFileUtilities));
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
        private final PersonaFileUtilities personaFileUtilities;
        private RawAdvancedFusion rawAdvancedFusion;

        public TaskParams(String personaName, MutableLiveData<List<MainListPersona>> personas,
                          Context applicationContext, MainPersonaRepository mainPersonaRepository,
                          PersonaFileUtilities personaFileUtilities) {
            this.personaName = personaName;
            this.personas = personas;
            this.applicationContext = applicationContext;
            this.mainPersonaRepository = mainPersonaRepository;
            this.personaFileUtilities = personaFileUtilities;
        }
    }

    private static class AdvancedRecipesTask extends AsyncTask<TaskParams, Void, TaskParams> {

        @Override
        protected TaskParams doInBackground(TaskParams... taskParams) {
            TaskParams taskParam = taskParams[0];
            String personaNameNormalized = PersonaUtilities.normalizePersonaName(taskParam.personaName);

            InputStream advancedFusionsFile = taskParam.applicationContext.getResources()
                    .openRawResource(R.raw.advanced_fusions);
            RawAdvancedFusion[] rawAdvancedFusions =  taskParam.personaFileUtilities.parseJsonFile(advancedFusionsFile,
                    RawAdvancedFusion[].class);

            for (RawAdvancedFusion rawAdvancedFusion : rawAdvancedFusions) {
                if(PersonaUtilities.normalizePersonaName(rawAdvancedFusion.result).equals(personaNameNormalized)){
                    taskParam.rawAdvancedFusion = rawAdvancedFusion;
                    return taskParam;
                }
            }

            throw new IllegalStateException("could not find advanced persona:" + taskParam.personaName);
        }

        @Override
        protected void onPostExecute(TaskParams taskParam) {
            super.onPostExecute(taskParam);

            taskParam.mainPersonaRepository
                    .getAllPersonasForMainListLiveData().observeForever(mainListPersonas -> {
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
                    mainListPersonaHashMap.put(PersonaUtilities.normalizePersonaName(mainListPersona.getName()),
                            mainListPersona);
                }

                for (String source : taskParam.rawAdvancedFusion.sources) {
                    final String normalizedName = PersonaUtilities.normalizePersonaName(source);
                    if(mainListPersonaHashMap.containsKey(normalizedName)){
                        finalList.add(mainListPersonaHashMap.get(normalizedName));
                    }
                }

                Collections.sort(finalList, (p1, p2) -> p1.getName().compareTo(p2.getName()));
                taskParam.personas.postValue(finalList);
            });

        }
    }

}

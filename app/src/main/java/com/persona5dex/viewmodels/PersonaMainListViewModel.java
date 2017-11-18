package com.persona5dex.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.persona5dex.Persona5Application;
import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.repositories.MainPersonaRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaMainListViewModel extends AndroidViewModel {
    private LiveData<List<MainListPersona>> mainListPersonas;

    @Inject
    MainPersonaRepository repository;

    public PersonaMainListViewModel(@NonNull Application application) {
        super(application);

        Persona5Application persona5Application = (Persona5Application) application;

        persona5Application.getComponent()
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);
    }

    public LiveData<List<MainListPersona>> getMainListPersonas(){
        if(mainListPersonas != null){
            return mainListPersonas;
        }

        mainListPersonas = repository.getPersonasForMainList();

        return Transformations.map(mainListPersonas, new Function<List<MainListPersona>, List<MainListPersona>>() {
            @Override
            public List<MainListPersona> apply(List<MainListPersona> input) {
                Collections.sort(input, new Comparator<MainListPersona>() {
                    @Override
                    public int compare(MainListPersona p1, MainListPersona p2) {
                        return p1.name.compareTo(p2.name);
                    }
                });

                return input;
            }
        });
    }
}

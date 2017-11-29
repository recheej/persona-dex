package com.persona5dex.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.repositories.PersonaDetailRepository;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailInfoViewModel extends ViewModel {

    @Inject
    PersonaDetailRepository repository;

    private LiveData<PersonaDetailInfo> detailInfo;
    private int personaID;

    public PersonaDetailInfoViewModel(PersonaDetailRepository repository){
        this.repository = repository;
    }

    public PersonaDetailInfoViewModel() {}

    public void init(Persona5ApplicationComponent component, int personaID) {
        component
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);

        this.personaID = personaID;

        if(detailInfo == null){
            detailInfo = repository.getDetailsForPersona(personaID);
        }
    }

    public LiveData<PersonaDetailInfo> getDetailsForPersona(){
        return detailInfo;
    }
}

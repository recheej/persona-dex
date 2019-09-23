package com.persona5dex.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaShadowDetail;
import com.persona5dex.repositories.PersonaDetailRepository;

import java.util.Arrays;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailInfoViewModel extends ViewModel {

    @Inject
    PersonaDetailRepository repository;

    private LiveData<PersonaDetailInfo> detailInfo;
    private LiveData<PersonaShadowDetail[]> personaShadows;

    public PersonaDetailInfoViewModel() {}

    public PersonaDetailInfoViewModel(PersonaDetailRepository repository, int personaID){
        this.repository = repository;
        initDetailInfo(personaID);
    }

    public void init(Persona5ApplicationComponent component, int personaID) {
        component
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .inject(this);
        initDetailInfo(personaID);
    }

    private void initDetailInfo(int personaID) {
        if(detailInfo == null){
            detailInfo = repository.getDetailsForPersona(personaID);
        }

        personaShadows = Transformations.switchMap(detailInfo, input -> repository.getShadowsForPersona(input.id));
    }

    public LiveData<PersonaDetailInfo> getDetailsForPersona(){
        return detailInfo;
    }

    public LiveData<String> getShadowsForPersona() {
        return Transformations.map(personaShadows, shadows -> {
            if(shadows == null || shadows.length == 0){
                return null;
            }

            Arrays.sort(shadows, (s1, s2) -> {
                if(s1.isPrimary() && !s2.isPrimary()){
                    return -1;
                }

                if(s2.isPrimary() && !s1.isPrimary()){
                    return 1;
                }

                return s1.shadowName.compareTo(s2.shadowName);
            });

            StringBuilder stringBuilder = new StringBuilder();

            for (PersonaShadowDetail shadow : shadows) {
                stringBuilder.append(shadow.shadowName);
                stringBuilder.append("\n");
            }

            //remove last new line
            stringBuilder.setLength(stringBuilder.length() - 1);

            return stringBuilder.toString();
        });
    }
}

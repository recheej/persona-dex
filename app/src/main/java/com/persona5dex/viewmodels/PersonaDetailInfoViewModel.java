package com.persona5dex.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaShadowDetail;
import com.persona5dex.repositories.PersonaDetailRepository;

import java.util.Arrays;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailInfoViewModel extends ViewModel {

    private final PersonaDetailRepository repository;
    private final ArcanaNameProvider arcanaNameProvider;

    private LiveData<PersonaDetailInfo> detailInfo;
    private LiveData<PersonaShadowDetail[]> personaShadows;

    public PersonaDetailInfoViewModel(PersonaDetailRepository repository, ArcanaNameProvider arcanaNameProvider, int personaID) {
        this.repository = repository;
        this.arcanaNameProvider = arcanaNameProvider;

        detailInfo = Transformations.map(this.repository.getDetailsForPersona(personaID), input -> {
            input.arcanaName = this.arcanaNameProvider.getArcanaNameForDisplay(input.arcana);
            if(input.imageUrl != null) {
                if(!input.imageUrl.contains("https")) {
                    input.imageUrl = input.imageUrl.replace("http", "https");
                }
            }
            return input;
        });

        personaShadows = Transformations.switchMap(detailInfo, input -> this.repository.getShadowsForPersona(input.id));
    }

    public LiveData<PersonaDetailInfo> getDetailsForPersona() {
        return detailInfo;
    }

    public LiveData<String> getShadowsForPersona() {
        return Transformations.map(personaShadows, shadows -> {
            if(shadows == null || shadows.length == 0) {
                return null;
            }

            Arrays.sort(shadows, (s1, s2) -> {
                if(s1.isPrimary() && !s2.isPrimary()) {
                    return -1;
                }

                if(s2.isPrimary() && !s1.isPrimary()) {
                    return 1;
                }

                return s1.shadowName.compareTo(s2.shadowName);
            });

            StringBuilder stringBuilder = new StringBuilder();

            for(PersonaShadowDetail shadow : shadows) {
                stringBuilder.append(shadow.shadowName);
                stringBuilder.append("\n");
            }

            //remove last new line
            stringBuilder.setLength(stringBuilder.length() - 1);

            return stringBuilder.toString();
        });
    }
}

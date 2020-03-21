package com.persona5dex.dagger.activity;

import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaTransferRepository;
import com.persona5dex.viewmodels.PersonaFusionListViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/3/2017.
 */

@Module
public class ViewModelModule {

    @Provides
    @ActivityScope
    PersonaFusionListViewModel personaFusionListViewModel(PersonaEdgesRepository repository, PersonaTransferRepository transferRepository) {
        return new PersonaFusionListViewModel(repository, transferRepository);
    }
}

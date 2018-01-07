package com.persona5dex.dagger.activity;

import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;
import com.persona5dex.repositories.PersonaTransferRepository;
import com.persona5dex.viewmodels.PersonaDetailViewModel;
import com.persona5dex.viewmodels.PersonaFilterViewModel;
import com.persona5dex.viewmodels.PersonaFusionListViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/3/2017.
 */

@Module
public class ViewModelModule {
    @Provides
    @ActivityScope
    PersonaDetailViewModel personaDetailViewModel(PersonaTransferRepository repository) {
        return new PersonaDetailViewModel(repository);
    }

    @Provides
    @ActivityScope
    PersonaFusionListViewModel personaFusionListViewModel(PersonaEdgesRepository repository, PersonaTransferRepository transferRepository) {
        return new PersonaFusionListViewModel(repository, transferRepository);
    }

//    @Provides
//    @ActivityScope
//    ViewModelFactory viewModelFactory(Lazy<PersonaSkillsRepository> skillsRepositoryLazy,
//                                      Lazy<MainPersonaRepository> mainPersonaRepositoryLazy,
//                                      Lazy<PersonaDisplayEdgesRepository> personaDisplayEdgesRepositoryLazy) {
//        return new ViewModelFactory(skillsRepositoryLazy, mainPersonaRepositoryLazy,
//                personaDisplayEdgesRepositoryLazy);
//    }

    @Provides
    @ActivityScope
    PersonaFilterViewModel personaFilterViewModel() {
        return new PersonaFilterViewModel();
    }
}

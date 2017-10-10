package com.persona5dex.dagger;

import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaRepository;
import com.persona5dex.repositories.PersonaTransferRepository;
import com.persona5dex.viewmodels.PersonaDetailViewModel;
import com.persona5dex.viewmodels.PersonaFilterViewModel;
import com.persona5dex.viewmodels.PersonaFusionListViewModel;
import com.persona5dex.viewmodels.PersonaListViewModel;
import com.persona5dex.viewmodels.PersonaSkillsViewModel;

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
    PersonaListViewModel personaListViewModel(PersonaRepository personaRepository, PersonaTransferRepository personaTransferRepository) {
        return new PersonaListViewModel(personaRepository, personaTransferRepository);
    }

    @Provides
    @ActivityScope
    PersonaFusionListViewModel personaFusionListViewModel(PersonaEdgesRepository repository, PersonaTransferRepository transferRepository, PersonaListViewModel personaListViewModel) {
        return new PersonaFusionListViewModel(repository, transferRepository, personaListViewModel);
    }

    @Provides
    @ActivityScope
    PersonaSkillsViewModel personaSkillsViewModel(PersonaTransferRepository transferRepository) {
        return new PersonaSkillsViewModel(transferRepository);
    }

    @Provides
    @ActivityScope
    PersonaFilterViewModel personaFilterViewModel() {
        return new PersonaFilterViewModel();
    }
}

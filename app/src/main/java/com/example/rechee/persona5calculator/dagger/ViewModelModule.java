package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;
import com.example.rechee.persona5calculator.viewmodels.PersonaFilterViewModel;
import com.example.rechee.persona5calculator.viewmodels.PersonaFusionListViewModel;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;
import com.example.rechee.persona5calculator.viewmodels.PersonaSkillsViewModel;

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

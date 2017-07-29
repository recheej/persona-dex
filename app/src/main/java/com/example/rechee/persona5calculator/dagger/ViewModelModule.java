package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;

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
}

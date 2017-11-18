package com.persona5dex.dagger.AndroidViewModels;

import com.persona5dex.dagger.FusionServiceScope;
import com.persona5dex.dagger.ViewModelRepositoryModule;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

import dagger.Subcomponent;

/**
 * Created by Rechee on 11/18/2017.
 */

@ViewModelScope
@Subcomponent(modules = {
        AndroidViewModelRepositoryModule.class
}
)
public interface ViewModelComponent {
    void inject(PersonaMainListViewModel viewModel);
}
package com.persona5dex.dagger.AndroidViewModels;

import com.persona5dex.dagger.FusionServiceScope;
import com.persona5dex.dagger.ViewModelRepositoryModule;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModel;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;
import com.persona5dex.viewmodels.PersonaElementsViewModel;
import com.persona5dex.viewmodels.PersonaFusionViewModel;
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
    void inject(PersonaDetailInfoViewModel viewModel);
    void inject(PersonaElementsViewModel viewModel);
    void inject(PersonaDetailSkillsViewModel viewModel);
    void inject(PersonaFusionViewModel viewModel);
}
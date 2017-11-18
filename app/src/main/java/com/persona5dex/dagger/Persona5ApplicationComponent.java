package com.persona5dex.dagger;

import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.AndroidViewModels.ViewModelComponent;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@ApplicationScope
@Component(
    modules = {
            ApplicationContextModule.class,
    }
)
public interface Persona5ApplicationComponent {
    ActivityComponent plus(LayoutModule layoutModule,
                           ActivityContextModule activityContextModule,
                           ViewModelModule viewModelModule,
                           ViewModelRepositoryModule viewModelRepositoryModule);

    FusionCalculatorServiceComponent plus(FusionServiceContextModule fusionServiceContextModule, FusionArcanaDataModule fusionArcanaDataModule);
    ViewModelComponent plus(AndroidViewModelRepositoryModule androidViewModelRepositoryModule);
}

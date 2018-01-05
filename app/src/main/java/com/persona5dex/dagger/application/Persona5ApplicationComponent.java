package com.persona5dex.dagger.application;

import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.ViewModelComponent;
import com.persona5dex.dagger.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.fusionService.FusionArcanaDataModule;
import com.persona5dex.dagger.fusionService.FusionCalculatorServiceComponent;
import com.persona5dex.dagger.fusionService.FusionServiceContextModule;

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
    FusionCalculatorServiceComponent plus(FusionServiceContextModule fusionServiceContextModule, FusionArcanaDataModule fusionArcanaDataModule);
    ViewModelComponent viewModelComponent(AndroidViewModelRepositoryModule androidViewModelRepositoryModule);
}

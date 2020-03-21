package com.persona5dex.dagger.application;

import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.ViewModelComponent;
import com.persona5dex.dagger.fusionService.FusionArcanaDataModule;
import com.persona5dex.dagger.fusionService.FusionCalculatorServiceComponent;
import com.persona5dex.dagger.fusionService.FusionServiceContextModule;
import com.persona5dex.fusionService.GenerateFusionWorker;

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
    void inject(GenerateFusionWorker generateFusionWorker);
}

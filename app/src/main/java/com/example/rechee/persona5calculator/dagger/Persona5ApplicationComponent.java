package com.example.rechee.persona5calculator.dagger;

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
}

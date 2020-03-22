package com.persona5dex.dagger.application;

import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.fusionService.GenerateFusionWorker;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@ApplicationScope
@Component(
    modules = {
            ApplicationContextModule.class,
            AndroidViewModelRepositoryModule.class
    }
)
public interface Persona5ApplicationComponent {
    void inject(GenerateFusionWorker generateFusionWorker);
    ActivityComponent activityComponent(LayoutModule layoutModule,
                                        ActivityContextModule activityContextModule
    );
}

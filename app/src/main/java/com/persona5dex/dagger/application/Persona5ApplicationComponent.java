package com.persona5dex.dagger.application;

import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.fusionService.GenerateFusionWorker;
import com.persona5dex.jobs.PersonaJobCreator;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@Singleton
@Component(
        modules = {
                ApplicationContextModule.class,
                AndroidViewModelRepositoryModule.class
        }
)
public interface Persona5ApplicationComponent {
    void inject(GenerateFusionWorker generateFusionWorker);

    PersonaJobCreator personaJobCreator();
    ActivityComponent.Builder activityComponent();
}

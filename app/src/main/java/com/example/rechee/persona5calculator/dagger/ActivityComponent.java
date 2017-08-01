package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.activities.MainActivity;
import com.example.rechee.persona5calculator.activities.PersonaDetailActivity;
import com.example.rechee.persona5calculator.activities.PersonaFusionActivity;

import dagger.Subcomponent;

/**
 * Created by Rechee on 7/1/2017.
 */

@ActivityScope
@Subcomponent(
        modules = {
                LayoutModule.class,
                ActivityContextModule.class,
                ViewModelModule.class,
                ViewModelRepositoryModule.class,
                PersonaFileModule.class
        }
)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(PersonaDetailActivity activity);
    void inject(PersonaFusionActivity activity);
    FragmentComponent plus();
}

package com.persona5dex.dagger.activity;

import com.persona5dex.activities.BaseActivity;
import com.persona5dex.activities.MainActivity;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.activities.PersonaFusionActivity;
import com.persona5dex.activities.SettingsActivity;
import com.persona5dex.activities.SkillDetailActivity;
import com.persona5dex.dagger.fragment.FragmentComponent;
import com.persona5dex.fragments.PersonaSkillsFragment;

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
                ViewModelRepositoryModule.class
        }
)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(PersonaDetailActivity activity);
    void inject(PersonaFusionActivity activity);
    void inject(SettingsActivity activity);
    void inject(PersonaSkillsFragment fragment);
    void inject(SkillDetailActivity activity);
    void inject(BaseActivity activity);

    FragmentComponent plus();
}

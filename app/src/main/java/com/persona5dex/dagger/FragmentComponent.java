package com.persona5dex.dagger;

import com.persona5dex.activities.MainActivity;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.dagger.FragmentScope;
import com.persona5dex.fragments.FilterDialogFragment;
import com.persona5dex.fragments.FusionListFragment;
import com.persona5dex.fragments.PersonaDetailInfoFragment;
import com.persona5dex.fragments.PersonaElementsFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;

import dagger.Subcomponent;

/**
 * Created by Rechee on 7/1/2017.
 */

@FragmentScope
@Subcomponent(
        modules = {
        }
)
public interface FragmentComponent {
    void inject(PersonaDetailInfoFragment fragment);
    void inject(FusionListFragment fragment);
    void inject(PersonaElementsFragment fragment);
    void inject(PersonaSkillsFragment fragment);
    void inject(FilterDialogFragment fragment);

}

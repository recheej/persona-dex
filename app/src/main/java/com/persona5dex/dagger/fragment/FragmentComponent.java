package com.persona5dex.dagger.fragment;

import com.persona5dex.fragments.AdvancedPersonaFragment;
import com.persona5dex.fragments.FilterDialogFragment;
import com.persona5dex.fragments.FusionListFragment;
import com.persona5dex.fragments.PersonaDetailInfoFragment;
import com.persona5dex.fragments.PersonaElementsFragment;
import com.persona5dex.fragments.PersonaListFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.fragments.SettingsFragment;
import com.persona5dex.onboarding.OnboardingChooseGameFragment;
import com.persona5dex.onboarding.OnboardingThemeChooserFragment;

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
    void inject(PersonaListFragment fragment);
    void inject(AdvancedPersonaFragment advancedPersonaFragment);
    void inject(SettingsFragment fragment);
    void inject(OnboardingChooseGameFragment onboardingChooseGameFragment);
    void inject(OnboardingThemeChooserFragment onboardingThemeChooserFragment);
}

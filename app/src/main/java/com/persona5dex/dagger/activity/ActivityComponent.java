package com.persona5dex.dagger.activity;

import android.content.Context;

import androidx.annotation.Nullable;

import com.persona5dex.activities.BaseActivity;
import com.persona5dex.activities.MainActivity;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.activities.PersonaFusionActivity;
import com.persona5dex.activities.SettingsActivity;
import com.persona5dex.activities.SkillDetailActivity;
import com.persona5dex.fragments.AdvancedPersonaFragment;
import com.persona5dex.fragments.FilterDialogFragment;
import com.persona5dex.fragments.FusionListFragment;
import com.persona5dex.fragments.PersonaDetailInfoFragment;
import com.persona5dex.fragments.PersonaElementsFragment;
import com.persona5dex.fragments.PersonaListFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.fragments.SettingsFragment;
import com.persona5dex.onboarding.OnboardingActivity;
import com.persona5dex.onboarding.OnboardingChooseGameFragment;
import com.persona5dex.onboarding.OnboardingPrivacyFragment;
import com.persona5dex.onboarding.OnboardingThemeChooserFragment;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Subcomponent;

/**
 * Created by Rechee on 7/1/2017.
 */

@ActivityScope
@Subcomponent(
        modules = {
                LayoutModule.class,
                ActivityContextModule.class
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
    void inject(OnboardingActivity activity);

    void inject(PersonaDetailInfoFragment fragment);
    void inject(FusionListFragment fragment);
    void inject(PersonaElementsFragment fragment);
    void inject(FilterDialogFragment fragment);
    void inject(PersonaListFragment fragment);
    void inject(AdvancedPersonaFragment advancedPersonaFragment);
    void inject(SettingsFragment fragment);
    void inject(OnboardingChooseGameFragment onboardingChooseGameFragment);
    void inject(OnboardingThemeChooserFragment onboardingThemeChooserFragment);
    void inject(OnboardingPrivacyFragment onboardingPrivacyFragment);

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        Builder skillId(@Nullable @Named("skillId") Integer skillId);
        @BindsInstance
        Builder activityContext(@Named("activityContext") Context context);
        ActivityComponent build();
        @BindsInstance
        Builder advancedPersonaId(@Nullable @Named("advancedPersonaId") Integer personaID);
    }
}

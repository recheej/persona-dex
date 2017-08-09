package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.activities.MainActivity;
import com.example.rechee.persona5calculator.activities.PersonaDetailActivity;
import com.example.rechee.persona5calculator.fragments.FusionListFragment;
import com.example.rechee.persona5calculator.fragments.PersonaDetailInfoFragment;
import com.example.rechee.persona5calculator.fragments.PersonaElementsFragment;
import com.example.rechee.persona5calculator.fragments.PersonaSkillsFragment;

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
}

package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.activities.MainActivity;
import com.example.rechee.persona5calculator.activities.PersonaDetailActivity;
import com.example.rechee.persona5calculator.fragments.PersonaDetailInfoFragment;

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
}

package com.persona5dex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.PersonaFusionActivity;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;

import javax.inject.Inject;

/**
 * Created by reche on 1/6/2018.
 */

public class AdvancedPersonaFragment extends BaseFragment {

    private int personaID;

    public static AdvancedPersonaFragment newInstance(int personaID){
        AdvancedPersonaFragment advancedPersonaFragment = new AdvancedPersonaFragment();
        Bundle args = new Bundle();
        args.putInt("persona_id", personaID);
        advancedPersonaFragment.setArguments(args);
        return advancedPersonaFragment;
    }

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_advanced_persona, container, false);
        return baseView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Persona5Application.get(activity).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(new LayoutModule(activity),
                        new ActivityContextModule(activity),
                        new ViewModelModule(),
                        new ViewModelRepositoryModule())
                .inject((PersonaFusionActivity) activity);
    }

}

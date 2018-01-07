package com.persona5dex.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.PersonaFusionActivity;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.viewmodels.AdvancedFusionViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by reche on 1/6/2018.
 */

public class AdvancedPersonaFragment extends BaseFragment {

    public static final String PERSONA_ID = "persona_id";
    private int personaID;
    private TextView fusionPromptTextView;

    public static AdvancedPersonaFragment newInstance(int personaID){
        AdvancedPersonaFragment advancedPersonaFragment = new AdvancedPersonaFragment();
        Bundle args = new Bundle();
        args.putInt(PERSONA_ID, personaID);
        advancedPersonaFragment.setArguments(args);
        return advancedPersonaFragment;
    }

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            personaID = getArguments().getInt(PERSONA_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_advanced_persona, container, false);
        
        this.fusionPromptTextView = baseView.findViewById(R.id.textView_fusionPrompt);
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
                .plus().inject(this);

        AdvancedFusionViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AdvancedFusionViewModel.class);

        viewModel.getRecipesForAdvancedPersona(personaID).observe(this, personas -> {

        });

        viewModel.getPersonaName().observe(this, personaName -> {
            fusionPromptTextView.setText(getString(R.string.advanced_fusion_prompt, personaName));
        });
    }
}

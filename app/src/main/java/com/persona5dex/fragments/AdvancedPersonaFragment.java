package com.persona5dex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.fusionService.AdvancedPersonaFusionsFileService;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.viewmodels.AdvancedFusionViewModelFactory;
import com.persona5dex.viewmodels.AdvancedFusionViewModelV2;

import javax.inject.Inject;

/**
 * Created by reche on 1/6/2018.
 */

public class AdvancedPersonaFragment extends BaseFragment {

    private static final String PERSONA_ID = "persona_id";
    private int personaID;
    private TextView fusionPromptTextView;
    private AdvancedFusionViewModelV2 viewModel;
    private PersonaListFragment personaListFragment;
    private ProgressBar progressBar;

    public static AdvancedPersonaFragment newInstance(int personaID) {
        AdvancedPersonaFragment advancedPersonaFragment = new AdvancedPersonaFragment();
        Bundle args = new Bundle();
        args.putInt(PERSONA_ID, personaID);
        advancedPersonaFragment.setArguments(args);
        return advancedPersonaFragment;
    }

    @Inject
    MainPersonaRepository mainPersonaRepository;

    @Inject
    AdvancedPersonaFusionsFileService advancedPersonaFusionsFileService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            personaID = getArguments().getInt(PERSONA_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_advanced_persona, container, false);

        this.fusionPromptTextView = baseView.findViewById(R.id.textView_fusionPrompt);
        progressBar = baseView.findViewById(R.id.progress_bar_fusions);
        progressBar.setVisibility(View.VISIBLE);
        return baseView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Persona5Application.get(activity).getComponent()
                .activityComponent(new LayoutModule(activity),
                        new ActivityContextModule(activity)
                )
                .plus().inject(this);

        final AdvancedFusionViewModelFactory factory = new AdvancedFusionViewModelFactory(personaID, mainPersonaRepository, advancedPersonaFusionsFileService);
        viewModel = new ViewModelProvider(this, factory).get(AdvancedFusionViewModelV2.class);

        viewModel.getPersonaName().observe(getViewLifecycleOwner(), personaName -> {
            fusionPromptTextView.setText(getString(R.string.advanced_fusion_prompt, personaName));
        });

        viewModel.getRecipesForAdvancedPersona().observe(getViewLifecycleOwner(), personas -> {
            personaListFragment.setPersonas(personas);
            progressBar.setVisibility(View.GONE);
        });

        personaListFragment = PersonaListFragment.newInstance(false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, personaListFragment).commit();
    }
}

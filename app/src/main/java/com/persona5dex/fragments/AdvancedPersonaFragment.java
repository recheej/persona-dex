package com.persona5dex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.persona5dex.R;
import com.persona5dex.fusionService.advanced.AdvancedPersonaService;
import com.persona5dex.models.GameType;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.viewmodels.AdvancedFusionViewModel;
import com.persona5dex.viewmodels.AdvancedFusionViewModelFactory;

import javax.inject.Inject;

/**
 * Created by reche on 1/6/2018.
 */

public class AdvancedPersonaFragment extends BaseFragment {

    private static final String PERSONA_ID = "persona_id";
    private int personaID;
    private TextView fusionPromptTextView;
    private AdvancedFusionViewModel viewModel;
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
    AdvancedPersonaService advancedPersonaService;

    @Inject
    GameType gameType;

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

        getActivityComponent().inject(this);

        final AdvancedFusionViewModelFactory factory = new AdvancedFusionViewModelFactory(
                personaID,
                mainPersonaRepository,
                advancedPersonaService,
                gameType
        );
        viewModel = new ViewModelProvider(this, factory).get(AdvancedFusionViewModel.class);

        viewModel.getPersonaName().observe(getViewLifecycleOwner(), personaName -> {
            fusionPromptTextView.setText(getString(R.string.advanced_fusion_prompt, personaName));
        });

//        viewModel.getRecipesForAdvancedPersona().observe(getViewLifecycleOwner(), personas -> {
//            personaListFragment.setPersonas(personas);
//            progressBar.setVisibility(View.GONE);
//        });

//        personaListFragment = PersonaListFragment.newInstance(false, );
//
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, personaListFragment).commit();
    }
}

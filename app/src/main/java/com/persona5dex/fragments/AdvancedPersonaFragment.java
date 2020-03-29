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

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.fusionService.advanced.AdvancedPersonaService;
import com.persona5dex.models.GameType;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaListRepositoryFactory;
import com.persona5dex.viewmodels.AdvancedFusionViewModel;
import com.persona5dex.viewmodels.AdvancedFusionViewModelFactory;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

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

    @Inject
    PersonaListRepositoryFactory personaListRepositoryFactory;

    @Inject
    ArcanaNameProvider arcanaNameProvider;

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

        Persona5Application.get(requireActivity()).getComponent().activityComponent()
                .activityContext(requireActivity())
                .advancedPersonaId(personaID).build().inject(this);

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

        final PersonaMainListViewModel personaMainListViewModel = new ViewModelProvider(requireActivity()).get(PersonaMainListViewModel.class);
        personaMainListViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if(state == PersonaMainListViewModel.State.LoadingCompleted.INSTANCE) {
                progressBar.setVisibility(View.GONE);
            }
        });

        personaListFragment = PersonaListFragment.newInstance(false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, personaListFragment).commit();
    }
}

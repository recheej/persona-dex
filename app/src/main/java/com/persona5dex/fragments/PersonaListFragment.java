package com.persona5dex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaListAdapter;
import com.persona5dex.models.GameType;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by Rechee on 1/6/2018.
 */

public class PersonaListFragment extends BaseFragment {

    public static final String INDEX_BAR_VISIBLE = "index_bar_visible";
    private static final String REPO_TYPE = "repoType";
    //https://github.com/myinnos/AlphabetIndex-Fast-Scroll-RecyclerView
    private IndexFastScrollRecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<MainListPersona> personas;
    private PersonaListAdapter personaListAdapter;
    private PersonaMainListViewModel viewModel;

    @Inject
    ArcanaNameProvider arcanaNameProvider;

    @Inject
    GameType gameType;

    private LinearLayoutManager layoutManager;
    private boolean showIndexBar;

    public static PersonaListFragment newInstance(boolean indexBarVisible) {
        PersonaListFragment listFragment = new PersonaListFragment();
        Bundle args = new Bundle();
        args.putBoolean(INDEX_BAR_VISIBLE, indexBarVisible);
        listFragment.setArguments(args);
        return listFragment;
    }

    public PersonaListFragment() {
        this.personas = new ArrayList<>(250);
        this.showIndexBar = true;
    }

    public void setIndexBarVisible(boolean isVisible) {
        this.showIndexBar = isVisible;
        this.recyclerView.setIndexBarVisibility(isVisible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personas = new ArrayList<>(350);

        this.showIndexBar = true;

        Bundle args = getArguments();
        if(args != null) {
            this.showIndexBar = args.getBoolean(INDEX_BAR_VISIBLE, true);
            final int repoTypeInt = args.getInt(REPO_TYPE, PersonaListRepositoryType.PERSONA.getValue());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_persona_list, container, false);

        progressBar = baseView.findViewById(R.id.progress_bar);

        recyclerView = baseView.findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setIndexBarVisibility(showIndexBar);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return baseView;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void filterPersonas(@NonNull GameType gameType) {
        showProgressBar();
        viewModel.filterPersonas(gameType);
    }

    public void sortPersonasByName(boolean asc) {
        viewModel.sortPersonasByName(asc);

        personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.PersonaName);
        personaListAdapter.notifyDataSetChanged();

        recyclerView.setIndexBarVisibility(showIndexBar);
    }

    public void sortPersonasByLevel(boolean asc) {
        viewModel.sortPersonasByLevel(asc);

        recyclerView.setIndexBarVisibility(false);

        personaListAdapter.notifyDataSetChanged();
    }

    public void sortPersonasByArcana(boolean asc) {
        viewModel.sortPersonasByArcana(asc);

        personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.ArcanaName);
        personaListAdapter.notifyDataSetChanged();

        recyclerView.setIndexBarVisibility(showIndexBar);
    }

//    private FusionListRepositoryComponent getRepositoryComponent(FragmentComponent component) {
//        if(repositoryType == PersonaListRepositoryType.PERSONA) {
//            return component.
//        } else {
//            return component.personaSkillsRepositoryComponent();
//        }
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivityComponent().inject(this);

        personaListAdapter = new PersonaListAdapter(personas, arcanaNameProvider);
        recyclerView.setAdapter(personaListAdapter);

        showProgressBar();
        viewModel = new ViewModelProvider(requireActivity()).get(PersonaMainListViewModel.class);

        viewModel.getFilteredPersonas().observe(getViewLifecycleOwner(), personas -> {
            this.personas.clear();

            if(personas != null) {
                this.personas.addAll(personas);
            }

            personaListAdapter.notifyDataSetChanged();

            hideProgressBar();
        });
    }
}

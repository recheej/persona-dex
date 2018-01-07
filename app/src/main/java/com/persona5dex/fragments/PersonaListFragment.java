package com.persona5dex.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaListAdapter;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.viewmodels.PersonaListViewModelFactory;
import com.persona5dex.viewmodels.PersonaMainListViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by Rechee on 1/6/2018.
 */

public class PersonaListFragment extends BaseFragment {

    //https://github.com/myinnos/AlphabetIndex-Fast-Scroll-RecyclerView
    private IndexFastScrollRecyclerView recyclerView;
    private List<MainListPersona> personas;
    private PersonaListAdapter personaListAdapter;
    private PersonaMainListViewModel viewModel;

    @Inject
    ViewModelFactory viewModelFactory;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personas = new ArrayList<>(250);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_persona_list, container, false);

        recyclerView = baseView.findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        personaListAdapter = new PersonaListAdapter(personas);
        recyclerView.setAdapter(personaListAdapter);

        return baseView;
    }

    public void filterPersonas(String personaName){
        viewModel.filterPersonas(personaName);
    }

    public void sortPersonasByName(boolean asc){
        viewModel.sortPersonasByName(asc);

        personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.PersonaName);
        personaListAdapter.notifyDataSetChanged();

        recyclerView.setIndexBarVisibility(true);
    }

    public void sortPersonasByLevel(boolean asc){
        viewModel.sortPersonasByLevel(asc);

        recyclerView.setIndexBarVisibility(false);

        personaListAdapter.notifyDataSetChanged();
    }

    public void sortPersonasByArcana(boolean asc){
        viewModel.sortPersonasByArcana(asc);

        personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.ArcanaName);
        personaListAdapter.notifyDataSetChanged();

        recyclerView.setIndexBarVisibility(true);
    }

    public void filterPersonas(PersonaFilterArgs filterArgs){
        viewModel.filterPersonas(filterArgs);
    }

    public void setPersonas(List<MainListPersona> personas){
        viewModel.updatePersonas(personas);
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
                .plus()
                .inject(this);

        PersonaListViewModelFactory viewModelFactory = new PersonaListViewModelFactory(personas);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PersonaMainListViewModel.class);
        viewModel.getFilteredPersonas().observe(this, newPersonas -> {
            this.personas.clear();

            if(newPersonas != null){
                this.personas.addAll(newPersonas);
            }

            personaListAdapter.notifyDataSetChanged();
        });
    }
}

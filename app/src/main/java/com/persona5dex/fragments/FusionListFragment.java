package com.persona5dex.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListAdapter;
import com.persona5dex.jobs.PersonaJobCreator;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.viewmodels.FusionListViewModel;
import com.persona5dex.viewmodels.FusionListViewModelFactory;
import com.persona5dex.viewmodels.PersonaFusionViewModel;
import com.persona5dex.viewmodels.PersonaFusionViewModelFactory;
import com.persona5dex.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FusionListFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_TO_LIST = "isToList";
    private static final String PERSONA_ID = "personaName";

    private boolean isToList;
    private int personaID;
    private List<PersonaEdgeDisplay> edgeDisplays;
    private PersonaFusionListAdapter fusionListAdapter;

    private PersonaFusionViewModel viewModel;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ViewGroup listHeader;
    private SearchView searchView;

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    PersonaJobCreator personaJobCreator;

    @Inject
    PersonaDisplayEdgesRepository personaDisplayEdgesRepository;

    @Inject
    MainPersonaRepository mainPersonaRepository;

    private FusionListViewModel fusionListViewModel;

    public FusionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isToList Says whether you are creating a to list they are almost exactly the same
     * @return A new instance of fragment FusionListFragment.
     */
    public static FusionListFragment newInstance(boolean isToList, int personaID) {
        FusionListFragment fragment = new FusionListFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_TO_LIST, isToList);
        args.putInt(PERSONA_ID, personaID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            isToList = getArguments().getBoolean(IS_TO_LIST);
            personaID = getArguments().getInt(PERSONA_ID);
        }

        this.edgeDisplays = new ArrayList<>(500);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivityComponent().inject(this);

        final PersonaFusionViewModelFactory factory = new PersonaFusionViewModelFactory(
                personaDisplayEdgesRepository,
                mainPersonaRepository,
                personaID,
                personaJobCreator
        );

        setUpRecyclerView();

        fusionListAdapter = new PersonaFusionListAdapter(edgeDisplays, recyclerView);
        recyclerView.setAdapter(fusionListAdapter);

        TextView personaHeaderColumnOne = baseView.findViewById(R.id.textView_fusion_column_one_label);
        TextView personaHeaderColumnTwo = baseView.findViewById(R.id.textView_fusion_column_two_label);

        if(isToList) {
            personaHeaderColumnOne.setText(R.string.persona_one);
            personaHeaderColumnTwo.setText(R.string.persona_two);
        } else {
            personaHeaderColumnOne.setText(R.string.persona_two);
            personaHeaderColumnTwo.setText(R.string.result);
        }

        setProgressBarVisible(true);

        final LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();

        viewModel = new ViewModelProvider(requireActivity(), factory).get(PersonaFusionViewModel.class);

        final FusionListViewModelFactory fusionListViewModelFactory =
                new FusionListViewModelFactory(isToList, personaID, mainPersonaRepository);
        fusionListViewModel = new ViewModelProvider(this, fusionListViewModelFactory).get(FusionListViewModel.class);

        LiveData<List<PersonaEdgeDisplay>> edgesLiveData = isToList ? viewModel.getToEdges() : viewModel.getFromEdges();
        edgesLiveData.observe(viewLifecycleOwner, personaEdgeDisplays -> {
            fusionListViewModel.setEdgeDisplays(personaEdgeDisplays);

            setProgressBarVisible(false);

            updateDisplayEdges(personaEdgeDisplays);

            fusionListViewModel.getFilteredEdgeDisplayLiveData().observe(viewLifecycleOwner, this::updateDisplayEdges);
            fusionListViewModel.getQueryForDisplay().observe(viewLifecycleOwner, query -> {
                searchView.setQuery(query, false);
                searchView.clearFocus();
            });
        });
    }

    private void updateDisplayEdges(List<PersonaEdgeDisplay> personaEdgeDisplays) {
        edgeDisplays.clear();

        if(personaEdgeDisplays != null) {
            edgeDisplays.addAll(personaEdgeDisplays);
        }

        fusionListAdapter.notifyDataSetChanged();
    }

    private void setProgressBarVisible(boolean visible) {
        if(visible) {
            recyclerView.setVisibility(View.GONE);
            listHeader.setVisibility(View.GONE);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            searchView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
            listHeader.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpRecyclerView() {
        recyclerView = baseView.findViewById(R.id.recycler_view_persona_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_fusion_list, container, false);
        listHeader = baseView.findViewById(R.id.fusion_list_header);
        progressBar = baseView.findViewById(R.id.progress_bar_fusions);
        searchView = baseView.findViewById(R.id.fusion_search_view);
        setUpSearchView();
        return baseView;
    }

    private void setUpSearchView() {
        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0) {
                    fusionListViewModel.setSearch(null);
                }
                return false;
            }
        });
    }

    public void performSearch(String query) {
        fusionListViewModel.setSearch(query);
    }

    public void performSearch(int suggestionPersonaId) {
        fusionListViewModel.setSuggestionSearch(suggestionPersonaId);
    }
}

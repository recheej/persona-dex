package com.persona5dex.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.PersonaFusionActivity;
import com.persona5dex.adapters.PersonaFusionListAdapter;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.services.FusionCalculatorJobService;
import com.persona5dex.viewmodels.PersonaFusionViewModel;
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
    private RecyclerView recyclerView;

    PersonaFusionViewModel viewModel;

    private ProgressBar progressBar;

    private ViewGroup listHeader;

    private List<PersonaEdgeDisplay> edgeDisplays;
    private PersonaFusionListAdapter fusionListAdapter;

    @Inject
    ViewModelFactory viewModelFactory;
    private LiveData<List<PersonaEdgeDisplay>> edgesLiveData;

    private FusionListListener listener;

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

    public interface FusionListListener {
        void fusionListCountUpdated(int count, boolean isToList);
    }

    public void setListener(FusionListListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isToList = getArguments().getBoolean(IS_TO_LIST);
            personaID = getArguments().getInt(PERSONA_ID);
        }

        this.edgeDisplays = new ArrayList<>(500);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Persona5ApplicationComponent component = Persona5Application.get(activity).getComponent();
        component.viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(new LayoutModule(activity),
                        new ActivityContextModule(activity),
                        new ViewModelModule(),
                        new ViewModelRepositoryModule())
                .plus()
                .inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PersonaFusionViewModel.class);

        recyclerView = baseView.findViewById(R.id.recycler_view_persona_list);

        fusionListAdapter = new PersonaFusionListAdapter(edgeDisplays, recyclerView);
        recyclerView.setAdapter(fusionListAdapter);

        listHeader = baseView.findViewById(R.id.fusion_list_header);
        progressBar = baseView.findViewById(R.id.progress_bar_fusions);

        setProgressBarVisible(true);
        FusionCalculatorJobService.enqueueWork(getContext(), new Intent(getContext(), FusionCalculatorJobService.class));
    }

    private void setProgressBarVisible(boolean visible) {
        if(visible){
            recyclerView.setVisibility(View.GONE);
            listHeader.setVisibility(View.GONE);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else{
            progressBar.setVisibility(ProgressBar.GONE);
            listHeader.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerCalculationFinishedReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private void setUpRecyclerView() {

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        TextView personaHeaderColumnOne = baseView.findViewById(R.id.textView_fusion_column_one_label);
        TextView personaHeaderColumnTwo = baseView.findViewById(R.id.textView_fusion_column_two_label);

        if(isToList){
            personaHeaderColumnOne.setText(R.string.persona_one);
            personaHeaderColumnTwo.setText(R.string.persona_two);
        }
        else{
            personaHeaderColumnOne.setText(R.string.persona_two);
            personaHeaderColumnTwo.setText(R.string.result);
        }

        this.edgesLiveData = viewModel.getEdges(personaID, isToList);
        this.edgesLiveData.observe(this, new Observer<List<PersonaEdgeDisplay>>() {
            @Override
            public void onChanged(@Nullable List<PersonaEdgeDisplay> personaEdgeDisplays) {
                edgeDisplays.clear();

                if(personaEdgeDisplays != null){
                    edgeDisplays.addAll(personaEdgeDisplays);
                }

                if(listener != null){
                    listener.fusionListCountUpdated(edgeDisplays.size(), isToList);
                }

                fusionListAdapter.notifyDataSetChanged();
            }
        });
    }

    // Define the callback for what to do when fusion calculation service is finished
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUpRecyclerView();
            setProgressBarVisible(false);
        }
    };

    private void registerCalculationFinishedReceiver() {
        IntentFilter calculationFinishedIntentFilter = new IntentFilter(FusionCalculatorJobService.Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, calculationFinishedIntentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_fusion_list, container, false);
        return baseView;
    }
}

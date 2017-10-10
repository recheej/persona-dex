package com.example.rechee.persona5calculator.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.adapters.PersonaFusionListAdapter;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.PersonaStoreDisplay;
import com.example.rechee.persona5calculator.services.FusionCalculatorJobService;
import com.example.rechee.persona5calculator.viewmodels.PersonaFusionListViewModel;

import javax.inject.Inject;

public class FusionListFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_TO_LIST = "isToList";
    private static final String PERSONA_ID = "personaName";

    private boolean isToList;
    private int personaID;
    private RecyclerView recyclerView;

    @Inject
    PersonaFusionListViewModel viewModel;
    private ProgressBar progressBar;

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

        if (getArguments() != null) {
            isToList = getArguments().getBoolean(IS_TO_LIST);
            personaID = getArguments().getInt(PERSONA_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences commonSharedPreferences = activity.getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS,
                Context.MODE_PRIVATE);
        recyclerView = baseView.findViewById(R.id.recycler_view_persona_list);

        if(commonSharedPreferences.contains("initialized") && !commonSharedPreferences.contains("finished")){
            progressBar.setVisibility(ProgressBar.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

            FragmentComponent component = activity.getComponent().plus();
            component.inject(this);
        }
        else{
            FragmentComponent component = activity.getComponent().plus();
            component.inject(this);

            setUpRecyclerView();
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

        PersonaStoreDisplay personaStore = viewModel.getEdgesForPersona(personaID);

        PersonaFusionListAdapter fusionListAdapter;
        TextView personaHeaderColumnOne = baseView.findViewById(R.id.textView_fusion_column_one_label);
        TextView personaHeaderColumnTwo = baseView.findViewById(R.id.textView_fusion_column_two_label);

        if(this.isToList){
            fusionListAdapter = new PersonaFusionListAdapter(personaStore.edgesTo(), personaID, true, recyclerView, viewModel);

            personaHeaderColumnOne.setText(R.string.persona_one);
            personaHeaderColumnTwo.setText(R.string.persona_two);
        }
        else{
            fusionListAdapter = new PersonaFusionListAdapter(personaStore.edgesFrom(), personaID, false, recyclerView, viewModel);
            personaHeaderColumnOne.setText(R.string.persona_two);
            personaHeaderColumnTwo.setText(R.string.result);
        }
        recyclerView.setAdapter(fusionListAdapter);
    }

    // Define the callback for what to do when fusion calculation service is finished
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUpRecyclerView();
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
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

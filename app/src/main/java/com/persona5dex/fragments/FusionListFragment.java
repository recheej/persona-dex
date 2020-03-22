package com.persona5dex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListAdapter;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.extensions.WorkInfoStateUtils;
import com.persona5dex.jobs.PersonaJobCreator;
import com.persona5dex.models.PersonaEdgeDisplay;
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

    @Inject
    PersonaJobCreator personaJobCreator;
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

        Persona5Application.get(activity).getComponent()
                .activityComponent(new LayoutModule(activity),
                        new ActivityContextModule(activity)
                )
                .plus()
                .inject(this);

        personaJobCreator.getStateForGenerateFusionJob().observe(getViewLifecycleOwner(), state -> {
            if(WorkInfoStateUtils.isDone(state)){
                setUpRecyclerView();
                setProgressBarVisible(false);
            }
        });

        viewModel = new ViewModelProvider(this, viewModelFactory).get(PersonaFusionViewModel.class);

        recyclerView = baseView.findViewById(R.id.recycler_view_persona_list);

        fusionListAdapter = new PersonaFusionListAdapter(edgeDisplays, recyclerView);
        recyclerView.setAdapter(fusionListAdapter);

        listHeader = baseView.findViewById(R.id.fusion_list_header);
        progressBar = baseView.findViewById(R.id.progress_bar_fusions);

        setProgressBarVisible(true);
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
        this.edgesLiveData.observe(getViewLifecycleOwner(), personaEdgeDisplays -> {
            edgeDisplays.clear();

            if(personaEdgeDisplays != null){
                edgeDisplays.addAll(personaEdgeDisplays);
            }

            if(listener != null){
                listener.fusionListCountUpdated(edgeDisplays.size(), isToList);
            }

            fusionListAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_fusion_list, container, false);
        return baseView;
    }
}

package com.example.rechee.persona5calculator.fragments;

import android.os.Bundle;
import android.renderscript.BaseObj;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.BaseActivity;
import com.example.rechee.persona5calculator.adapters.PersonaFusionListAdapter;
import com.example.rechee.persona5calculator.adapters.PersonaListAdapter;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.PersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;
import com.example.rechee.persona5calculator.viewmodels.PersonaFusionListViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FusionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FusionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FusionListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_TO_LIST = "isToList";
    private static final String PERSONA_NAME = "personaName";

    private boolean isToList;
    private String personaName;
    private RecyclerView recyclerView;
    private BaseActivity activity ;

    @Inject
    PersonaFusionListViewModel viewModel;

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
    public static FusionListFragment newInstance(boolean isToList, String personaName) {
        FusionListFragment fragment = new FusionListFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_TO_LIST, isToList);
        args.putString(PERSONA_NAME, personaName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isToList = getArguments().getBoolean(IS_TO_LIST);
            personaName = getArguments().getString(PERSONA_NAME);
        }

        this.activity = (BaseActivity) getActivity();
        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentListView = inflater.inflate(R.layout.fragment_fusion_list, container, false);

        recyclerView = (RecyclerView) fragmentListView.findViewById(R.id.recycler_view_persona_list);
        recyclerView.hasFixedSize();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        PersonaStore personaStore = viewModel.getEdgesForPersona(personaName);

        PersonaFusionListAdapter fusionListAdapter;
        TextView personaHeaderColumnOne = (TextView) fragmentListView.findViewById(R.id.textView_fusion_column_one_label);
        TextView personaHeaderColumnTwo = (TextView) fragmentListView.findViewById(R.id.textView_fusion_column_two_label);

        if(this.isToList){
            fusionListAdapter = new PersonaFusionListAdapter(personaStore.edgesTo(), personaName, true);

            personaHeaderColumnOne.setText(R.string.persona_one);
            personaHeaderColumnTwo.setText(R.string.persona_two);
        }
        else{
            fusionListAdapter = new PersonaFusionListAdapter(personaStore.edgesFrom(), personaName, false);
            personaHeaderColumnOne.setText(R.string.persona_two);
            personaHeaderColumnTwo.setText(R.string.result);
        }
        recyclerView.setAdapter(fusionListAdapter);

        return fragmentListView;
    }
}

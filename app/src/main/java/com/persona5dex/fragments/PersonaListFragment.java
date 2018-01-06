package com.persona5dex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by Rechee on 1/6/2018.
 */

public class PersonaListFragment extends BaseFragment {

    //https://github.com/myinnos/AlphabetIndex-Fast-Scroll-RecyclerView
    private IndexFastScrollRecyclerView recyclerView;

    public

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_persona_list, container, false);

        recyclerView = baseView.findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);

        return baseView;
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
    }
}

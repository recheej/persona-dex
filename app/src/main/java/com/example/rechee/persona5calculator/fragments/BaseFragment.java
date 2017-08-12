package com.example.rechee.persona5calculator.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.rechee.persona5calculator.activities.BaseActivity;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;

/**
 * Created by Rechee on 8/12/2017.
 */

public class BaseFragment extends Fragment {

    protected BaseActivity activity;
    protected ActivityComponent activityComponent;
    protected View baseView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (BaseActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activityComponent = activity.getComponent();
    }
}

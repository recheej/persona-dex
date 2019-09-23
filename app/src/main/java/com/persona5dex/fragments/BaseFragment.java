package com.persona5dex.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.persona5dex.activities.BaseActivity;
import com.persona5dex.dagger.activity.ActivityComponent;

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

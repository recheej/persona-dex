package com.persona5dex.dagger.activity;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.persona5dex.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/3/2017.
 */

@Module
public class LayoutModule {

    @ActivityScope
    @Provides
    Toolbar mainToolbar(@Named("activityContext") Context context) {
        Activity activity = (Activity) context;
        return (Toolbar) activity.findViewById(R.id.main_toolbar);
    }

    @ActivityScope
    @Provides
    ProgressBar fusionsProgressBar(@Named("activityContext") Context context) {
        Activity activity = (Activity) context;
        return (ProgressBar) activity.findViewById(R.id.progress_bar_fusions);
    }
}

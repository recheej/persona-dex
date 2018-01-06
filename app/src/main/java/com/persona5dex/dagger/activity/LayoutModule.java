package com.persona5dex.dagger.activity;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.persona5dex.R;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/3/2017.
 */

@Module
public class LayoutModule {

    private final Activity activity;

    public LayoutModule(Activity activity){
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    Toolbar mainToolbar() {
        return (Toolbar) activity.findViewById(R.id.main_toolbar);
    }

    @ActivityScope
    @Provides
    ProgressBar fusionsProgressBar() {
        return (ProgressBar) activity.findViewById(R.id.progress_bar_fusions);
    }
}

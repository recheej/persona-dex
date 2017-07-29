package com.example.rechee.persona5calculator.dagger;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.example.rechee.persona5calculator.R;

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

    @Provides
    Toolbar mainToolbar() {
        return (Toolbar) activity.findViewById(R.id.main_toolbar);
    }
}

package com.example.rechee.persona5calculator.dagger;

import android.content.Context;
import android.support.v7.widget.Toolbar;

import com.example.rechee.persona5calculator.activities.MainActivity;
import com.example.rechee.persona5calculator.activities.PersonaDetailActivity;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 7/1/2017.
 */

@ActivityScope
@Component(modules = { LayoutModule.class, ActivityContextModule.class}, dependencies = {ViewModelComponent.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(PersonaDetailActivity activity);
    Toolbar mainToolbar();
    @Named("activityContext") Context activityContext();
}

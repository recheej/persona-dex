package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@Persona5ApplicationScope
@Component(modules = { ApplicationContextModule.class})
public interface Persona5ApplicationComponent {
    @Named("applicationContext") Context getContext();
    @Named("applicationGson") Gson gson();
}

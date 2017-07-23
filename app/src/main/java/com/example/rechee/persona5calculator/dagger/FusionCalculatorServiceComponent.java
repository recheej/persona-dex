package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.example.rechee.persona5calculator.services.FusionCalculatorService;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@FusionServiceScope
@Component(modules = {FusionServiceContextModule.class, FusionArcanaDataModule.class}, dependencies = {Persona5ApplicationComponent.class})
public interface FusionCalculatorServiceComponent {
    void inject(FusionCalculatorService service);
}

package com.example.rechee.persona5calculator.dagger;

import android.content.Context;
import android.util.SparseArray;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Rechee on 6/11/2017.
 */

@FusionServiceScope
@Component(dependencies = { Persona5ApplicationComponent.class}, modules = {FusionArcanaDataModule.class})
public interface FusionServiceComponent {
    SparseArray<List<Persona>> personasByArcana();
    HashMap<Enumerations.Arcana, HashMap<Enumerations.Arcana, Enumerations.Arcana>> arcanaTable();
}

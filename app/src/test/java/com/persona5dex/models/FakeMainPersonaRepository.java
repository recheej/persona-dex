package com.persona5dex.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.persona5dex.repositories.MainPersonaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public class FakeMainPersonaRepository implements MainPersonaRepository {

    private List<MainListPersona> fakeData;
    public FakeMainPersonaRepository() {
        fakeData = new ArrayList<>();

        MainListPersona fakePersona = new MainListPersona();
        fakePersona.name = "one";
        fakePersona.arcanaName = "any";
        fakePersona.arcana = Enumerations.Arcana.ANY;
        fakePersona.level = 1;

        fakeData.add(fakePersona);

        fakePersona = new MainListPersona();
        fakePersona.name = "two";
        fakePersona.arcanaName = "Chariot";
        fakePersona.arcana = Enumerations.Arcana.CHARIOT;
        fakePersona.level = 99;
        fakePersona.rare = true;
        fakePersona.dlc = true;

        fakeData.add(fakePersona);

        fakePersona = new MainListPersona();
        fakePersona.name = "three";
        fakePersona.arcanaName = "Hanged Man";
        fakePersona.arcana = Enumerations.Arcana.HANGED_MAN;
        fakePersona.level = 2;
        fakePersona.rare = true;

        fakeData.add(fakePersona);
    }

    public FakeMainPersonaRepository(List<MainListPersona> fakeData){
        this.fakeData = fakeData;
    }

    @Override
    public LiveData<List<MainListPersona>> getPersonasForMainList() {
        MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
        data.setValue(fakeData);
        return data;
    }
}

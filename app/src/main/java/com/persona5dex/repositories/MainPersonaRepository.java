package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.MainListPersona;

import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public interface MainPersonaRepository {
    LiveData<List<MainListPersona>> getPersonasForMainList();
}

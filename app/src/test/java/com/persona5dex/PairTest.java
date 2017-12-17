package com.persona5dex;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Pair;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.room.Persona;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.viewmodels.SettingsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Rechee on 8/13/2017.
 */

public class PairTest {

    @Test
    public void testPairEquals(){
        Pair<Integer, Integer> pairOne = new Pair<>(1, 2);
        Pair<Integer, Integer> pairTwo = new Pair<>(1, 2);

        assertTrue(pairOne.equals(pairTwo));
    }

    @Test
    public void testPairNotEquals(){
        Pair<Integer, Integer> pairOne = new Pair<>(1, 2);
        Pair<Integer, Integer> pairTwo = new Pair<>(1, 3);

        assertFalse(pairOne.equals(pairTwo));
    }

    @Test
    public void testPairWithSet(){
        Pair<Integer, Integer> pairOne = new Pair<>(1, 2);
        Pair<Integer, Integer> pairTwo = new Pair<>(1, 2);

        HashSet<Pair<Integer, Integer>> pairHashSet = new HashSet<>();
        pairHashSet.add(pairOne);

        assertTrue(pairHashSet.contains(pairTwo));
    }

    @Test
    public void testPairWithSetNegative(){
        Pair<Integer, Integer> pairOne = new Pair<>(1, 2);
        Pair<Integer, Integer> pairTwo = new Pair<>(1, 3);

        HashSet<Pair<Integer, Integer>> pairHashSet = new HashSet<>();
        pairHashSet.add(pairOne);

        assertFalse(pairHashSet.contains(pairTwo));
    }

    @Test
    public void testPairWithObject(){
        PersonaForFusionService one = new PersonaForFusionService();
        PersonaForFusionService two = new PersonaForFusionService();
        Pair<PersonaForFusionService, PersonaForFusionService> pairOne = new Pair<>(one, two);

        PersonaForFusionService three = two;
        PersonaForFusionService four = one;
        Pair<PersonaForFusionService, PersonaForFusionService> pairTwo = new Pair<>(three, four);

        assertTrue(pairOne.equals(pairTwo));
    }
}

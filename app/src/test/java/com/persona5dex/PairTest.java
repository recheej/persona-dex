package com.persona5dex;

import com.persona5dex.models.Pair;
import com.persona5dex.models.PersonaForFusionService;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by Rechee on 8/13/2017.
 */

public class PairTest {

    @Test
    public void testPairEquals(){
        Pair<Integer, Integer> pairOne = new Pair<>(1, 2);
        Pair<Integer, Integer> pairTwo = new Pair<>(1, 2);
        Pair<Integer, Integer> pairThree = new Pair<>(2, 1);

        assertTrue(pairOne.equals(pairTwo));
        assertTrue(pairTwo.equals(pairOne));
        assertTrue(pairOne.equals(pairThree));
        assertTrue(pairThree.equals(pairOne));
        assertTrue(pairThree.equals(pairTwo));
        assertTrue(pairTwo.equals(pairThree));
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

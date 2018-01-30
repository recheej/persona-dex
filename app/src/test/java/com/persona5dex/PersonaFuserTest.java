package com.persona5dex;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Pair;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.RawPersona;
import com.persona5dex.services.PersonaFuser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Rechee on 9/24/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersonaFuserTest {

    private final PersonaForFusionService[] allPersonas;
    private final PersonaForFusionService[] personasByLevel;
    private final HashMap<Enumerations.Arcana, HashMap<Enumerations.Arcana, Enumerations.Arcana>> arcanaTable;
    private final Map<String, int[]> rareCombos;

    public PersonaFuserTest() {
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream personaDataStream = classLoader.getResourceAsStream("person_data.json");
        InputStream arcanaTableStream = classLoader.getResourceAsStream("arcana_combo_data.json");
        InputStream rareComboSteam = classLoader.getResourceAsStream("rare_combos.json");

        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(new Gson());

        RawPersona[] rawPersonas = personaFileUtilities.getRawPersonas(personaDataStream);
        this.allPersonas = getAllPersonas(rawPersonas);
        this.personasByLevel = this.sortPersonasByLevel(allPersonas);

        this.arcanaTable = personaFileUtilities.getArcanaTable(arcanaTableStream);
        this.rareCombos = personaFileUtilities.rareCombos(rareComboSteam);
    }

    private PersonaForFusionService[] getAllPersonas(RawPersona[] rawPersonas){
        List<PersonaForFusionService> personas = new ArrayList(rawPersonas.length);

        for (int i = 0; i < rawPersonas.length; i++) {
            PersonaForFusionService persona = mockPersona(rawPersonas[i]);
            persona.setId(i);
            personas.add(persona);
        }

        return personas.toArray(new PersonaForFusionService[rawPersonas.length]);
    }

    public PersonaForFusionService mockPersona(RawPersona persona){
        PersonaForFusionService personaForFusionService = mock(PersonaForFusionService.class);

        Enumerations.Arcana arcana = Enumerations.Arcana.getArcana(persona.arcanaName);
        if(arcana == Enumerations.Arcana.ANY){
            arcana = Enumerations.Arcana.getArcana(persona.arcana);
        }

        when(personaForFusionService.getArcana()).thenReturn(arcana);
        when(personaForFusionService.getName()).thenReturn(persona.name);
        when(personaForFusionService.getArcanaName()).thenReturn(persona.arcana);
        when(personaForFusionService.getLevel()).thenReturn(persona.level);
        when(personaForFusionService.isRare()).thenReturn(persona.rare);
        when(personaForFusionService.isSpecial()).thenReturn(persona.special);
        when(personaForFusionService.isDlc()).thenReturn(persona.dlc);

        return personaForFusionService;
    }

    @Test
    public void basicFusionTest() throws Exception {
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        PersonaForFusionService personaOne = this.getPersonaByName("Apsaras");
        PersonaForFusionService personaTwo = this.getPersonaByName("Yaksini");

        PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("Eligor", result.getName());
    }

    @Test
    public void basicFusionTestTwo() throws Exception {
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        PersonaForFusionService personaOne = this.getPersonaByName("arsene");
        PersonaForFusionService personaTwo = this.getPersonaByName("pixie");

        PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("agathion", result.getName());
    }

    @Test
    public void basicFusionTestThree() throws Exception {

        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        PersonaForFusionService personaOne = this.getPersonaByName("orthrus");
        PersonaForFusionService personaTwo = this.getPersonaByName("matador");

        PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("sui-ki", result.getName());
    }

    @Test
    public void sameArcanaFusionTestSuccess() throws Exception {
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        PersonaForFusionService personaOne = this.getPersonaByName("pixie");
        PersonaForFusionService personaTwo = this.getPersonaByName("Leanan Sidhe");

        PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("saki mitama", result.getName());
    }

    @NonNull
    private PersonaFuser.PersonaFusionArgs getDefaultPersonaFuserArgs() {
        PersonaFuser.PersonaFusionArgs args = new PersonaFuser.PersonaFusionArgs();
        args.personasByLevel = personasByLevel;
        args.arcanaTable = arcanaTable;
        args.rareComboMap = rareCombos;
        args.rarePersonaAllowedInFusion = true;
        args.ownedDLCPersonaIDs = new HashSet<>();
        return args;
    }

    public void assertPersonaName(String expected, String actual){
        assertEquals(expected.toLowerCase(), actual.toLowerCase());
    }

    @Test
    public void rareCannotBeResultOfFusion() {
        //asserts that a rare persona cannot be fused from any fusion
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        for (PersonaForFusionService personaOne : allPersonas) {
            for (PersonaForFusionService personaTwo : allPersonas) {
                PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.isRare());
                }
            }
        }
    }

    @Test
    public void specialCannotBeResultOfFusion() {
        //asserts that a special persona cannot be fused from any fusion.
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        for (PersonaForFusionService personaOne : allPersonas) {
            for (PersonaForFusionService personaTwo : allPersonas) {
                PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.isSpecial());
                }
            }
        }
    }

    @Test
    public void fuserHandlesDLCPersonaFusion() {
        //tests that a dlc persona is included in fusions if setting on

        PersonaForFusionService ariadne = getPersonaByName("Ariadne");

        //asserts that a special persona cannot be fused from any fusion.
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();
        args.ownedDLCPersonaIDs.add(Integer.toString(ariadne.getId()));

        PersonaFuser fuser = new PersonaFuser(args);

        PersonaForFusionService clotho = getPersonaByName("clotho");
        PersonaForFusionService regent = getPersonaByName("regent");

        PersonaForFusionService result = fuser.fuseNormal(clotho, regent);

        assertNotNull(regent);
        assertEquals(ariadne.getName().toLowerCase(), result.getName().toLowerCase());
    }

    @Test
    public void fuserIgnoresDLCIfNotAvailable() {
        //asserts that no dlc persona are the result of any fusions if no dlc are selected
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);
        for (PersonaForFusionService personaOne : allPersonas) {
            for (PersonaForFusionService personaTwo : allPersonas) {
                PersonaForFusionService result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.isDlc());
                }
            }
        }
    }


    @Test
    public void fuserIgnoresRareIfFlagon() {
        //tests that if user doesn't allow rare persona, it returns null if rare persona part of fusion

        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();
        args.rarePersonaAllowedInFusion = false;

        PersonaForFusionService rarePersona = getPersonaByName("regent");
        PersonaForFusionService manDrake = getPersonaByName("mandrake");

        PersonaFuser fuser = new PersonaFuser(args);
        PersonaForFusionService result = fuser.fuseNormal(rarePersona, manDrake);

        assertNull(result);
    }

    private PersonaForFusionService getPersonaByName(String name) {
        for (PersonaForFusionService persona : allPersonas) {
            if(persona.getName().toLowerCase().equals(name.toLowerCase())){
                return persona;
            }
        }

        return null;
    }

    private PersonaForFusionService[] sortPersonasByLevel(PersonaForFusionService[] personas){
        PersonaForFusionService[] personsSortedByLevel = new PersonaForFusionService[personas.length];

        System.arraycopy(personas, 0, personsSortedByLevel, 0, personas.length);
        Arrays.sort(personsSortedByLevel, new Comparator<PersonaForFusionService>() {
            @Override
            public int compare(PersonaForFusionService o1, PersonaForFusionService o2) {
                if(o1.getLevel() < o2.getLevel()){
                    return -1;
                }

                if(o1.getLevel() == o2.getLevel()){
                    return 0;
                }

                return 1;
            }
        });

        return personsSortedByLevel;
    }


}

package com.persona5dex;

import android.support.annotation.NonNull;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Persona;
import com.persona5dex.services.PersonaFuser;
import com.google.gson.Gson;

import org.apache.tools.ant.taskdefs.Input;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Rechee on 9/24/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersonaFuserTest {

    private final Persona[] allPersonas;
    private final Persona[] personasByLevel;
    private final HashMap<Enumerations.Arcana, HashMap<Enumerations.Arcana, Enumerations.Arcana>> arcanaTable;
    private final Map<String, int[]> rareCombos;

    public PersonaFuserTest() {
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream personaDataStream = classLoader.getResourceAsStream("person_data.json");
        InputStream arcanaTableStream = classLoader.getResourceAsStream("arcana_combo_data.json");
        InputStream rareComboSteam = classLoader.getResourceAsStream("rare_combos.json");


        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(new Gson());

        this.allPersonas = personaFileUtilities.allPersonas(personaDataStream);

        this.personasByLevel = this.sortPersonasByLevel(allPersonas);
        setPersonaIDs();

        this.arcanaTable = personaFileUtilities.getArcanaTable(arcanaTableStream);
        this.rareCombos = personaFileUtilities.rareCombos(rareComboSteam);
    }

    private void setPersonaIDs() {
        for (int i = 0; i < allPersonas.length; i++) {
            Persona persona = allPersonas[i];
            persona.id = i;
        }
    }

    @Test
    public void basicFusionTest() throws Exception {
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        Persona personaOne = this.getPersonaByName("Apsaras");
        Persona personaTwo = this.getPersonaByName("Yaksini");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("Eligor", result.name);
    }

    @Test
    public void basicFusionTestTwo() throws Exception {
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        Persona personaOne = this.getPersonaByName("arsene");
        Persona personaTwo = this.getPersonaByName("pixie");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("agathion", result.name);
    }

    @Test
    public void basicFusionTestThree() throws Exception {

        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        Persona personaOne = this.getPersonaByName("orthrus");
        Persona personaTwo = this.getPersonaByName("matador");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("sui-ki", result.name);
    }

    @Test
    public void sameArcanaFusionTestSuccess() throws Exception {
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        Persona personaOne = this.getPersonaByName("pixie");
        Persona personaTwo = this.getPersonaByName("Leanan Sidhe");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("saki mitama", result.name);
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

        for (Persona personaOne : allPersonas) {
            for (Persona personaTwo : allPersonas) {
                Persona result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.rare);
                }
            }
        }
    }

    @Test
    public void specialCannotBeResultOfFusion() {
        //asserts that a special persona cannot be fused from any fusion.
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);

        for (Persona personaOne : allPersonas) {
            for (Persona personaTwo : allPersonas) {
                Persona result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.special);
                }
            }
        }
    }

    @Test
    public void fuserHandlesDLCPersonaFusion() {
        //tests that a dlc persona is included in fusions if setting on

        Persona ariadne = getPersonaByName("Ariadne");

        //asserts that a special persona cannot be fused from any fusion.
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();
        args.ownedDLCPersonaIDs.add(Integer.toString(ariadne.id));

        PersonaFuser fuser = new PersonaFuser(args);

        Persona clotho = getPersonaByName("clotho");
        Persona regent = getPersonaByName("regent");

        Persona result = fuser.fuseNormal(clotho, regent);

        assertNotNull(regent);
        assertEquals(ariadne.name.toLowerCase(), result.name.toLowerCase());
    }

    @Test
    public void fuserIgnoresDLCIfNotAvailable() {
        //asserts that no dlc persona are the result of any fusions if no dlc are selected
        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();

        PersonaFuser fuser = new PersonaFuser(args);
        for (Persona personaOne : allPersonas) {
            for (Persona personaTwo : allPersonas) {
                Persona result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.dlc);
                }
            }
        }
    }


    @Test
    public void fuserIgnoresRareIfFlagon() {
        //tests that if user doesn't allow rare persona, it returns null if rare persona part of fusion

        PersonaFuser.PersonaFusionArgs args = getDefaultPersonaFuserArgs();
        args.rarePersonaAllowedInFusion = false;

        Persona rarePersona = getPersonaByName("regent");
        Persona manDrake = getPersonaByName("mandrake");

        PersonaFuser fuser = new PersonaFuser(args);
        Persona result = fuser.fuseNormal(rarePersona, manDrake);

        assertNull(result);
    }

    private Persona getPersonaByName(String name) {
        for (Persona persona : allPersonas) {
            if(persona.name.toLowerCase().equals(name.toLowerCase())){
                return persona;
            }
        }

        return null;
    }

    private Persona[] sortPersonasByLevel(Persona[] personas){
        Persona[] personsSortedByLevel = new Persona[personas.length];

        System.arraycopy(personas, 0, personsSortedByLevel, 0, personas.length);
        Arrays.sort(personsSortedByLevel, new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                if(o1.level < o2.level){
                    return -1;
                }

                if(o1.level == o2.level){
                    return 0;
                }

                return 1;
            }
        });

        return personsSortedByLevel;
    }
}

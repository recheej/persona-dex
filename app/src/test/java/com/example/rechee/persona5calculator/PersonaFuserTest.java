package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.services.PersonaFuser;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    public PersonaFuserTest() {
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream personaDataStream = classLoader.getResourceAsStream("person_data.json");
        InputStream arcanaTableStream = classLoader.getResourceAsStream("arcana_combo_data.json");

        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(new Gson());

        this.allPersonas = personaFileUtilities.allPersonas(personaDataStream);
        this.personasByLevel = this.sortPersonasByLevel(allPersonas);
        this.arcanaTable = personaFileUtilities.getArcanaTable(arcanaTableStream);
    }

    @Test
    public void basicFusionTest() throws Exception {
        PersonaFuser fuser = new PersonaFuser(personasByLevel, arcanaTable);

        Persona personaOne = this.getPersonaByName("Apsaras");
        Persona personaTwo = this.getPersonaByName("Yaksini");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("Eligor", result.name);
    }

    @Test
    public void basicFusionTestTwo() throws Exception {
        PersonaFuser fuser = new PersonaFuser(personasByLevel, arcanaTable);

        Persona personaOne = this.getPersonaByName("arsene");
        Persona personaTwo = this.getPersonaByName("pixie");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("agathion", result.name);
    }

    @Test
    public void basicFusionTestThree() throws Exception {
        PersonaFuser fuser = new PersonaFuser(personasByLevel, arcanaTable);

        Persona personaOne = this.getPersonaByName("orthrus");
        Persona personaTwo = this.getPersonaByName("matador");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("sui-ki", result.name);
    }

    @Test
    public void sameArcanaFusionTestSuccess() throws Exception {
        PersonaFuser fuser = new PersonaFuser(personasByLevel, arcanaTable);

        Persona personaOne = this.getPersonaByName("pixie");
        Persona personaTwo = this.getPersonaByName("Leanan Sidhe");

        Persona result = fuser.fuseNormal(personaOne, personaTwo);
        assertPersonaName("saki mitama", result.name);
    }

    public void assertPersonaName(String expected, String actual){
        assertEquals(expected.toLowerCase(), actual.toLowerCase());
    }

    @Test
    public void rareCannotBeResultOfFusion() {
        //asserts that a rare persona cannot be fused from any fusion
        PersonaFuser fuser = new PersonaFuser(personasByLevel, arcanaTable);

        for (Persona personaOne : allPersonas) {
            for (Persona personaTwo : allPersonas) {
                Persona result = fuser.fuseNormal(personaOne, personaTwo);
                if(result != null){
                    assertFalse(result.rare);
                }
            }
        }
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

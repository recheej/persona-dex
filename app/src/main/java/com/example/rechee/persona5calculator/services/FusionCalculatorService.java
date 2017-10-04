package com.example.rechee.persona5calculator.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.dagger.FusionArcanaDataModule;
import com.example.rechee.persona5calculator.dagger.FusionCalculatorServiceComponent;
import com.example.rechee.persona5calculator.dagger.FusionServiceContextModule;
import com.example.rechee.persona5calculator.models.Pair;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawPersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaGraph;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.viewmodels.PersonaFusionListViewModel;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Named;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/17/2017.
 */

public class FusionCalculatorService extends IntentService {

    @Inject
    PersonaEdgesRepository personaEdgeRepository;

    @Inject
    PersonaTransferRepository personaTransferRepository;

    @Inject
    @Named("personaByLevel") Persona[] personaByLevel;

    @Inject
    HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;

    private final static String SERVICE_NAME = "FusionCalculatorService";

    public final class Constants {
        // Defines a custom Intent action
        public static final String BROADCAST_ACTION =
                "com.example.rechee.person5calculator.BROADCAST";
    }

    public FusionCalculatorService(){super(SERVICE_NAME);}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FusionCalculatorServiceComponent component = Persona5Application.get(this).getComponent()
                .plus(new FusionServiceContextModule(this), new FusionArcanaDataModule());
        component.inject(this);

        this.personaTransferRepository.setPersonaIDs(personaByLevel);
        this.personaTransferRepository.commit();

        PersonaFuser personaFuser = new PersonaFuser(personaByLevel, arcanaTable);
        PersonaGraph graph = this.makePersonaGraph(personaByLevel, personaFuser);

        this.personaEdgeRepository.markInit();
        for(Persona persona: personaByLevel){
            RawPersonaEdge[] edgesTo = graph.edgesTo(persona);
            edgesTo = PersonaFusionListViewModel.filterOutDuplicateEdges(edgesTo, persona.id, true);

            RawPersonaEdge[] edgesFrom = graph.edgesFrom(persona);
            edgesFrom = PersonaFusionListViewModel.filterOutDuplicateEdges(edgesFrom, persona.id, false);

            PersonaStore store = new PersonaStore(edgesFrom, edgesTo);
            this.personaEdgeRepository.addPersonaEdges(persona, store);
        }

        this.personaEdgeRepository.markFinished();

        Intent fusionCalculationFishedIntent = new Intent(Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(fusionCalculationFishedIntent);
    }

    private PersonaGraph makePersonaGraph(Persona[] personas, PersonaFuser personaFuser){

        HashSet<Pair<Persona, Persona>> pairSet = new HashSet<>(20000);
        PersonaGraph graph = new PersonaGraph();

        for (Persona personaOne: personas){
            for (Persona personaTwo: personas){

                Pair<Persona, Persona> personaPair = new Pair<>(personaOne, personaTwo);
                if(pairSet.contains(personaPair)){
                    continue;
                }

                Persona result = personaFuser.fuseNormal(personaOne, personaTwo);

                if(result != null) {
                    graph.addEdge(personaOne, personaTwo, result);
                }

                pairSet.add(personaPair);
            }
        }

        return graph;
    }
}

package com.example.rechee.persona5calculator.services;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.dagger.FusionArcanaDataModule;
import com.example.rechee.persona5calculator.dagger.FusionCalculatorServiceComponent;
import com.example.rechee.persona5calculator.dagger.FusionServiceContextModule;
import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Pair;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaGraph;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.models.RawPersonaEdge;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.viewmodels.PersonaFusionListViewModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Rechee on 10/8/2017.
 */

public class FusionCalculatorJobService extends JobIntentService {
    private static final int JOB_ID = 1000;

    @Inject
    PersonaEdgesRepository personaEdgeRepository;

    @Inject
    PersonaTransferRepository personaTransferRepository;

    @Inject
    Map<String, int[]> rarePersonaCombos;

    @Inject
    @Named("personaByLevel") Persona[] personaByLevel;

    @Inject
    HashMap<Enumerations.Arcana, HashMap<Enumerations.Arcana, Enumerations.Arcana>> arcanaTable;

    private final static String SERVICE_NAME = "FusionCalculatorJobService";

    public final class Constants {
        // Defines a custom Intent action
        public static final String BROADCAST_ACTION =
                "com.example.rechee.person5calculator.BROADCAST";
    }
    
    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, FusionCalculatorJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        FusionCalculatorServiceComponent component = Persona5Application.get(this).getComponent()
                .plus(new FusionServiceContextModule(this), new FusionArcanaDataModule());
        component.inject(this);

        this.personaTransferRepository.setPersonaIDs(personaByLevel);
        this.personaTransferRepository.commit();

        PersonaFuser.PersonaFusionArgs fuserArgs = new PersonaFuser.PersonaFusionArgs();
        fuserArgs.personasByLevel = personaByLevel;
        fuserArgs.arcanaTable = arcanaTable;
        fuserArgs.rareComboMap = rarePersonaCombos;
        fuserArgs.rarePersonaAllowedInFusion = personaTransferRepository.rarePersonaAllowedInFusions();
        fuserArgs.ownedDLCPersonaIDs = personaTransferRepository.getOwnedDlCPersonaIDs();

        PersonaFuser personaFuser = new PersonaFuser(fuserArgs);

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

        Intent fusionCalculationFishedIntent = new Intent(FusionCalculatorJobService.Constants.BROADCAST_ACTION);
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

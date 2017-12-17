package com.persona5dex.services;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;

import com.persona5dex.Persona5Application;
import com.persona5dex.dagger.fusionService.FusionArcanaDataModule;
import com.persona5dex.dagger.fusionService.FusionCalculatorServiceComponent;
import com.persona5dex.dagger.fusionService.FusionServiceContextModule;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Pair;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.PersonaGraph;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.models.RawPersonaEdge;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaTransferRepository;
import com.persona5dex.viewmodels.PersonaFusionViewModel;

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
    @Named("personaByLevel")
    PersonaForFusionService[] personaByLevel;

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

        final boolean forceReset = intent.getBooleanExtra("forceReset", false);
        if(forceReset || !personaEdgeRepository.edgesStored()){

            PersonaFuser.PersonaFusionArgs fuserArgs = new PersonaFuser.PersonaFusionArgs();
            fuserArgs.personasByLevel = personaByLevel;
            fuserArgs.arcanaTable = arcanaTable;
            fuserArgs.rareComboMap = rarePersonaCombos;
            fuserArgs.rarePersonaAllowedInFusion = personaTransferRepository.rarePersonaAllowedInFusions();
            fuserArgs.ownedDLCPersonaIDs = personaTransferRepository.getOwnedDlCPersonaIDs();

            PersonaFuser personaFuser = new PersonaFuser(fuserArgs);

            PersonaGraph graph = this.makePersonaGraph(personaByLevel, personaFuser);

            this.personaEdgeRepository.markInit();

            for(PersonaForFusionService persona: personaByLevel){
                RawPersonaEdge[] edgesTo = graph.edgesTo(persona);
                edgesTo = PersonaFusionViewModel.filterOutDuplicateEdges(edgesTo, persona.getId(), true);

                RawPersonaEdge[] edgesFrom = graph.edgesFrom(persona);
                edgesFrom = PersonaFusionViewModel.filterOutDuplicateEdges(edgesFrom, persona.getId(), false);

                PersonaStore store = new PersonaStore(edgesFrom, edgesTo);
                this.personaEdgeRepository.addPersonaEdges(store);
            }

            this.personaEdgeRepository.markFinished();
        }

        Intent fusionCalculationFishedIntent = new Intent(FusionCalculatorJobService.Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(fusionCalculationFishedIntent);
    }

    private PersonaGraph makePersonaGraph(PersonaForFusionService[] personas, PersonaFuser personaFuser){

        PersonaGraph graph = new PersonaGraph();

        for (PersonaForFusionService personaOne: personas){
            for (PersonaForFusionService personaTwo: personas){

                PersonaForFusionService result = personaFuser.fuseNormal(personaOne, personaTwo);

                if(result != null) {
                    graph.addEdge(personaOne, personaTwo, result);
                }
            }
        }

        return graph;
    }
}

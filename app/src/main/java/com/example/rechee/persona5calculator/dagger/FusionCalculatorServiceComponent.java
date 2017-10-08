package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.services.FusionCalculatorJobService;
import dagger.Subcomponent;

/**
 * Created by Rechee on 6/11/2017.
 */

@FusionServiceScope
@Subcomponent(modules = {
        FusionServiceContextModule.class,
        FusionArcanaDataModule.class
    }
)
public interface FusionCalculatorServiceComponent {
    void inject(FusionCalculatorJobService service);
}

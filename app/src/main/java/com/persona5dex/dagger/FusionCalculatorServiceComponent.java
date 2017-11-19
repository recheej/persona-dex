package com.persona5dex.dagger;

import com.persona5dex.services.CreateDatabaseJobService;
import com.persona5dex.services.FusionCalculatorJobService;
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
    void inject(CreateDatabaseJobService service);
}

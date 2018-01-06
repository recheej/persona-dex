package com.persona5dex.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListPagerAdapter;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.application.Persona5ApplicationComponent;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.viewmodels.PersonaFusionViewModel;

import javax.inject.Inject;

public class PersonaFusionActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    PersonaFusionViewModel viewModel;

    private int personaForFusionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_fusion);

        ActivityComponent component = Persona5Application.get(this).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .activityComponent(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        personaForFusionID = getIntent().getIntExtra("persona_id", 1);

        Persona5ApplicationComponent applicationComponent = Persona5Application.get(this).getComponent();
        viewModel = ViewModelProviders.of(this).get(PersonaFusionViewModel.class);
        viewModel.init(applicationComponent, personaForFusionID, false);

        setUpToolbar();

        ViewPager viewPager = findViewById(R.id.view_pager_fusion);
        PersonaFusionListPagerAdapter pagerAdapter = new PersonaFusionListPagerAdapter(getSupportFragmentManager(), this, personaForFusionID);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout_fusions);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpToolbar(){

        if(BuildConfig.ENABLE_CRASHLYTICS){
            //see how personas are being viewed in app
//            Answers.getInstance().logContentView(new ContentViewEvent()
//                    .putContentName("View Persona Fusion")
//                    .putContentType("View Persona Screen")
//                    .putContentId(personaName)
//            );
        }

        setSupportActionBar(this.mainToolbar);
        mainToolbar.setTitle(R.string.loading_data);

        viewModel.getPersonaName(personaForFusionID).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String personaName) {
                mainToolbar.setTitle(String.format("Fusions for: %s", personaName));
            }
        });
    }
}

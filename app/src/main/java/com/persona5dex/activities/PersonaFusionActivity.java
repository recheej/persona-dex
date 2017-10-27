package com.persona5dex.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListAdapter;
import com.persona5dex.adapters.PersonaFusionListPagerAdapter;
import com.persona5dex.dagger.ActivityComponent;
import com.persona5dex.dagger.ActivityContextModule;
import com.persona5dex.dagger.LayoutModule;
import com.persona5dex.dagger.ViewModelModule;
import com.persona5dex.dagger.ViewModelRepositoryModule;
import com.persona5dex.viewmodels.PersonaDetailViewModel;

import javax.inject.Inject;

public class PersonaFusionActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDetailViewModel viewModel;

    private int personaForFusionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_fusion);

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        personaForFusionID = viewModel.getPersonaForFusion();

        setUpToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_fusion);
        PersonaFusionListPagerAdapter pagerAdapter = new PersonaFusionListPagerAdapter(getSupportFragmentManager(), this, personaForFusionID);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_fusions);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpToolbar(){
        String personaName = viewModel.getPersonaName(personaForFusionID);

        if(BuildConfig.ENABLE_CRASHLYTICS){
            //see how personas are being viewed in app
//            Answers.getInstance().logContentView(new ContentViewEvent()
//                    .putContentName("View Persona Fusion")
//                    .putContentType("View Persona Screen")
//                    .putContentId(personaName)
//            );
        }

        this.mainToolbar.setTitle(String.format("Fusions for: %s", personaName));

        setSupportActionBar(this.mainToolbar);
    }
}

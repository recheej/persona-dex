package com.persona5dex.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListPagerAdapter;
import com.persona5dex.dagger.activity.ActivityComponent;
import com.persona5dex.dagger.activity.ActivityContextModule;
import com.persona5dex.dagger.activity.LayoutModule;
import com.persona5dex.dagger.activity.ViewModelModule;
import com.persona5dex.dagger.activity.ViewModelRepositoryModule;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.fragments.AdvancedPersonaFragment;
import com.persona5dex.fragments.FusionListFragment;
import com.persona5dex.viewmodels.PersonaFusionViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class PersonaFusionActivity extends BaseActivity implements FusionListFragment.FusionListListener {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ViewModelFactory viewModelFactory;

    private PersonaFusionViewModel viewModel;

    private int personaForFusionID;
    private PersonaFusionListPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_persona_fusion);

        personaForFusionID = getIntent().getIntExtra("persona_id", 1);

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

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PersonaFusionViewModel.class);

        setUpToolbar();

        LiveData<Integer> personaIsAdvanced = viewModel.personaIsAdvanced(personaForFusionID);
        personaIsAdvanced.observe(this, intValue -> {
            if(intValue == null){
                intValue = 0;
            }

            boolean isAdvanced = intValue == 1;

            Fragment toFragment;
            if(isAdvanced){
                toFragment = AdvancedPersonaFragment.newInstance(personaForFusionID);
            }
            else{
                FusionListFragment fusionListFragment = FusionListFragment.newInstance(true, personaForFusionID);
                toFragment = fusionListFragment;
                fusionListFragment.setListener(this);
            }

            FusionListFragment fromFragment = FusionListFragment.newInstance(false, personaForFusionID);
            fromFragment.setListener(this);

            viewPager = findViewById(R.id.view_pager_fusion);
            pagerAdapter = new PersonaFusionListPagerAdapter(getSupportFragmentManager(),
                    this, toFragment, fromFragment);
            viewPager.setAdapter(pagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tab_layout_fusions);
            tabLayout.setupWithViewPager(viewPager);
        });
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

        viewModel.getPersonaName(personaForFusionID).observe(this,
                personaName -> mainToolbar.setTitle(String.format("Fusions for: %s", personaName)));
    }

    @Override
    public void fusionListCountUpdated(int count, boolean isToList) {
        if(isToList){
            pagerAdapter.setToListCount(count);
        }
        else{
            pagerAdapter.setFromListCount(count);
        }

        viewPager.setAdapter(pagerAdapter);
    }
}

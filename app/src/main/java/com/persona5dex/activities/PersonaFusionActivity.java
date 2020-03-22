package com.persona5dex.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.persona5dex.BuildConfig;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListPagerAdapter;
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
        component.inject(this);

        personaForFusionID = getIntent().getIntExtra("persona_id", 1);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(PersonaFusionViewModel.class);

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

package com.persona5dex.activities;

import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.persona5dex.BuildConfig;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListPagerAdapter;
import com.persona5dex.fragments.AdvancedPersonaFragment;
import com.persona5dex.fragments.FusionListFragment;
import com.persona5dex.jobs.PersonaJobCreator;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.viewmodels.PersonaFusionViewModelFactory;
import com.persona5dex.viewmodels.PersonaFusionViewModel;

import java.util.Locale;

import javax.inject.Inject;

public class PersonaFusionActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    PersonaDisplayEdgesRepository personaDisplayEdgesRepository;

    @Inject
    MainPersonaRepository mainPersonaRepository;

    @Inject
    PersonaJobCreator personaJobCreator;

    private PersonaFusionViewModel viewModel;

    private int personaForFusionID;
    private PersonaFusionListPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_persona_fusion);
        component.inject(this);

        personaForFusionID = getIntent().getIntExtra("persona_id", 1);

        final PersonaFusionViewModelFactory factory = new PersonaFusionViewModelFactory(
                personaDisplayEdgesRepository,
                mainPersonaRepository,
                personaForFusionID,
                personaJobCreator
        );
        viewModel = new ViewModelProvider(this, factory).get(PersonaFusionViewModel.class);

        setUpToolbar();

        viewPager = findViewById(R.id.view_pager_fusion);
        tabLayout = findViewById(R.id.tab_layout_fusions);

        viewModel.getPersonaIsAdvanced().observe(this, intValue -> {
            if(intValue == null) {
                intValue = 0;
            }

            boolean isAdvanced = intValue == 1;

            Fragment toFragment;
            if(isAdvanced) {
                toFragment = AdvancedPersonaFragment.newInstance(personaForFusionID);
            } else {
                toFragment = FusionListFragment.newInstance(true, personaForFusionID);
            }

            FusionListFragment fromFragment = FusionListFragment.newInstance(false, personaForFusionID);

            pagerAdapter = new PersonaFusionListPagerAdapter(getSupportFragmentManager(),
                    this, toFragment, fromFragment);
            viewPager.setAdapter(pagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tab_layout_fusions);
            tabLayout.setupWithViewPager(viewPager);
        });

        viewModel.getToEdges().observe(this, toEdges -> {
            setTabTextCount(true, toEdges.size());
        });

        viewModel.getFromEdges().observe(this, fromEdges -> {
            setTabTextCount(false, fromEdges.size());
        });
    }

    private void setUpToolbar() {

        if(BuildConfig.ENABLE_CRASHLYTICS) {
            //see how personas are being viewed in app
//            Answers.getInstance().logContentView(new ContentViewEvent()
//                    .putContentName("View Persona Fusion")
//                    .putContentType("View Persona Screen")
//                    .putContentId(personaName)
//            );
        }

        setSupportActionBar(this.mainToolbar);
        mainToolbar.setTitle(R.string.loading_data);

        viewModel.getPersonaName().observe(this,
                personaName -> mainToolbar.setTitle(String.format("Fusions for: %s", personaName)));
    }

    private void setTabTextCount(boolean toList, int count) {
        int position = toList ? 0 : 1;
        final String tabText = getTabText(toList, count);
        final TabLayout.Tab tab = tabLayout.getTabAt(position);
        if(tab != null) {
            tab.setText(tabText);
        }
    }

    private String getTabText(boolean toList, int count) {
        @StringRes int headerTextRes = toList ? R.string.to : R.string.from;
        String headerText = getString(headerTextRes);
        if(count == 0) {
            return headerText;
        }

        return String.format(Locale.getDefault(), "%s (%d)", headerText, count);
    }
}

package com.persona5dex.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaFusionListPagerAdapter;
import com.persona5dex.fragments.AdvancedPersonaFragment;
import com.persona5dex.fragments.FusionListFragment;
import com.persona5dex.fragments.PersonaListRepositoryType;
import com.persona5dex.jobs.PersonaJobCreator;
import com.persona5dex.models.GameType;
import com.persona5dex.models.PersonaRepository;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaListRepositoryFactory;
import com.persona5dex.viewmodels.PersonaFusionViewModel;
import com.persona5dex.viewmodels.PersonaFusionViewModelFactory;
import com.persona5dex.viewmodels.PersonaListViewModelFactory;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

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
    @Inject
    PersonaListRepositoryFactory personaListRepositoryFactory;
    @Inject
    ArcanaNameProvider arcanaNameProvider;
    @Inject
    GameType gameType;

    private PersonaFusionViewModel viewModel;

    private int personaForFusionID;
    private PersonaFusionListPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_persona_fusion);
        personaForFusionID = getIntent().getIntExtra("persona_id", 1);

        Persona5Application.get(this).getComponent().activityComponent()
                .activityContext(this)
                .advancedPersonaId(personaForFusionID).build().inject(this);

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
                configureListViewModel();
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

        setTabBarText(0, R.string.to);
        setTabBarText(1, R.string.from);

        viewModel.getToEdges().observe(this, toEdges -> {
            setTabTextCount(true, toEdges.size());
        });

        viewModel.getFromEdges().observe(this, fromEdges -> {
            setTabTextCount(false, fromEdges.size());
        });

        if(savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    private void configureListViewModel() {
        final PersonaRepository personaListRepository = personaListRepositoryFactory.getPersonaListRepository(PersonaListRepositoryType.ADVANCED);
        final PersonaListViewModelFactory personaListViewModelFactory = new PersonaListViewModelFactory(arcanaNameProvider, gameType, personaListRepository, defaultSharedPreferences);
        new ViewModelProvider(this, personaListViewModelFactory).get(PersonaMainListViewModel.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(query == null) {
                // user searched by clicking persona
                final String data = intent.getDataString();
                if(data != null) {
                    int suggestionPersonaId = Integer.parseInt(data);
                    performSearch(suggestionPersonaId);
                }
            } else {
                performSearch(query);
            }
        }
    }

    private void performSearch(int suggestionPersonaId) {
        final FusionListFragment fusionListFragment = getCurrentFusionListFragment();
        if(fusionListFragment != null) {
            fusionListFragment.performSearch(suggestionPersonaId);
        }
    }

    private void performSearch(String query) {
        final FusionListFragment fusionListFragment = getCurrentFusionListFragment();
        if(fusionListFragment != null) {
            fusionListFragment.performSearch(query);
        }
    }

    @Nullable
    private FusionListFragment getCurrentFusionListFragment() {
        final Fragment currentFragment = pagerAdapter.getCurrentFragment();
        if(currentFragment instanceof FusionListFragment) {
            return (FusionListFragment) currentFragment;
        }

        return null;
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
                personaName -> mainToolbar.setTitle(String.format("Fusions for: %s", personaName == null ? "" : personaName)));
    }

    private void setTabTextCount(boolean toList, int count) {
        int position = toList ? 0 : 1;
        final String tabText = getTabText(toList, count);
        setTabBarText(position, tabText);
    }

    private void setTabBarText(int position, String tabText) {
        final TabLayout.Tab tab = tabLayout.getTabAt(position);
        if(tab != null) {
            tab.setText(tabText);
        }
    }

    private void setTabBarText(int position, @StringRes int textRes) {
        final String tabText = getString(textRes);
        setTabBarText(position, tabText);
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

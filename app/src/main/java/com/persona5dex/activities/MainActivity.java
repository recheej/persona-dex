package com.persona5dex.activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.persona5dex.BuildConfig;
import com.persona5dex.Persona5Application;
import com.persona5dex.PersonaUtilities;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaListAdapter;
import com.persona5dex.dagger.ActivityComponent;
import com.persona5dex.dagger.ActivityContextModule;
import com.persona5dex.dagger.LayoutModule;
import com.persona5dex.dagger.ViewModelModule;
import com.persona5dex.dagger.ViewModelRepositoryModule;
import com.persona5dex.fragments.FilterDialogFragment;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.services.FusionCalculatorJobService;
import com.persona5dex.viewmodels.PersonaMainListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity implements FilterDialogFragment.OnFilterListener {

    private static final String FILTER_DIALOG = "FILTER_DIALOG";

    //https://github.com/myinnos/AlphabetIndex-Fast-Scroll-RecyclerView
    private IndexFastScrollRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PersonaListAdapter personaListAdapter;

    @Inject
    Toolbar mainToolbar;

    private PersonaMainListViewModel viewModel;

    private PersonaFilterArgs latestFilterArgs;

    private int selectedSortMenuItemID;
    private List<MainListPersona> filteredPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule()
        );
        component.inject(this);
        this.component = component;

        //init crashlytics
        if(BuildConfig.ENABLE_CRASHLYTICS){
            Fabric.with(this, new Crashlytics());
        }

        this.viewModel = ViewModelProviders.of(this).get(PersonaMainListViewModel.class);
        this.viewModel.inject(Persona5Application.get(this).getComponent());

        filteredPersonas = new ArrayList<>(250);

        recyclerView = findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);

        personaListAdapter = new PersonaListAdapter(filteredPersonas);
        recyclerView.setAdapter(personaListAdapter);

        viewModel.getFilteredPersonas().observe(this, new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> mainListPersonas) {
                MainActivity.this.filteredPersonas.clear();

                if(mainListPersonas != null){
                    MainActivity.this.filteredPersonas.addAll(mainListPersonas);
                }

                personaListAdapter.notifyDataSetChanged();
            }
        });

        //sets default values for preferences only once in entire lifetime of application
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences commonSharedPreferences = getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS,
                Context.MODE_PRIVATE);

        boolean isInitialized = commonSharedPreferences.getBoolean("initialized", false);
        boolean isFinished = commonSharedPreferences.getBoolean("finished", false);
        if(!isInitialized || !isFinished){
            FusionCalculatorJobService.enqueueWork(this, new Intent(this, FusionCalculatorJobService.class));
        }

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainToolbar.setLogo(R.drawable.ic_app_icon_fore);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            viewModel.filterPersonas(query);
        }
        else if(Intent.ACTION_VIEW.equals(intent.getAction())){
            String personaName = intent.getDataString();
            //viewModel.storePersonaForDetail(personaName);

            if(BuildConfig.ENABLE_CRASHLYTICS){
                Answers.getInstance().logSearch(new SearchEvent()
                        .putQuery(personaName));
            }


            Intent startDetailIntent = new Intent(this, PersonaDetailActivity.class);
            startActivity(startDetailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.persona_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem settingsItem = menu.findItem(R.id.action_settings);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                menu.setGroupVisible(R.id.menu_item_group_sorting, false);
                settingsItem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                viewModel.filterPersonas("");

                menu.setGroupVisible(R.id.menu_item_group_sorting, true);
                settingsItem.setVisible(true);
                invalidateOptionsMenu();
                return true;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = getComponentName();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_sort:
                this.showSortPopup();
                return true;
            case R.id.action_filter:
                FilterDialogFragment dialogFragment = FilterDialogFragment.newInstance(latestFilterArgs);
                dialogFragment.show(getSupportFragmentManager(), FILTER_DIALOG);
                return true;
            case R.id.action_settings:
                Intent goToSettingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(goToSettingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortPopup(){
        View anchor = findViewById(R.id.action_search);
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.inflate(R.menu.sort_persona_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity.this.selectedSortMenuItemID = item.getItemId();
                return handleSortClick();
            }
        });

        popupMenu.show();
    }

    /**
     * Handles when the sort menu item is clicked. Will sort the current persona items and
     * set index bar visibility
     * @return True if selected sort menu item matches one of menu item ids
     */
    private boolean handleSortClick() {
        switch (selectedSortMenuItemID){
            case R.id.menu_sort_name_asc:
                viewModel.sortPersonasByName(true);
                personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.PersonaName);

                personaListAdapter.notifyDataSetChanged();

                recyclerView.setIndexBarVisibility(true);
                return true;
            case R.id.menu_sort_name_desc:
                viewModel.sortPersonasByName(false);
                personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.PersonaName);

                recyclerView.setIndexBarVisibility(true);
                return true;
            case R.id.menu_sort_level_asc:
                viewModel.sortPersonasByLevel(true);

                recyclerView.setIndexBarVisibility(false);
                return true;
            case R.id.menu_sort_level_desc:
                viewModel.sortPersonasByLevel(false);

                recyclerView.setIndexBarVisibility(false);
                return true;
            case R.id.menu_sort_arcana_asc:
                viewModel.sortPersonasByArcana(true);

                personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.ArcanaName);

                personaListAdapter.notifyDataSetChanged();

                recyclerView.setIndexBarVisibility(true);
                return true;
            case R.id.menu_sort_arcana_desc:
                viewModel.sortPersonasByArcana(false);

                personaListAdapter.setIndexerType(PersonaListAdapter.IndexerType.ArcanaName);

                personaListAdapter.notifyDataSetChanged();

                recyclerView.setIndexBarVisibility(true);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onFilterSelected(PersonaFilterArgs filterArgs) {
        this.latestFilterArgs = filterArgs;
        viewModel.filterPersonas(latestFilterArgs);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(latestFilterArgs == null){
            latestFilterArgs = new PersonaFilterArgs();
        }

        outState.putBoolean("filter_rarePersona", latestFilterArgs.rarePersona);
        outState.putBoolean("filter_dlcPersona", latestFilterArgs.dlcPersona);
        outState.putInt("filter_selectedArcana", latestFilterArgs.arcana.value());
        outState.putInt("filter_minLevel", latestFilterArgs.minLevel);
        outState.putInt("filter_maxLevel", latestFilterArgs.maxLevel);

        outState.putInt("sort_id", selectedSortMenuItemID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            latestFilterArgs = new PersonaFilterArgs();
            latestFilterArgs.rarePersona = savedInstanceState.getBoolean("filter_rarePersona");
            latestFilterArgs.dlcPersona = savedInstanceState.getBoolean("filter_dlcPersona");

            int arcanaValue = savedInstanceState.getInt("filter_selectedArcana");
            latestFilterArgs.arcana = Enumerations.Arcana.getArcana(arcanaValue);

            latestFilterArgs.minLevel = savedInstanceState.getInt("filter_minLevel");
            latestFilterArgs.maxLevel = savedInstanceState.getInt("filter_maxLevel");
            viewModel.filterPersonas(latestFilterArgs);

            selectedSortMenuItemID = savedInstanceState.getInt("sort_id");
            handleSortClick();
        }
    }
}

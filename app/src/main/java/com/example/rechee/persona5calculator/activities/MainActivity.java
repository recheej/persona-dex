package com.example.rechee.persona5calculator.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.adapters.PersonaListAdapter;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.ViewModelModule;
import com.example.rechee.persona5calculator.dagger.ViewModelRepositoryModule;
import com.example.rechee.persona5calculator.fragments.FilterDialogFragment;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaFilterArgs;
import com.example.rechee.persona5calculator.services.FusionCalculatorService;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;

import javax.inject.Inject;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class MainActivity extends BaseActivity implements FilterDialogFragment.OnFilterListener {

    private static final String FILTER_DIALOG = "FILTER_DIALOG";
    private IndexFastScrollRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PersonaListAdapter personaListAdapter;

    @Inject
    Toolbar mainToolbar;

    private Persona[] filteredPersonas;
    private Persona[] allPersonas;

    @Inject
    PersonaListViewModel viewModel;
    private PersonaFilterArgs latestFilterArgs;

    private int selectedSortMenuItemID;

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

        //sets default values for preferences only once in entire lifetime of application
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences commonSharedPreferences = getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS,
                Context.MODE_PRIVATE);

        recyclerView = (IndexFastScrollRecyclerView) findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);

        if(!commonSharedPreferences.contains("initialized") || (commonSharedPreferences.contains("initialized") && !commonSharedPreferences.contains("finished"))){
            startService(new Intent(this, FusionCalculatorService.class));
        }

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainToolbar.setLogo(R.drawable.ic_app_icon_fore);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        this.filteredPersonas = viewModel.getAllPersonas();
        this.allPersonas = this.filteredPersonas;

        personaListAdapter = new PersonaListAdapter(this.filteredPersonas, viewModel);
        recyclerView.setAdapter(personaListAdapter);

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

            this.filteredPersonas = viewModel.filterPersonas(this.allPersonas, query);
            personaListAdapter.setPersonas(this.filteredPersonas);
        }
        else if(Intent.ACTION_VIEW.equals(intent.getAction())){
            String personaName = intent.getDataString();
            viewModel.storePersonaForDetail(personaName);

            Intent startDetailIntent = new Intent(this, PersonaDetailActivity.class);
            startActivity(startDetailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.persona_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem settingsitem = menu.findItem(R.id.action_settings);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.setGroupVisible(R.id.menu_item_group_sorting, false);
                settingsitem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //restore all personas to list when you click the x button on search
                MainActivity.this.filteredPersonas = MainActivity.this.allPersonas;
                MainActivity.this.personaListAdapter.setPersonas(MainActivity.this.filteredPersonas);

                menu.setGroupVisible(R.id.menu_item_group_sorting, true);
                settingsitem.setVisible(true);
                invalidateOptionsMenu();
                return true;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

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
        View sortMenuView = findViewById(R.id.action_sort);
        PopupMenu popupMenu = new PopupMenu(this, sortMenuView);
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
                viewModel.sortPersonasByName(filteredPersonas, true);
                personaListAdapter.setPersonas(filteredPersonas);

                recyclerView.setIndexBarVisibility(true);
                return true;
            case R.id.menu_sort_name_desc:
                viewModel.sortPersonasByName(filteredPersonas, false);
                personaListAdapter.setPersonas(filteredPersonas);

                recyclerView.setIndexBarVisibility(true);
                return true;
            case R.id.menu_sort_level_asc:
                viewModel.sortPersonasByLevel(filteredPersonas, true);
                personaListAdapter.setPersonas(filteredPersonas);

                recyclerView.setIndexBarVisibility(false);
                return true;
            case R.id.menu_sort_level_desc:
                viewModel.sortPersonasByLevel(filteredPersonas, false);
                personaListAdapter.setPersonas(filteredPersonas);

                recyclerView.setIndexBarVisibility(false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onFilterSelected(PersonaFilterArgs filterArgs) {
        this.latestFilterArgs = filterArgs;

        filterPersonas();
    }

    private void filterPersonas() {
        if(latestFilterArgs.arcana == null){
            latestFilterArgs.arcanaOrdinal = -1;
        }
        else{
            latestFilterArgs.arcanaOrdinal = latestFilterArgs.arcana.ordinal();
        }

        this.filteredPersonas = viewModel.filterPersonas(latestFilterArgs, allPersonas);
        personaListAdapter.setPersonas(this.filteredPersonas);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(latestFilterArgs == null){
            latestFilterArgs = new PersonaFilterArgs();
        }

        outState.putBoolean("filter_rarePersona", latestFilterArgs.rarePersona);
        outState.putBoolean("filter_dlcPersona", latestFilterArgs.dlcPersona);

        if(latestFilterArgs.arcana == null){
            //we're marking 'Any' arcana as -1
            outState.putInt("filter_selectedArcana", -1);
        }
        else{
            outState.putInt("filter_selectedArcana", latestFilterArgs.arcanaOrdinal);
        }

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
            latestFilterArgs.arcanaOrdinal = savedInstanceState.getInt("filter_selectedArcana");
            latestFilterArgs.minLevel = savedInstanceState.getInt("filter_minLevel");
            latestFilterArgs.maxLevel = savedInstanceState.getInt("filter_maxLevel");
            this.filterPersonas();

            selectedSortMenuItemID = savedInstanceState.getInt("sort_id");
            handleSortClick();
        }
    }
}

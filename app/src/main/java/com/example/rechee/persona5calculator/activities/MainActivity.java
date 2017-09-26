package com.example.rechee.persona5calculator.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private DividerItemDecoration mDividerItemDecoration;

    @Inject
    Toolbar mainToolbar;

    private Persona[] filteredPersonas;
    private Persona[] allPersonas;

    @Inject
    PersonaListViewModel viewModel;

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

        SharedPreferences commonSharedPreferences = getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS,
                Context.MODE_PRIVATE);

        recyclerView = (IndexFastScrollRecyclerView) findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);

        if(!commonSharedPreferences.contains("initialized") || (commonSharedPreferences.contains("initialized") && !commonSharedPreferences.contains("finished"))){
            startService(new Intent(this, FusionCalculatorService.class));
        }

        setSupportActionBar(this.mainToolbar);

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
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.setGroupVisible(R.id.menu_item_group_sorting, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //restore all personas to list when you click the x button on search
                MainActivity.this.filteredPersonas = MainActivity.this.allPersonas;
                MainActivity.this.personaListAdapter.setPersonas(MainActivity.this.filteredPersonas);

                menu.setGroupVisible(R.id.menu_item_group_sorting, true);
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
                FilterDialogFragment dialogFragment = new FilterDialogFragment();
                dialogFragment.show(getFragmentManager(), FILTER_DIALOG);
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
                switch (item.getItemId()){
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
        });

        popupMenu.show();
    }

    @Override
    public void onFilterSelected(PersonaFilterArgs filterArgs) {
        this.filteredPersonas = viewModel.filterPersonas(filterArgs, allPersonas);
        personaListAdapter.setPersonas(this.filteredPersonas);
    }
}

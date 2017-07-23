package com.example.rechee.persona5calculator.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.DaggerActivityComponent;
import com.example.rechee.persona5calculator.dagger.DaggerViewModelComponent;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.dagger.RepositoryModule;
import com.example.rechee.persona5calculator.dagger.ViewModelComponent;
import com.example.rechee.persona5calculator.fragments.PersonaListAdapter;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.services.FusionCalculatorService;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
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

        ViewModelComponent viewModelComponent = DaggerViewModelComponent.builder()
                .personaFileModule(new PersonaFileModule(this))
                .persona5ApplicationComponent(Persona5Application.get(this).getComponent())
                .build();

        ActivityComponent component = DaggerActivityComponent.builder()
                .layoutModule(new LayoutModule(this))
                .activityContextModule(new ActivityContextModule(this))
                .viewModelComponent(viewModelComponent)

                .build();
        component.inject(this);

        SharedPreferences fusionSharedPreferences = getSharedPreferences(Persona5Application.getPersonaFusionSharedPrefName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fusionSharedPreferences.edit();
        editor.clear();
        editor.commit();

        if(!fusionSharedPreferences.contains("finished")){
            startService(new Intent(this, FusionCalculatorService.class));
        }

        setSupportActionBar(this.mainToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.persona_view);
        recyclerView.hasFixedSize();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //add divider for api >= 25
            mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    this.getResources().getConfiguration().orientation);
            recyclerView.addItemDecoration(mDividerItemDecoration);
        }

        this.filteredPersonas = Persona5Application.get(this).getAllPersonas();
        this.allPersonas = this.filteredPersonas;

        String test = fusionSharedPreferences.getString(allPersonas[0].name, null);
        String test2 = "";

        personaListAdapter = new PersonaListAdapter(this.filteredPersonas);
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

            this.filteredPersonas = viewModel.filterPersonas(this.filteredPersonas, query);
            personaListAdapter.setPersonas(MainActivity.this.filteredPersonas);
        }
        else if(Intent.ACTION_VIEW.equals(intent.getAction())){
            String personaName = intent.getDataString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.persona_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //restore all personas to list
                MainActivity.this.filteredPersonas = MainActivity.this.allPersonas;
                MainActivity.this.personaListAdapter.setPersonas(MainActivity.this.filteredPersonas);
                return true;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = getComponentName();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return super.onCreateOptionsMenu(menu);
    }
}

package com.example.rechee.persona5calculator.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.dagger.ActivityComponent;
import com.example.rechee.persona5calculator.dagger.ActivityContextModule;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.dagger.LayoutModule;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.dagger.ViewModelModule;
import com.example.rechee.persona5calculator.dagger.ViewModelRepositoryModule;
import com.example.rechee.persona5calculator.adapters.PersonaListAdapter;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.services.FusionCalculatorService;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;
import com.viethoa.RecyclerViewFastScroller;
import com.viethoa.models.AlphabetItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

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
    private RecyclerViewFastScroller fastScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityComponent component = Persona5Application.get(this).getComponent().plus(
                new LayoutModule(this),
                new ActivityContextModule(this),
                new ViewModelModule(),
                new ViewModelRepositoryModule(),
                new PersonaFileModule(this)
        );
        component.inject(this);
        this.component = component;

        SharedPreferences commonSharedPreferences = getSharedPreferences(PersonaUtilities.SHARED_PREF_COMMON,
                Context.MODE_PRIVATE);

        if(!commonSharedPreferences.contains("finished")){
            startService(new Intent(this, FusionCalculatorService.class));
        }

        setSupportActionBar(this.mainToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.persona_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //add divider for api >= 25
            mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    this.getResources().getConfiguration().orientation);
            recyclerView.addItemDecoration(mDividerItemDecoration);
        }

        this.filteredPersonas = viewModel.getAllPersonas();
        this.allPersonas = this.filteredPersonas;

        personaListAdapter = new PersonaListAdapter(this.filteredPersonas, viewModel);
        recyclerView.setAdapter(personaListAdapter);

        setUpAlphabetScroller();

        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void setUpAlphabetScroller(){
        
        fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recyclerView);

        ArrayList<AlphabetItem> mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < filteredPersonas.length; i++) {
            Persona persona = filteredPersonas[i];
            if (persona.name == null || persona.name.trim().isEmpty())
                continue;

            String word = persona.name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }

        fastScroller.setUpAlphabet(mAlphabetItems);
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
                //restore all personas to list when you click the x button on search
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

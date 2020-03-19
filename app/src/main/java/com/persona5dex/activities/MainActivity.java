package com.persona5dex.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.persona5dex.BuildConfig;
import com.persona5dex.Constants;
import com.persona5dex.R;
import com.persona5dex.fragments.FilterDialogFragment;
import com.persona5dex.fragments.PersonaListFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Enumerations.SearchResultType;
import com.persona5dex.models.GameType;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.repositories.MainPersonaRepository;
import com.persona5dex.services.FusionCalculatorJobService;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static android.app.SearchManager.EXTRA_DATA_KEY;
import static android.app.SearchManager.USER_QUERY;

public class MainActivity extends BaseActivity implements FilterDialogFragment.OnFilterListener {

    private static final String FILTER_DIALOG = "FILTER_DIALOG";
    public static final String PRIVACY_POLICY_SHOWN = "privacy_policy_shown";

    @Inject
    Toolbar mainToolbar;

    @Inject MainPersonaRepository repository;

    private PersonaFilterArgs latestFilterArgs;

    private int selectedSortMenuItemID;
    private PersonaListFragment personaListFragment;
    private GameType currentGameType;
    private Button switchGameButton;
    private TextView currentGameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        component.inject(this);

        //init crashlytics
        if(BuildConfig.ENABLE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        personaListFragment = (PersonaListFragment) fragmentManager.findFragmentById(R.id.fragment_persona_list);

        repository.getAllPersonasForMainListLiveData().observe(this, personas -> {
            personaListFragment.setPersonas(personas);
        });

        //sets default values for preferences only once in entire lifetime of application
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        FusionCalculatorJobService.enqueueWork(this, new Intent(this, FusionCalculatorJobService.class));

        setSupportActionBar(this.mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainToolbar.setLogo(R.drawable.ic_app_icon_fore);

        showPrivacyPolicy();

        currentGameTextView = findViewById(R.id.current_game_text_view);
        setCurrentGameString();
        setUpSwitchGameButton();

        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void setCurrentGameString() {
        @StringRes int switchButtonTextRes;
        if(currentGameType == GameType.BASE) {
            switchButtonTextRes = R.string.game_persona_5;
        } else {
            switchButtonTextRes = R.string.game_royal;
        }
        @StringRes final int currentGameRes = switchButtonTextRes;
        currentGameTextView.setText(getString(R.string.currently_viewing, getString(currentGameRes)));
    }

    private void setUpSwitchGameButton() {
        switchGameButton = findViewById(R.id.switch_game_button);

        final int gameTypeInt = defaultSharedPreferences.getInt(Constants.SHARED_PREF_KEY_GAME_TYPE, GameType.ROYAL.getValue());
        currentGameType = GameType.getGameType(gameTypeInt);

        setNewSwitchButtonText();
        switchGameButton.setOnClickListener(v -> {
            switchGames();
        });
    }

    private void switchGames() {
        currentGameType = currentGameType == GameType.BASE ? GameType.ROYAL : GameType.BASE;
        setCurrentGameString();
        setNewSwitchButtonText();
        defaultSharedPreferences.edit()
                .putInt(Constants.SHARED_PREF_KEY_GAME_TYPE, currentGameType.getValue())
                .apply();
        personaListFragment.filterPersonas(currentGameType);
        //todo: kick off new job
    }

    private void setNewSwitchButtonText() {
        @StringRes int switchButtonTextRes;
        if(currentGameType == GameType.BASE) {
            switchButtonTextRes = R.string.game_royal;
        } else {
            switchButtonTextRes = R.string.game_persona_5;
        }
        switchGameButton.setText(switchButtonTextRes);
    }

    private void showPrivacyPolicy() {
        boolean showedPrivacyPolicy = defaultSharedPreferences.getBoolean(PRIVACY_POLICY_SHOWN, false);
        if(!showedPrivacyPolicy) {

            String privacyPrompt = getString(R.string.privacy_prompt);
            privacyPrompt += getString(R.string.privacy_policy_url);

            TextView message = new TextView(this);

            SpannableString privacyPromptSpannable = new SpannableString(privacyPrompt);
            Linkify.addLinks(privacyPromptSpannable, Linkify.WEB_URLS);

            message.setText(privacyPromptSpannable);
            message.setMovementMethod(LinkMovementMethod.getInstance());

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.privacy)
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setView(message)
                    .create();

            final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
            edit.putBoolean(PRIVACY_POLICY_SHOWN, true);
            edit.apply();

            alertDialog.show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            logSearchQuery(query);

            personaListFragment.filterPersonas(query);
        } else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            String itemID = intent.getDataString();

            Bundle extras = intent.getExtras();
            if(extras != null) {
                String userQuery = extras.getString(USER_QUERY, null);
                String searchType = extras.getString(EXTRA_DATA_KEY, null);

                if(userQuery != null) {
                    logSearchQuery(userQuery);
                }

                if(searchType != null) {
                    int searchTypeAsInt = Integer.parseInt(searchType);
                    SearchResultType searchResultType = SearchResultType.getSearchResultType(searchTypeAsInt);

                    if(searchResultType == SearchResultType.PERSONA) {
                        Intent startDetailIntent = new Intent(this, PersonaDetailActivity.class);
                        startDetailIntent.putExtra("persona_id", Integer.parseInt(itemID));
                        startActivity(startDetailIntent);
                    } else {
                        Intent skillDetailIntent = new Intent(this, SkillDetailActivity.class);
                        skillDetailIntent.putExtra(PersonaSkillsFragment.SKILL_ID, Integer.parseInt(itemID));
                        startActivity(skillDetailIntent);
                    }
                }
            }
        }
    }

    private void logSearchQuery(String query) {
        if(BuildConfig.ENABLE_CRASHLYTICS) {
            Answers.getInstance().logSearch(new SearchEvent()
                    .putQuery(query));
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
                personaListFragment.filterPersonas("");

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

        switch(item.getItemId()) {
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

    private void showSortPopup() {
        View anchor = findViewById(R.id.action_search);
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.inflate(R.menu.sort_persona_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            MainActivity.this.selectedSortMenuItemID = item.getItemId();
            return handleSortClick();
        });

        popupMenu.show();
    }

    /**
     * Handles when the sort menu item is clicked. Will sort the current persona items and
     * set index bar visibility
     *
     * @return True if selected sort menu item matches one of menu item ids
     */
    private boolean handleSortClick() {
        switch(selectedSortMenuItemID) {
            case R.id.menu_sort_name_asc:
                personaListFragment.sortPersonasByName(true);
                return true;
            case R.id.menu_sort_name_desc:
                personaListFragment.sortPersonasByName(false);
                return true;
            case R.id.menu_sort_level_asc:
                personaListFragment.sortPersonasByLevel(true);
                return true;
            case R.id.menu_sort_level_desc:
                personaListFragment.sortPersonasByLevel(false);
                return true;
            case R.id.menu_sort_arcana_asc:
                personaListFragment.sortPersonasByArcana(true);
                return true;
            case R.id.menu_sort_arcana_desc:
                personaListFragment.sortPersonasByArcana(false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onFilterSelected(PersonaFilterArgs filterArgs) {
        this.latestFilterArgs = filterArgs;
        personaListFragment.filterPersonas(latestFilterArgs);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(latestFilterArgs == null) {
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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        latestFilterArgs = new PersonaFilterArgs();
        latestFilterArgs.rarePersona = savedInstanceState.getBoolean("filter_rarePersona");
        latestFilterArgs.dlcPersona = savedInstanceState.getBoolean("filter_dlcPersona");

        int arcanaValue = savedInstanceState.getInt("filter_selectedArcana");
        latestFilterArgs.arcana = Enumerations.Arcana.getArcana(arcanaValue);

        latestFilterArgs.minLevel = savedInstanceState.getInt("filter_minLevel");
        latestFilterArgs.maxLevel = savedInstanceState.getInt("filter_maxLevel");
        personaListFragment.filterPersonas(latestFilterArgs);

        selectedSortMenuItemID = savedInstanceState.getInt("sort_id");
        handleSortClick();
    }
}

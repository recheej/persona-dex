package com.persona5dex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.fragments.PersonaListFragment;
import com.persona5dex.fragments.PersonaListRepositoryType;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.models.GameType;
import com.persona5dex.models.PersonaRepository;
import com.persona5dex.models.room.Skill;
import com.persona5dex.repositories.PersonaListRepositoryFactory;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;
import com.persona5dex.viewmodels.PersonaListViewModelFactory;
import com.persona5dex.viewmodels.PersonaMainListViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class SkillDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    ArcanaNameProvider arcanaNameProvider;

    @Inject
    GameType gameType;

    @Inject
    PersonaListRepositoryFactory personaListRepositoryFactory;

    private PersonaDetailSkillsViewModel viewModel;
    private PersonaListFragment personaListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_detail);

        Intent intent = getIntent();
        final int skillID = intent.getIntExtra(PersonaSkillsFragment.SKILL_ID, 1);

        Persona5Application.get(this).getComponent()
                .activityComponent()
                .skillId(skillID)
                .activityContext(this).build()
                .inject(this);

        mainToolbar.setTitle(R.string.persona_skill);
        setSupportActionBar(mainToolbar);

        final TextView skillNameTextView = findViewById(R.id.textView_skillName);
        final TextView effectTextView = findViewById(R.id.textView_effect);
        final TextView elementTextView = findViewById(R.id.textView_Element);
        final TextView noteTextView = findViewById(R.id.textView_Note);
        final TextView costTextView = findViewById(R.id.textView_Cost);

        final PersonaRepository repository = personaListRepositoryFactory.getPersonaListRepository(PersonaListRepositoryType.SKILLS);
        final PersonaListViewModelFactory factory = new PersonaListViewModelFactory(arcanaNameProvider, gameType, repository);
        new ViewModelProvider(this, factory).get(PersonaMainListViewModel.class);

        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(PersonaDetailSkillsViewModel.class);
        this.viewModel.getSkill(skillID).observe(this, new Observer<Skill>() {
            @Override
            public void onChanged(@Nullable Skill skill) {

                String firstLetter = Character.toString(skill.element.charAt(0)).toUpperCase();
                skill.element = String.format("%s%s", firstLetter, skill.element.substring(1));

                skillNameTextView.setText(skill.name);
                effectTextView.setText(skill.effect);
                elementTextView.setText(skill.element);

                if(skill.note == null || skill.note.isEmpty()) {
                    View labelNote = findViewById(R.id.textView_note_label);
                    View star = findViewById(R.id.textView_star);

                    star.setVisibility(View.GONE);
                    labelNote.setVisibility(View.GONE);
                    noteTextView.setVisibility(View.GONE);
                } else {
                    noteTextView.setText(skill.note);
                }

                costTextView.setText(skill.costFriendly());
            }
        });

        configureFragment();
    }

    private void configureFragment() {
        personaListFragment = PersonaListFragment.newInstance(false);

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.persona_list, personaListFragment);
        fragmentTransaction.commit();
    }
}

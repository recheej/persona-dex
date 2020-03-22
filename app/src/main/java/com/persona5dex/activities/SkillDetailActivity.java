package com.persona5dex.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.persona5dex.R;
import com.persona5dex.fragments.PersonaListFragment;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.models.room.Skill;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class SkillDetailActivity extends BaseActivity {

    @Inject
    Toolbar mainToolbar;

    @Inject
    ViewModelFactory viewModelFactory;

    private PersonaDetailSkillsViewModel viewModel;
    private PersonaListFragment personaListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_detail);
        component.inject(this);

        mainToolbar.setTitle(R.string.persona_skill);
        setSupportActionBar(mainToolbar);

        final TextView skillNameTextView = findViewById(R.id.textView_skillName);
        final TextView effectTextView = findViewById(R.id.textView_effect);
        final TextView elementTextView = findViewById(R.id.textView_Element);
        final TextView noteTextView = findViewById(R.id.textView_Note);
        final TextView costTextView = findViewById(R.id.textView_Cost);

        Intent intent = getIntent();
        final int skillID = intent.getIntExtra(PersonaSkillsFragment.SKILL_ID, 1);

        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(PersonaDetailSkillsViewModel.class);
        this.viewModel.getSkill(skillID).observe(this, new Observer<Skill>() {
            @Override
            public void onChanged(@Nullable Skill skill) {

                String firstLetter = Character.toString(skill.element.charAt(0)).toUpperCase();
                skill.element = String.format("%s%s", firstLetter, skill.element.substring(1));

                skillNameTextView.setText(skill.name);
                effectTextView.setText(skill.effect);
                elementTextView.setText(skill.element);

                if(skill.note == null || skill.note.isEmpty()){
                    View labelNote = findViewById(R.id.textView_note_label);
                    View star = findViewById(R.id.textView_star);

                    star.setVisibility(View.GONE);
                    labelNote.setVisibility(View.GONE);
                    noteTextView.setVisibility(View.GONE);
                }
                else{
                    noteTextView.setText(skill.note);
                }

                costTextView.setText(skill.costFriendly());
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        personaListFragment = (PersonaListFragment) fragmentManager.findFragmentById(R.id.fragment_persona_list);
        personaListFragment.setIndexBarVisible(false);

        viewModel.getPersonasWithSkill(skillID)
                .observe(this, personas -> personaListFragment.setPersonas(personas));
    }
}

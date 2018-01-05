package com.persona5dex.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.dagger.viewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.fragments.PersonaSkillsFragment;
import com.persona5dex.models.room.Skill;
import com.persona5dex.viewmodels.PersonaDetailSkillsViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class SkillDetailActivity extends AppCompatActivity {


    @Inject
    ViewModelFactory viewModelFactory;

    private PersonaDetailSkillsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_detail);

        final TextView skillNameTextView = findViewById(R.id.textView_skillName);
        final TextView effectTextView = findViewById(R.id.textView_effect);
        final TextView elementTextView = findViewById(R.id.textView_Element);
        final TextView noteTextView = findViewById(R.id.textView_Note);
        final TextView costTextView = findViewById(R.id.textView_Cost);

        Persona5Application.get(this).getComponent()
                .viewModelComponent(new AndroidViewModelRepositoryModule())
                .inject(this);

        Intent intent = getIntent();
        final int skillID = intent.getIntExtra(PersonaSkillsFragment.SKILL_ID, 1);

        this.viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonaDetailSkillsViewModel.class);

        this.viewModel.setSkillID(skillID);
        this.viewModel.getSkill().observe(this, new Observer<Skill>() {
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

                costTextView.setText(Integer.toString(skill.cost));
            }
        });
    }
}

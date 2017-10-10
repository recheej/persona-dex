package com.persona5dex.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.persona5dex.R;
import com.persona5dex.dagger.FragmentComponent;
import com.persona5dex.models.Skill;
import com.persona5dex.viewmodels.PersonaSkillsViewModel;

import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonaSkillsFragment extends BaseFragment {

    @Inject
    PersonaSkillsViewModel viewModel;

    private Skill[] skills;

    public PersonaSkillsFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);

        this.skills = viewModel.getPersonaSkills();

        LinearLayout skillsGrid = baseView.findViewById(R.id.skill_grid);

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup container = activity.findViewById(R.id.view_pager);

        for(Skill personaSkill: this.skills){
            View personaSkillRow = inflater.inflate(R.layout.persona_skil_row, container, false);

            TextView textViewSkillName = personaSkillRow.findViewById(R.id.textViewSkillName);
            textViewSkillName.setText(personaSkill.getName());

            TextView textViewSkillLevel = personaSkillRow.findViewById(R.id.textViewSkillLevel);

            if(personaSkill.getLevel() == 0){
                textViewSkillLevel.setText("-");
            }
            else{
                textViewSkillLevel.setText(String.format(Locale.getDefault(), "%d", personaSkill.getLevel()));
            }

            View horizontalDivider = inflater.inflate(R.layout.grid_divider_horizontal, container, false);

            skillsGrid.addView(personaSkillRow);
            skillsGrid.addView(horizontalDivider);
        }

        skillsGrid.removeViewAt(skillsGrid.getChildCount() - 1); //remove hanging divider
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseView = inflater.inflate(R.layout.fragment_persona_skills, container, false);
        return baseView;
    }
}

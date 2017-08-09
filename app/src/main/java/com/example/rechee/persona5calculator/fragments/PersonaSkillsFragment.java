package com.example.rechee.persona5calculator.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.BaseActivity;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.Skill;
import com.example.rechee.persona5calculator.viewmodels.PersonaDetailViewModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonaSkillsFragment extends Fragment {

    private Persona detailPersona;

    @Inject
    PersonaDetailViewModel viewModel;

    private BaseActivity activity;
    private Skill[] skills;

    public PersonaSkillsFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = (BaseActivity) getActivity();
        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);

        this.detailPersona = viewModel.getDetailPersona();

        Skill[] personaSkills = this.detailPersona.getPersonaSkills();
        Arrays.sort(personaSkills, new Comparator<Skill>() {
            @Override
            public int compare(Skill o1, Skill o2) {
                if(o1.getLevel() < o2.getLevel()){
                    return -1;
                }

                if(o1.getLevel() > o2.getLevel()){
                    return 1;
                }

                //if the levels are equal, compare by name;
                return o1.getName().compareTo(o2.getName());
            }
        });

        this.skills = personaSkills;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_persona_skills, container, false);

        LinearLayout skillsGrid = (LinearLayout) view.findViewById(R.id.skill_grid);

        for(Skill personaSkill: this.skills){
            View personaSkillRow = inflater.inflate(R.layout.persona_skil_row, container, false);

            TextView textViewSkillName = (TextView) personaSkillRow.findViewById(R.id.textViewSkillName);
            textViewSkillName.setText(personaSkill.getName());

            TextView textViewSkillLevel = (TextView) personaSkillRow.findViewById(R.id.textViewSkillLevel);

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

        return view;
    }

}

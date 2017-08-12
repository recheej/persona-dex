package com.example.rechee.persona5calculator.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.PersonaDetailActivity;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListAdapter extends RecyclerView.Adapter<PersonaListAdapter.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewPersonaName;
        private TextView textViewPersonaLevel;
        private TextView textViewPersonaArcana;
        private PersonaListViewModel viewModel;
        private Persona bindedPersona;

        public ViewHolder(View itemView, PersonaListViewModel viewModel) {
            super(itemView);

            itemView.setOnClickListener(this);

            this.textViewPersonaName = (TextView) itemView.findViewById(R.id.textViewPersonaName);
            this.textViewPersonaLevel = (TextView) itemView.findViewById(R.id.textViewPersonaLevel);
            this.textViewPersonaArcana = (TextView) itemView.findViewById(R.id.textViewArcana);
            this.viewModel = viewModel;
        }

        public void bindPersona(Persona personaToBind){
            this.bindedPersona = personaToBind;
            this.textViewPersonaName.setText(personaToBind.name);
            this.textViewPersonaLevel.setText(Integer.toString(personaToBind.level));
            this.textViewPersonaArcana.setText(personaToBind.arcanaName);
        }

        @Override
        public void onClick(View v) {
            viewModel.storePersonaForDetail(bindedPersona);

            Context context = itemView.getContext();
            Intent startDetailIntent = new Intent(context, PersonaDetailActivity.class);
            context.startActivity(startDetailIntent);
        }
    }

    private List<Persona> personas;
    private final PersonaListViewModel viewModel;

    public PersonaListAdapter(Persona[] personas, PersonaListViewModel viewModel){
        this.personas = new ArrayList<>(personas.length);
        Collections.addAll(this.personas, personas);
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_persona_item, parent, false);

        return new ViewHolder(view, viewModel);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindPersona(this.personas.get(position));
    }

    @Override
    public int getItemCount() {
        return this.personas.size();
    }

    public void setPersonas(Persona[] newPersonas){
        this.personas.clear();
        Collections.addAll(this.personas, newPersonas);
        this.notifyDataSetChanged();
    }
}

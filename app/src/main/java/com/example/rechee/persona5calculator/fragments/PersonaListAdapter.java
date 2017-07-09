package com.example.rechee.persona5calculator.fragments;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.viewmodels.PersonaListViewModel;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListAdapter extends RecyclerView.Adapter<PersonaListAdapter.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPersonaName;
        private TextView textViewPersonaLevel;
        private TextView textViewPersonaArcana;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewPersonaName = (TextView) itemView.findViewById(R.id.textViewPersonaName);
            this.textViewPersonaLevel = (TextView) itemView.findViewById(R.id.textViewPersonaLevel);
            this.textViewPersonaArcana = (TextView) itemView.findViewById(R.id.textViewArcana);
        }

        public void bindPersona(Persona personaToBind){
            this.textViewPersonaName.setText(personaToBind.name);
            this.textViewPersonaLevel.setText(Integer.toString(personaToBind.level));
            this.textViewPersonaArcana.setText(personaToBind.arcanaName);
        }
    }

    private Persona[] personas;

    public PersonaListAdapter(Persona[] personas){
        this.personas = personas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_persona_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindPersona(this.personas[position]);
    }

    @Override
    public int getItemCount() {
        return this.personas.length;
    }

    public void setPersonas(Persona[] newPersonas){
        this.personas = newPersonas;
        this.notifyDataSetChanged();
    }
}

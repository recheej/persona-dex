package com.example.rechee.persona5calculator.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.models.PersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListAdapter extends RecyclerView.Adapter<PersonaFusionListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        private final boolean isToList;
        private TextView textViewPersonaNameOne;
        private TextView textViewPersonaNameTwo;


        ViewHolder(View itemView, boolean isToList) {
            super(itemView);

            this.isToList = isToList;
            this.textViewPersonaNameOne = (TextView) itemView.findViewById(R.id.textView_personaNameOne);
            this.textViewPersonaNameTwo = (TextView) itemView.findViewById(R.id.textView_personaNameTwo);
        }

        void bind(PersonaEdge edge){
            if(edge != null && edge.start != null){
                if(isToList){
                    this.textViewPersonaNameOne.setText(edge.start);
                    this.textViewPersonaNameTwo.setText(edge.pairPersona);
                }
                else{
                    this.textViewPersonaNameOne.setText(edge.pairPersona);
                    this.textViewPersonaNameTwo.setText(edge.end);
                }
            }
            else{
                this.textViewPersonaNameOne.setText("-");
                this.textViewPersonaNameTwo.setText("-");
            }
        }
    }

    private final PersonaEdge[] edges;
    private final String personaName;
    private final boolean isToList;


    public PersonaFusionListAdapter(PersonaEdge[] edges, String personaName, boolean isToList){
        this.edges = edges;
        this.personaName = personaName;
        this.isToList = isToList;
    }

    @Override
    public PersonaFusionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_personafusion, parent, false);

        return new ViewHolder(view, isToList);
    }

    @Override
    public void onBindViewHolder(PersonaFusionListAdapter.ViewHolder holder, int position) {
        holder.bind(edges[position]);
    }

    @Override
    public int getItemCount() {
        return this.edges.length;
    }
}

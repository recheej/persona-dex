package com.persona5dex.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.persona5dex.R;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Persona;
import com.persona5dex.viewmodels.PersonaListViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListAdapter extends RecyclerView.Adapter<PersonaListAdapter.ViewHolder> implements SectionIndexer {

    private List<Persona> personas;
    private final PersonaListViewModel viewModel;
    private ArrayList<Integer> mSectionPositions;
    private boolean sectionAsc;

    public enum IndexerType {
        PersonaName,
        ArcanaName
    }

    private IndexerType sectionIndexerType;

    public static class ViewHolder extends RecyclerView.ViewHolder{
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

    public PersonaListAdapter(Persona[] personas, PersonaListViewModel viewModel){
        this.personas = new ArrayList<>(personas.length);
        Collections.addAll(this.personas, personas);
        this.viewModel = viewModel;
        this.sectionIndexerType = IndexerType.PersonaName;
        this.sectionAsc = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_persona_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Persona detailPersona = personas.get(viewHolder.getAdapterPosition());
                viewModel.storePersonaForDetail(detailPersona);

                Context context = v.getContext();
                Intent startDetailIntent = new Intent(context, PersonaDetailActivity.class);
                context.startActivity(startDetailIntent);
            }
        });

        return viewHolder;
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

    /**
     * Section Indexer Methods
     */

    @Override
    public Object[] getSections() {
        switch (sectionIndexerType){
            case PersonaName:
                return getPersonaNameSections();
            case ArcanaName:
                return this.getArcanaNameSections();
            default:
                return new Object[0];
        }
    }

    private Object[] getArcanaNameSections() {
        int arcanaSize = Enumerations.Arcana.values().length;
        List<String> sections = new ArrayList<>(arcanaSize);
        mSectionPositions = new ArrayList<>(arcanaSize);
        for (int i = 0; i < personas.size(); i++) {
            String section = personas.get(i).arcanaName.toUpperCase();
            if(section.length() > 5){
                section = section.substring(0, 4);
            }

            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }

        return sections.toArray(new String[0]);
    }

    private Object[] getPersonaNameSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = personas.size(); i < size; i++) {
            String section = String.valueOf(personas.get(i).name.charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public void setIndexerType(IndexerType indexerType){
        this.sectionIndexerType = indexerType;
    }
}

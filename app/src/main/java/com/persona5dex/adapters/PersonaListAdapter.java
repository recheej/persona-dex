package com.persona5dex.adapters;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.persona5dex.R;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.GameType;
import com.persona5dex.models.MainListPersona;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaListAdapter extends RecyclerView.Adapter<PersonaListAdapter.ViewHolder> implements SectionIndexer {

    private List<MainListPersona> personas;
    private ArrayList<Integer> mSectionPositions;
    private boolean sectionAsc;

    public enum IndexerType {
        PersonaName,
        ArcanaName
    }

    private IndexerType sectionIndexerType;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPersonaName;
        private TextView textViewPrimaryShadowName;
        private TextView textViewPersonaLevel;
        private TextView textViewPersonaArcana;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewPersonaName = itemView.findViewById(R.id.textViewPersonaName);
            this.textViewPrimaryShadowName = itemView.findViewById(R.id.textViewPrimaryShadowName);
            this.textViewPersonaLevel = itemView.findViewById(R.id.textViewPersonaLevel);
            this.textViewPersonaArcana = itemView.findViewById(R.id.textViewArcana);
        }

        public void bindPersona(MainListPersona personaToBind){
            this.textViewPersonaName.setText(personaToBind.name);
            this.textViewPersonaLevel.setText(String.format(Locale.getDefault(), "%d" , personaToBind.level));
            this.textViewPersonaArcana.setText(personaToBind.arcanaName);


            @ColorRes final int textColor = getTextColor(personaToBind, );
            @ColorInt final int color = itemView.getContext().getResources().getColor(textColor);
            this.textViewPersonaName.setTextColor(color);
            this.textViewPersonaLevel.setTextColor(color);
            this.textViewPersonaArcana.setTextColor(color);
        }

        @ColorRes private int getTextColor(MainListPersona personaToBind, boolean primary) {
            @ColorRes int backgroundColor;
            if(personaToBind.gameId == GameType.ROYAL){
                if(primary){
                    backgroundColor = R.color.textColorGoldenOnDark;
                }
                else{

                }
            }
            else{
                if(primary){
                    backgroundColor = android.R.color.pri;
                }
                else{

                }
            }

            return backgroundColor;
        }

        private void getDefaultTextColor(boolean primary, Context context){
            TypedValue typedValue = new TypedValue();
            Theme theme = context.theme
            theme.resolveAttribute(R.attr.theme_color, typedValue, true);
            @ColorInt int color = typedValue.data;
        }
    }

    public PersonaListAdapter(List<MainListPersona> mainListPersonas){
        this.personas = mainListPersonas;
        this.sectionIndexerType = IndexerType.PersonaName;
        this.sectionAsc = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_persona_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(v -> {
            int personaPosition = viewHolder.getAdapterPosition();
            if(personaPosition == RecyclerView.NO_POSITION){
                personaPosition = 0;
            }

            MainListPersona detailPersona = personas.get(personaPosition);

            Context context = v.getContext();
            Intent startDetailIntent = new Intent(context, PersonaDetailActivity.class);
            startDetailIntent.putExtra("persona_id", detailPersona.id);
            context.startActivity(startDetailIntent);
        });

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        MainListPersona detailPersona = personas.get(position);
        return detailPersona.gameId.getValue();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindPersona(this.personas.get(position));
    }

    @Override
    public int getItemCount() {
        return this.personas.size();
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

package com.persona5dex.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.persona5dex.R;
import com.persona5dex.activities.PersonaDetailActivity;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.viewmodels.PersonaFusionListViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListAdapter extends RecyclerView.Adapter<PersonaFusionListAdapter.ViewHolder> {

    private final RecyclerView recyclerView;

    private final List<PersonaEdgeDisplay> edges;
    private PersonaEdgeDisplay selectedPersonaEdge;
    private int selectedPosition = -1;


    class ViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {
        private final ExpandableLayout expandableLayout;
        private TextView textViewPersonaNameOne;
        private TextView textViewPersonaNameTwo;
        private ImageView expandImage;

        private TextView textViewPersonaOneDetail;
        private TextView textViewPersonaTwoDetail;

        private PersonaEdgeDisplay personaEdge;

        ViewHolder(final View itemView) {
            super(itemView);

            this.expandImage = itemView.findViewById(R.id.expand_icon);

            this.expandableLayout = itemView.findViewById(R.id.expandable_fusion);
            this.expandableLayout.setInterpolator(new OvershootInterpolator());
            this.expandableLayout.setOnExpansionUpdateListener(this);
        }

        private void goToPersonaDetail(int personaID){
            Intent startDetailIntent = new Intent(itemView.getContext(), PersonaDetailActivity.class);
            startDetailIntent.putExtra("persona_id", 0);
            itemView.getContext().startActivity(startDetailIntent);
        }

        ExpandableLayout expandableLayout() {
            return this.expandableLayout;
        }

        PersonaEdgeDisplay personaEdge() {
            return this.personaEdge;
        }

        ImageView expandImage() {
            return this.expandImage;
        }

        void bind(final PersonaEdgeDisplay edge){

            this.textViewPersonaNameOne = itemView.findViewById(R.id.textView_personaNameOne);
            this.textViewPersonaNameTwo = itemView.findViewById(R.id.textView_personaNameTwo);
            this.textViewPersonaOneDetail = itemView.findViewById(R.id.persona_one_detail);
            this.textViewPersonaTwoDetail = itemView.findViewById(R.id.persona_two_detail);

            this.textViewPersonaNameOne.setText(edge.leftPersonaName);
            this.textViewPersonaNameTwo.setText(edge.rightPersonaName);

            this.textViewPersonaOneDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPersonaDetail(edge.leftPersonaID);
                }
            });

            this.textViewPersonaTwoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPersonaDetail(edge.rightPersonaID);
                }
            });

            String detailsFor = itemView.getContext().getString(R.string.details_for);

            this.textViewPersonaOneDetail.setText(detailsFor + ": " + edge.leftPersonaName);
            this.textViewPersonaTwoDetail.setText(detailsFor + ": " + edge.rightPersonaName);

            personaEdge = edge;
            expandableLayout.collapse(false);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            int scrollPosition = getAdapterPosition();
            if(scrollPosition == RecyclerView.NO_POSITION){
                scrollPosition = 0;
            }

            recyclerView.smoothScrollToPosition(scrollPosition);
        }
    }

    public PersonaFusionListAdapter(List<PersonaEdgeDisplay> edges, RecyclerView recyclerView){
        this.edges = edges;
        this.recyclerView = recyclerView;
    }

    @Override
    public PersonaFusionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_personafusion, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PersonaFusionListAdapter.ViewHolder holder, final int position) {
        final int edgePosition = holder.getAdapterPosition() == RecyclerView.NO_POSITION ? 0 : holder.getAdapterPosition();
        holder.bind(edges.get(edgePosition));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view clicked let's expand

                ViewHolder selectedViewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedPosition);
                if(selectedViewHolder != null){
                    selectedViewHolder.expandableLayout().collapse();
                    selectedViewHolder.expandImage().setImageResource(R.drawable.ic_expand_more_white_24dp);
                }

                if(holder.personaEdge() == selectedPersonaEdge){
                    selectedPersonaEdge = null;
                    selectedPosition = -1;
                    holder.expandImage().setImageResource(R.drawable.ic_expand_more_white_24dp);
                }
                else{
                    holder.expandableLayout().expand();
                    selectedPersonaEdge = edges.get(edgePosition);
                    selectedPosition = edgePosition;
                    holder.expandImage().setImageResource(R.drawable.ic_expand_less_white_24dp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.edges.size();
    }
}

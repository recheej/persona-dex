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
import com.persona5dex.models.RawPersonaEdge;
import com.persona5dex.viewmodels.PersonaFusionListViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListAdapter extends RecyclerView.Adapter<PersonaFusionListAdapter.ViewHolder> {

    private final RecyclerView recyclerView;

    private final PersonaEdgeDisplay[] edges;
    private final int personaID;
    private final boolean isToList;
    private final PersonaFusionListViewModel viewModel;
    private PersonaEdgeDisplay selectedPersonaEdge;
    private int selectedPosition = -1;


    class ViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {
        private final boolean isToList;
        private final ExpandableLayout expandableLayout;
        private TextView textViewPersonaNameOne;
        private TextView textViewPersonaNameTwo;
        private ImageView expandImage;

        private TextView textViewPersonaOneDetail;
        private TextView textViewPersonaTwoDetail;

        private PersonaEdgeDisplay personaEdge;

        ViewHolder(final View itemView, boolean isToList) {
            super(itemView);

            this.isToList = isToList;
            this.textViewPersonaNameOne = (TextView) itemView.findViewById(R.id.textView_personaNameOne);
            this.textViewPersonaNameTwo = (TextView) itemView.findViewById(R.id.textView_personaNameTwo);
            this.textViewPersonaOneDetail = (TextView) itemView.findViewById(R.id.persona_one_detail);
            this.textViewPersonaTwoDetail = (TextView) itemView.findViewById(R.id.persona_two_detail);

            this.textViewPersonaOneDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPersonaDetail(textViewPersonaNameOne.getText().toString());
                }
            });

            this.textViewPersonaTwoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPersonaDetail(textViewPersonaNameTwo.getText().toString());
                }
            });

            this.expandImage = (ImageView) itemView.findViewById(R.id.expand_icon);

            this.expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_fusion);
            this.expandableLayout.setInterpolator(new OvershootInterpolator());
            this.expandableLayout.setOnExpansionUpdateListener(this);
        }

        private void goToPersonaDetail(String personaName){
            viewModel.storePersonaForDetail(personaName);

            Intent startDetailIntent = new Intent(itemView.getContext(), PersonaDetailActivity.class);
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

        void bind(PersonaEdgeDisplay edge){

            this.textViewPersonaNameOne.setText(edge.left);
            this.textViewPersonaNameTwo.setText(edge.right);

            String detailsFor = itemView.getContext().getString(R.string.details_for);

            this.textViewPersonaOneDetail.setText(detailsFor + ": " + edge.left);
            this.textViewPersonaTwoDetail.setText(detailsFor + ": " + edge.right);

            personaEdge = edge;
            expandableLayout.collapse(false);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            recyclerView.smoothScrollToPosition(getAdapterPosition());
        }
    }

    public PersonaFusionListAdapter(PersonaEdgeDisplay[] edges, int personaID, boolean isToList, RecyclerView recyclerView, PersonaFusionListViewModel viewModel){
        this.edges = edges;
        this.personaID = personaID;
        this.isToList = isToList;
        this.recyclerView = recyclerView;
        this.viewModel = viewModel;
    }

    @Override
    public PersonaFusionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_personafusion, parent, false);

        return new ViewHolder(view, isToList);
    }

    @Override
    public void onBindViewHolder(final PersonaFusionListAdapter.ViewHolder holder, final int position) {
        final int edgePosition = holder.getAdapterPosition();
        holder.bind(edges[edgePosition]);

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
                    selectedPersonaEdge = edges[edgePosition];
                    selectedPosition = edgePosition;
                    holder.expandImage().setImageResource(R.drawable.ic_expand_less_white_24dp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.edges.length;
    }
}

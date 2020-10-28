package com.example.diplom1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HandbookRecyclerViewAdapter extends RecyclerView.Adapter<HandbookRecyclerViewAdapter.MyViewHolder> implements Filterable {

    private List<String> diseaseList = new ArrayList<>();
    private List<String> diseaseListFull = new ArrayList<>();
    private OnNoteListener mOnNoteListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView diseaseName;
        OnNoteListener onNoteListener;

        public MyViewHolder(View v, OnNoteListener onNoteListener) {
            super(v);
            diseaseName = v.findViewById(R.id.my_text_view);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public HandbookRecyclerViewAdapter(List<String> myDataset, OnNoteListener onNoteListener) {
        diseaseList = myDataset;
        diseaseListFull = new ArrayList<>(myDataset);
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public HandbookRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.handbook_recycler_view_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v, mOnNoteListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.diseaseName.setText(diseaseList.get(position));
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    @Override
    public Filter getFilter(){
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(diseaseListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (int i = 0; i < diseaseListFull.size(); i++){
                    if (diseaseListFull.get(i).toLowerCase().contains(filterPattern)){
                        filteredList.add(diseaseListFull.get(i));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            diseaseList.clear();
            diseaseList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}

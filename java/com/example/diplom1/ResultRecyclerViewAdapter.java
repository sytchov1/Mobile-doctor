package com.example.diplom1;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.MyViewHolder> {

    private List<String> diseaseList = new ArrayList<>();
    private List<Integer> percentList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    String str;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView diseaseName;
        public TextView diseasePercent;
        OnNoteListener onNoteListener;

        public MyViewHolder(View v, OnNoteListener onNoteListener) {
            super(v);
            diseaseName = v.findViewById(R.id.card_title);
            diseasePercent = v.findViewById(R.id.card_text2);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){ onNoteListener.onNoteClick(getAdapterPosition());}

    }

    public ResultRecyclerViewAdapter(List<String> diseases, List<Integer> percent, OnNoteListener onNoteListener) {
        diseaseList = diseases;
        percentList = percent;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public ResultRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v, mOnNoteListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.diseaseName.setText(diseaseList.get(position));
        if (percentList.get(position) < 1){
            holder.diseasePercent.setTextColor(Color.parseColor("#1E90FF"));
            str = "очень низкая";
        }
        else if (percentList.get(position) < 10){
            holder.diseasePercent.setTextColor(Color.parseColor("#007400"));
            str = "низкая";
        }
        else if (percentList.get(position) < 33){
            holder.diseasePercent.setTextColor(Color.parseColor("#FFA500"));
            str = "средняя";
        }
        else {
            holder.diseasePercent.setTextColor(Color.parseColor("#9B0000"));
            str = "высокая";
        }
        holder.diseasePercent.setText(str);
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}

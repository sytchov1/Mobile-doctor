package com.example.diplom1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DiagnosticRecyclerViewAdapter extends RecyclerView.Adapter<DiagnosticRecyclerViewAdapter.MyViewHolder>{

    private List<String> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.my_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    Log.i("Click", "on item click");
                }
            });
        }
    }


    public DiagnosticRecyclerViewAdapter(List<String> myDataset) {
    mDataset = myDataset;
}

@Override public DiagnosticRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnostic_recycler_view_item, parent, false);
    MyViewHolder vh = new MyViewHolder(v);
    return vh;
}

@Override public void onBindViewHolder(MyViewHolder holder, int position) {
    holder.mTextView.setText(mDataset.get(position));
}

@Override public int getItemCount() {
    return mDataset.size();
}
}
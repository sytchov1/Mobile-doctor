package com.example.diplom1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ResultFragment extends Fragment implements ResultRecyclerViewAdapter.OnNoteListener{

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myAdapter;
    private List<String> recyclerData1 = new ArrayList<>();
    private List<Integer> recyclerData2 = new ArrayList<>();
    private String[] strings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Результат");
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strings = bundle.getStringArray("d");
            recyclerData2 = bundle.getIntegerArrayList("p");
        }
        for (int i =0; i < strings.length; i++){
            recyclerData1.add(strings[i]);
        }
        myRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler_view);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(view.getContext());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new ResultRecyclerViewAdapter(recyclerData1, recyclerData2, this);
        myRecyclerView.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onNoteClick(int position){
        ((MainActivity)getActivity()).openDisease(recyclerData1.get(position));
    }
}

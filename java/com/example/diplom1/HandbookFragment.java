package com.example.diplom1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class HandbookFragment extends Fragment implements HandbookRecyclerViewAdapter.OnNoteListener {

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private HandbookRecyclerViewAdapter myAdapter;
    private List<String> recyclerData = new ArrayList<>();
    private SearchView searchView;
    DBHelper dbHelper;
    String[] col = {"name"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Справочник");
        View view = inflater.inflate(R.layout.handbook_fragment, container, false);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(view.getContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        if (recyclerData.isEmpty()){
            dbHelper = new DBHelper(getContext(), "mybd", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query("disease", col, null, null, null,
                    null, null);
            cursor.moveToFirst();
            int field1ColIndex = cursor.getColumnIndex("name");
            do {
                recyclerData.add(cursor.getString(field1ColIndex));
            } while (cursor.moveToNext());
            cursor.close();
        }
        myAdapter = new HandbookRecyclerViewAdapter(recyclerData, this);
        myRecyclerView.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        searchView = (SearchView) getActivity().findViewById(R.id.searchView1);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()   {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onNoteClick(int position){
        ((MainActivity)getActivity()).openDisease(recyclerData.get(position));
    }
}

package com.example.diplom1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;


public class ChooseFragment extends Fragment {

    ExpandableListView elvMain;
    ExpandableListViewAdapter ah;
    SimpleExpandableListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Выберите симптом");
         final View view = inflater.inflate(R.layout.choose_fragment1, container, false);
        ah = new ExpandableListViewAdapter(view.getContext());
        adapter = ah.getAdapter();

        elvMain = (ExpandableListView) view.findViewById(R.id.elvMain);
        elvMain.setAdapter(adapter);

        // нажатие на элемент
        elvMain.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition,   int childPosition, long id) {
                Log.i("Click", "onChildClick groupPosition = " + groupPosition +
                        " childPosition = " + childPosition +
                        " id = " + id);
                String s = ah.getChildText(groupPosition, childPosition);
                Log.i("Click", "" + s);
                ((MainActivity)getActivity()).addSymp(s);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return false;
            }
        });

        // нажатие на группу
        elvMain.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.i("Click", "onGroupClick groupPosition = " + groupPosition +
                        " id = " + id);
                return false;
            }
        });

        // сворачивание группы
        elvMain.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                Log.i("Click", "onGroupCollapse groupPosition = " + groupPosition);
            }
        });

        // разворачивание группы
        elvMain.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                Log.i("Click", "onGroupExpand groupPosition = " + groupPosition);
            }
        });
        return view;
    }
}
package com.example.diplom1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DiseaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Справочник");
        return inflater.inflate(R.layout.disease_fragment, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        TextView textViewName = getActivity().findViewById(R.id.textView4);
        TextView textViewDescription = getActivity().findViewById(R.id.textView6);
        TextView textViewRecommendations = getActivity().findViewById(R.id.textView8);
        Button findDoctorButton = getActivity().findViewById(R.id.buttonFindDoctor);
        findDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).findDoctor();
            }

        });
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            textViewName.setText(bundle.getString("n"));
            textViewDescription.setText(bundle.getString("d"));
            textViewRecommendations.setText(bundle.getString("r"));
        }
    }
}

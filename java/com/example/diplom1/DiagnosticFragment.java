package com.example.diplom1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticFragment extends Fragment {

    private SeekBar seekBar;
    private DiagnosticAsyncTask asyncTask;
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myAdapter;
    private List<String> recyclerData = new ArrayList<>();
    private List<String> gender = new ArrayList<>();
    public String age;
    private String[] gervar = new String[]{"male", "female"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Диагностика");
        View view = inflater.inflate(R.layout.diagnostic_fragment, container, false);
        final TextView textViewAge = view.findViewById(R.id.textViewAgeCount);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewAge.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        myRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(view.getContext());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new DiagnosticRecyclerViewAdapter(recyclerData);
        myRecyclerView.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Button buttonAdd = (Button) getActivity().findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseFragment chooseFragment = new ChooseFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, chooseFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button buttonClear = (Button) getActivity().findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            recyclerData.clear();
            myAdapter.notifyDataSetChanged();
            }

        });

        Button buttonDiagnostic = (Button) getActivity().findViewById(R.id.buttonDiagnostic);
        buttonDiagnostic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recyclerData.isEmpty()){
                    gender.clear();
                    age = String.valueOf(seekBar.getProgress());
                    RadioGroup radioGroup = getActivity().findViewById(R.id.radioGroup);
                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale){
                        gender.add(gervar[0]);
                    } else gender.add(gervar[1]);
                    gender.add(age);
                    asyncTask = new DiagnosticAsyncTask();
                    asyncTask.execute(recyclerData, gender);
                }
                else {
                    Toast toast = Toast.makeText(getContext(),
                            "Пожалуйста, укажите симптомы",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(22);
                    messageTextView.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });
    }

    public void upgradeList(String s){
        recyclerData.add(s);
        myAdapter.notifyDataSetChanged();
    }

    public class DiagnosticAsyncTask extends AsyncTask<List<String>, Integer, Void> {
        private DBHelper dbHelper;
        String col[] = {"id"};
        String selection = "name = ?";
        List<String> suspectedDiseaseList = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        protected Void doInBackground(List<String>... arg) {
            dbHelper = new DBHelper(getContext(), "mybd", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<String> symptomList = new ArrayList<>();
            List<String> symptomIDList = new ArrayList<>();
            List<Double> prevalenceOfDisease = new ArrayList<>();
            List<Double> chisliteli = new ArrayList<>();
            double chisl, znamen = 0, result;
            symptomList = arg[0];
            boolean flag;

            for (int i = 0; i < symptomList.size(); i++) {

                String[] selArgs = new String[1];
                selArgs[0] = symptomList.get(i);
                Cursor cursor = db.query("symptom", col, selection, selArgs, null,
                        null, null);
                cursor.moveToFirst();
                int field1ColIndex = cursor.getColumnIndex("id");
                do {
                    Log.i("MyAsyncTask", "Id = " + cursor.getString(field1ColIndex));
                    symptomIDList.add(cursor.getString(field1ColIndex));
                } while (cursor.moveToNext());
                cursor.close();
            }

            String table = "disease as DI inner join symptom_of_disease as SOD on DI.id = SOD.id_disease";
            String col[] = {"DI.name as Name", "DI.gender as Gender", "DI.prevalence3 as Prevalence3", "DI.prevalence12 as Prevalence12",
                    "DI.prevalence21 as Prevalence21", "DI.prevalence40 as Prevalence40",
                    "DI.prevalence60 as Prevalence60", "DI.prevalence120 as Prevalence120"};
            String selection = "id_symptom = ?";

            for (int i = 0; i < symptomIDList.size(); i++) {

                String[] selArgs = new String[1];
                selArgs[0] = symptomIDList.get(i);
                Cursor cursor = db.query(table, col, selection, selArgs, null,
                        null, null);
                cursor.moveToFirst();
                int field1ColIndex = cursor.getColumnIndex("Name");
                int field1ColIndex2 = cursor.getColumnIndex("Gender");
                int field1ColIndex3;
                if (Integer.valueOf(arg[1].get(1)) <= 3){
                    field1ColIndex3 = cursor.getColumnIndex("Prevalence3");}
                else if (Integer.valueOf(arg[1].get(1)) <= 12){
                    field1ColIndex3 = cursor.getColumnIndex("Prevalence12");}
                else if (Integer.valueOf(arg[1].get(1)) <= 21){
                    field1ColIndex3 = cursor.getColumnIndex("Prevalence21");}
                else if (Integer.valueOf(arg[1].get(1)) <= 40){
                    field1ColIndex3 = cursor.getColumnIndex("Prevalence40");}
                else if (Integer.valueOf(arg[1].get(1)) <= 60){
                    field1ColIndex3 = cursor.getColumnIndex("Prevalence60");}
                else field1ColIndex3 = cursor.getColumnIndex("Prevalence120");
                do {
                    if (cursor.getString(field1ColIndex2).equals("both") || cursor.getString(field1ColIndex2).equals(arg[1].get(0))){
                        if (!suspectedDiseaseList.isEmpty()){
                            flag = false;
                            for (int j = 0; j < suspectedDiseaseList.size(); j++){
                                if (cursor.getString(field1ColIndex).equals(suspectedDiseaseList.get(j))){
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag){
                                if (cursor.getDouble(field1ColIndex3) != 0){
                                    suspectedDiseaseList.add(cursor.getString(field1ColIndex));
                                    prevalenceOfDisease.add(cursor.getDouble(field1ColIndex3));
                                }
                            }
                        } else {
                            if (cursor.getDouble(field1ColIndex3) != 0){
                                suspectedDiseaseList.add(cursor.getString(field1ColIndex));
                                prevalenceOfDisease.add(cursor.getDouble(field1ColIndex3));
                            }
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
            for (int p = 0; p < suspectedDiseaseList.size(); p++){
                Log.i("MyAsyncTask", "Предполагаемая болезнь: " + suspectedDiseaseList.get(p) + " " + prevalenceOfDisease.get(p));
            }

            String col2[] = {"SOD.id_symptom as ID", "SOD.occurence as Occurence"};
            for (int i = 0; i < suspectedDiseaseList.size(); i++){
                chisl = 0;
                Cursor cursor = db.query(table, col2, null, null, null,
                        null, null);
                cursor.moveToFirst();
                int field1ColIndex = cursor.getColumnIndex("ID");
                int field1ColIndex2 = cursor.getColumnIndex("Occurence");
                do {
                    for (int j = 0; j < symptomIDList.size(); j++){
                        if (cursor.getString(field1ColIndex).equals(symptomIDList.get(j))){
                            chisl = chisl + (cursor.getDouble(field1ColIndex2))/100;
                        }
                    }
                } while (cursor.moveToNext());
                chisl = chisl * prevalenceOfDisease.get(i);
                chisliteli.add(chisl);
            }

            for (int p = 0; p < chisliteli.size(); p++){
                Log.i("MyAsyncTask", "Числитель: " + chisliteli.get(p));
            }

            for (int i = 0; i < chisliteli.size(); i++){
                znamen = znamen + chisliteli.get(i);
            }

            for (int i = 0; i < chisliteli.size(); i++){
                result = chisliteli.get(i)/znamen;
                result = Math.round(result*100000)/1000.0d;
                result = Math.round(result);
                results.add((int) result);
            }

            for (int p = 0; p < results.size(); p++){
                Log.i("MyAsyncTask", "Результаты: " + results.get(p));
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            Log.i("MyAsyncTask", "Progress is " + values[0]);
        }

        protected void onPostExecute(Void result) {
            if (results.isEmpty()){
                Toast toast = Toast.makeText(getContext(),
                        "Не удалось определить болезнь. Попробуйте указать больше симптомов",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(22);
                messageTextView.setGravity(Gravity.CENTER);
                toast.show();
            }
            else ((MainActivity)getActivity()).createResult(suspectedDiseaseList, results);
        }
    }
}

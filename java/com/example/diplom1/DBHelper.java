package com.example.diplom1;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    ContentValues cv;
    private final Context fContext;

    double[] prevalence3 = {0.5,50,200,0,0.1,0.01,0,11,0,1,0,0,6,0,100,100,0,0,1.2,395,20,100,1003,150,3.5,0,0.1,7,0,4,0,20,100,1,0,0,1.5,0,0,0,0,50,3400,50,0};
    double[] prevalence12 = {1,50,1000,2000,0.5,0.1,400,21,0,5,856,0,6,2,100,1312,56,0,2.1,563,20,80,2863,300,7.5,9.5,0.4,14,36.5,19,121,5,210,375,0.1,10,7,1,0.1,0,130,10,15810,50,2};
    double[] prevalence21 = {2.5,50,3000,12500,8.5,8,1500,30,0.1,450,4301,10,3,12.5,50,2005,300,112.5,3.2,917,20,400,3561,4288,130,102.5,1.6,312,82,180,98,1,199,1500,90,500,6.5,8,0.5,1000,308,1,25560,45,100};
    double[] prevalence40 = {12.5,50,5000,15000,8.5,8,3500,30,1.5,450,4301,1000,3,12.5,50,2005,862,112.5,3.2,917,20,400,3561,4288,130,102.5,1.6,312,82,180,98,1,199,3750,90,2250,6.5,8,0.5,1000,308,1,27821,45,100};
    double[] prevalence60 = {600,50,5000,15000,12.5,10,4500,30,3,320,3137.5,5000,1,15,75,1876,890,1250,3.2,889.5,20,700,3561,4288,142,120,0.3,456,130,324,87,0,233,3750,130,5000,14,8.1,1,500,323,0,29003,20,100};
    double[] prevalence120 = {6000,50,3000,15000,12.5,10,7500,30,7,20,1913,8500,0,16,100,1395,950,3500,3.5,727,20,500,3561,4288,182.5,130,0.2,268,69.5,348,90,0,214,1500,195,10000,21.5,3,0.7,250,339,0,36102,10,100};
    int[] sod_d = {1,1,1,1,2,2,2,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,9,9,9,9,10,10,10,10,11,11,11,11,11,12,12,12,13,13,14,14,15,15,16,16,17,17,17,17,18,18,18,19,19,19,19,20,20,20,20,20,21,21,21,21,22,22,23,23,23,23,23,24,24,24,24,25,25,26,26,25,26,25,27,27,28,28,28,29,30,30,30,31,32,32,33,33,34,34,34,34,34,34,34,35,35,35,34,35,35,35,35,36,36,36,36,36,37,37,37,37,37,37,37,37,37,37,37,38,38,38,38,38,38,39,39,39,40,40,40,41,41,41,42,42,43,43,44,44,44,45,45,45,45,45};
    int[] sod_s = {1,2,3,4,4,5,6,3,1,5,7,8,9,10,9,1,11,12,13,14,15,16,17,18,19,16,20,21,22,22,6,21,23,24,25,23,26,27,25,23,28,29,30,31,32,33,34,33,35,36,37,38,26,39,40,41,42,39,42,43,44,45,46,26,44,26,47,48,49,1,50,51,52,51,53,54,55,56,26,57,54,29,57,58,59,60,59,61,61,62,62,63,64,63,26,65,66,67,68,69,70,71,70,72,73,74,6,10,12,8,23,74,75,76,6,44,44,75,12,74,23,17,18,19,14,16,44,6,9,12,10,4,73,74,23,1,76,77,78,1,74,4,16,14,16,79,63,64,80,6,75,81,35,82,83,84,85,86,87,85,86,88,89};
    int[] occ = {100,50,100,5,80,95,5,100,100,70,70,20,30,40,50,30,50,20,90,90,80,30,90,90,70,55,90,90,70,100,70,70,50,100,100,100,95,100,100,100,90,60,100,70,100,50,40,80,70,60,70,50,90,90,90,90,100,95,90,90,90,90,50,100,90,100,100,90,90,100,80,80,70,80,100,100,100,90,60,50,100,90,70,70,90,80,100,100,70,60,50,90,80,100,90,100,100,100,90,90,100,90,100,100,100,100,100,30,20,2,20,50,60,100,100,90,90,90,80,20,30,90,100,80,50,40,100,80,80,80,70,50,50,50,50,40,5,60,60,60,30,25,20,90,70,75,30,90,100,50,40,95,50,90,100,50,100,90,70,100,90,70,60};
    int[] sol = {1,1,1,1,1,1,6,1,1,1,6,6,4,4,4,4,2,2,4,1,1,1,2,3,6,6,3,2,2,3,3,3,3,3,3,3,3,3,5,4,4,5,5,6,5,3,6,1,2,1,1,1,1,2,2,2,2,2,3,3,3,3,3,3,3,6,5,5,5,6,6,6,6,6,1,1,4,5,4,3,3,3,1,1,1,1,1,1,1};

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        fContext = context;
    }


    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table disease ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "doctor text,"
                + "gender text,"
                + "prevalence3 real,"
                + "prevalence12 real,"
                + "prevalence21 real,"
                + "prevalence40 real,"
                + "prevalence60 real,"
                + "prevalence120 real" + ");");

        db.execSQL("create table symptom ("
                + "id integer primary key autoincrement,"
                + "name text" + ");");

        db.execSQL("create table symptom_of_disease ("
                + "id integer primary key autoincrement,"
                + "id_disease integer,"
                + "id_symptom integer,"
                + "occurence integer" + ");");

        db.execSQL("create table location ("
                + "id integer primary key autoincrement,"
                + "name text" + ");");

        db.execSQL("create table symptom_of_location ("
                + "id integer primary key autoincrement,"
                + "id_location integer,"
                + "id_symptom integer" + ");");

        Resources res = fContext.getResources();
        String[] str1 = res.getStringArray(R.array.location);
        int length = str1.length;
        for (int i = 0; i < length; i++) {
            cv = new ContentValues();
            cv.put("name", str1[i]);
            db.insert("location", null, cv);
        }

        str1 = res.getStringArray(R.array.symptom);
        length = str1.length;
        for (int i = 0; i < length; i++) {
            cv = new ContentValues();
            cv.put("name", str1[i]);
            db.insert("symptom", null, cv);
        }

        length = sol.length;
        for (int i = 0; i < length; i++) {
            cv = new ContentValues();
            cv.put("id_location", sol[i]);
            cv.put("id_symptom", i+1);
            db.insert("symptom_of_location", null, cv);
        }

        str1 = res.getStringArray(R.array.disease);
        String[] doctorList = res.getStringArray(R.array.doctor);
        String[] genderList = res.getStringArray(R.array.gender);
        length = str1.length;
        for (int i = 0; i < length; i++) {
            cv = new ContentValues();
            cv.put("name", str1[i]);
            cv.put("doctor", doctorList[i]);
            cv.put("gender", genderList[i]);
            cv.put("prevalence3", prevalence3[i]);
            cv.put("prevalence12", prevalence12[i]);
            cv.put("prevalence21", prevalence21[i]);
            cv.put("prevalence40", prevalence40[i]);
            cv.put("prevalence60", prevalence60[i]);
            cv.put("prevalence120", prevalence120[i]);
            db.insert("disease", null, cv);
        }

        for (int i = 0; i < sod_d.length; i++) {
            cv = new ContentValues();
            cv.put("id_disease", sod_d[i]);
            cv.put("id_symptom", sod_s[i]);
            cv.put("occurence", occ[i]);
            db.insert("symptom_of_disease", null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
    }
}


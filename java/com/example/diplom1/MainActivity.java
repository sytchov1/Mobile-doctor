package com.example.diplom1;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DiseaseFragment diseaseFragment;
    private DiagnosticFragment df;
    private ResultFragment rf;
    private String newItem, doctorName;
    private DBHelper dbHelper;
    private String[] col = {"id","doctor"};
    private String selection = "name = ?";
    private String[] descriptions, recommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Resources resources = this.getResources();
        descriptions = resources.getStringArray(R.array.description);
        recommendations = resources.getStringArray(R.array.recommendations);
        df = new DiagnosticFragment();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                df).commit();
        navigationView.setCheckedItem(R.id.nav_diagnostic);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_diagnostic:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, df).addToBackStack(null).commit();
                break;
            case R.id.nav_handbook:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HandbookFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).addToBackStack(null).commit();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void findDoctor(){
        String q = "https://www.google.com/maps/search/" + doctorName;
        Uri address = Uri.parse(q);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);
    }

    public void addSymp(String s){
        newItem = s;
        df.upgradeList(newItem);
    }

    public void createResult(List<String> diseaseList, List<Integer> percentList){
        rf = new ResultFragment();
        String[] data1 = new String[diseaseList.size()];
        ArrayList<Integer> data2 = new ArrayList<>();
        for (int i = 0; i < diseaseList.size(); i++){
            data1[i] = diseaseList.get(i);
            data2.add(percentList.get(i));
        }
        Bundle bundle = new Bundle();
        bundle.putStringArray("d", data1);
        bundle.putIntegerArrayList("p", data2);
        rf.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rf).addToBackStack(null).commit();
    }

    public void openDisease(String s){
        String[] selArgs = new String[1];
        selArgs[0] = s;
        diseaseFragment = new DiseaseFragment();
        dbHelper = new DBHelper(getApplicationContext(), "mybd", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bundle bundle = new Bundle();

        Cursor cursor = db.query("disease", col, selection, selArgs, null,
                null, null);
        cursor.moveToFirst();
        int field1ColIndex = cursor.getColumnIndex("id");
        int field1ColIndex2 = cursor.getColumnIndex("doctor");
        bundle.putString("d", descriptions[cursor.getInt(field1ColIndex)-1]);
        bundle.putString("r", recommendations[cursor.getInt(field1ColIndex)-1]);
        bundle.putString("n", selArgs[0]);
        doctorName = cursor.getString(field1ColIndex2);
        cursor.close();
        diseaseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, diseaseFragment).addToBackStack(null).commit();
    }
}

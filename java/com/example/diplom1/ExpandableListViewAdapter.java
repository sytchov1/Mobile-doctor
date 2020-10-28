package com.example.diplom1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpandableListViewAdapter {

    final String ATTR_LOCATION_NAME = "LocationName";
    final String ATTR_DISEASE_NAME = "DiseaseName";
    private DBHelper dbHelper;
    String[] columns = {"name"};

    // коллекция для групп
    ArrayList<Map<String, String>> groupData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;

    // список аттрибутов группы или элемента
    Map<String, String> m;

    Context ctx;

    ExpandableListViewAdapter(Context _ctx) {
        ctx = _ctx;
    }

    SimpleExpandableListAdapter adapter;


    SimpleExpandableListAdapter getAdapter() {

        dbHelper = new DBHelper(ctx, "mybd", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("location", columns    , null, null, null,
                null, null);

        cursor.moveToFirst();
        int field1ColIndex = cursor.getColumnIndex("name");
        groupData = new ArrayList<Map<String, String>>();
        do {
            m = new HashMap<String, String>();
            m.put(ATTR_LOCATION_NAME, cursor.getString(field1ColIndex)); // имя компании
            groupData.add(m);
        } while (cursor.moveToNext());
        cursor.close();
        // заполняем коллекцию групп из массива с названиями групп
        Log.i("Size", "" + groupData.size());
        // список аттрибутов групп для чтения
        String groupFrom[] = new String[] {ATTR_LOCATION_NAME};
        // список ID view-элементов, в которые будет помещены аттрибуты групп
        int groupTo[] = new int[] {android.R.id.text1};


        // создаем коллекцию для коллекций элементов
        childData = new ArrayList<ArrayList<Map<String, String>>>();

        for(int i = 0; i < groupData.size(); i++) {

            String table = "symptom as SY inner join symptom_of_location as SOL on SY.id = SOL.id_symptom";
            String col[] = {"SY.name as Name"};
            String selection = "id_location = ?";
            String[] selectionArgs = {String.valueOf(i+1)};
            cursor = db.query(table, col    , selection, selectionArgs, null,
                    null, null);
            cursor.moveToFirst();
            // создаем коллекцию элементов для первой группы
            childDataItem = new ArrayList<Map<String, String>>();
            // заполняем список аттрибутов для каждого элемента
            field1ColIndex = cursor.getColumnIndex("Name");
            do {
                m = new HashMap<String, String>();
                m.put(ATTR_DISEASE_NAME, cursor.getString(field1ColIndex)); // название телефона
                childDataItem.add(m);
            } while (cursor.moveToNext());
            cursor.close();
            // добавляем в коллекцию коллекций
            childData.add(childDataItem);
        }

        // список аттрибутов элементов для чтения
        String childFrom[] = new String[] {ATTR_DISEASE_NAME};
        // список ID view-элементов, в которые будет помещены аттрибуты элементов
        int childTo[] = new int[] {android.R.id.text1};

        adapter = new SimpleExpandableListAdapter(
                ctx,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);

        return adapter;
    }

    String getGroupText(int groupPos) {
        return ((Map<String,String>)(adapter.getGroup(groupPos))).get(ATTR_LOCATION_NAME);
    }

    String getChildText(int groupPos, int childPos) {
        return ((Map<String,String>)(adapter.getChild(groupPos, childPos))).get(ATTR_DISEASE_NAME);
    }

    String getGroupChildText(int groupPos, int childPos) {
        return getGroupText(groupPos) + " " +  getChildText(groupPos, childPos);
    }
}

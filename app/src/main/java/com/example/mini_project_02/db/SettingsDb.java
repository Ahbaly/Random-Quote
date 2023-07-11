package com.example.mini_project_02.db;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.example.mini_project_02.models.Color;
import com.example.mini_project_02.models.Quote;
import com.example.mini_project_02.models.Setting;

import java.util.ArrayList;

public class SettingsDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Settings.db";
    private static final String SQL_CREATE_SETTING = String.format("CREATE TABLE %s (" +
                    "%s TEXT PRIMARY KEY," +
                    "%s TEXT)",
            SettingsContract.Setting.TABLE_NAME,
            SettingsContract.Setting.COLUMN_NAME_ID,
            SettingsContract.Setting.COLUMN_NAME_VALUE);

    private static final String SQL_CREATE_COLOR = String.format("CREATE TABLE %s (" +
                    "%s TEXT PRIMARY KEY," +
                    "%s TEXT)",
            SettingsContract.Color.TABLE_NAME,
            SettingsContract.Color.COLUMN_NAME_ID,
            SettingsContract.Color.COLUMN_NAME_CODE);


    private static final String SQL_DELETE_FAVORITE_SETTING = String.format("DROP TABLE IF EXISTS %s",
            SettingsContract.Setting.TABLE_NAME);

    private static final String SQL_DELETE_FAVORITE_COLOR = String.format("DROP TABLE IF EXISTS %s",
            SettingsContract.Color.TABLE_NAME);

    public SettingsDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        assert context != null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("inserted", MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("status",false);

        if(!status){
            addColor(new Color("LightSalmon","#FFA07A"));
            addColor(new Color("Plum","#DDA0DD"));
            addColor(new Color("PaleGreen","#98FB98"));
            addColor(new Color("CornflowerBlue","#6495ED"));

            addSetting(new Setting("bg","LightSalmon"));
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("status", true);
        editor.apply();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SETTING);
        db.execSQL(SQL_CREATE_COLOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITE_SETTING);
        db.execSQL(SQL_DELETE_FAVORITE_COLOR);
        onCreate(db);
    }

    private void addColor(Color color) {
        SQLiteDatabase db = SettingsDb.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SettingsContract.Color.COLUMN_NAME_ID, color.getName());
        values.put(SettingsContract.Color.COLUMN_NAME_CODE, color.getCode());

        db.insert(SettingsContract.Color.TABLE_NAME, null, values);
    }

    public void deleteColor(String name) {
        SQLiteDatabase db = SettingsDb.this.getWritableDatabase();

        String selection = SettingsContract.Color.COLUMN_NAME_ID + " = ?";

        String[] selectionArgs = {name};

        db.delete(SettingsContract.Color.TABLE_NAME, selection, selectionArgs);
    }

    private void addSetting(Setting setting) {
        SQLiteDatabase db = SettingsDb.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SettingsContract.Setting.COLUMN_NAME_ID, setting.getName());
        values.put(SettingsContract.Setting.COLUMN_NAME_VALUE, setting.getValue());

        db.insert(SettingsContract.Setting.TABLE_NAME, null, values);
    }

    public void UpdateSetting(Setting setting) {
        SQLiteDatabase db = SettingsDb.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SettingsContract.Setting.COLUMN_NAME_ID, setting.getName());
        values.put(SettingsContract.Setting.COLUMN_NAME_VALUE, setting.getValue());

        db.update(SettingsContract.Setting.TABLE_NAME, values,"name = ?", new String[]{setting.getName()});
    }

    public void deleteSetting(String name) {
        SQLiteDatabase db = SettingsDb.this.getWritableDatabase();

        String selection = SettingsContract.Setting.COLUMN_NAME_ID + " = ?";

        String[] selectionArgs = {name};

        db.delete(SettingsContract.Setting.TABLE_NAME, selection, selectionArgs);
    }

    public ArrayList<Color> getAllColors() {
        ArrayList<Color> colors = new ArrayList<>();
        SQLiteDatabase db = SettingsDb.this.getReadableDatabase();

        String[] projection = {
                SettingsContract.Color.COLUMN_NAME_ID,
                SettingsContract.Color.COLUMN_NAME_CODE,
        };

        Cursor cursor = db.query(
                SettingsContract.Color.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SettingsContract.Color.COLUMN_NAME_ID));
            String code = cursor.getString(cursor.getColumnIndexOrThrow(SettingsContract.Color.COLUMN_NAME_CODE));

            colors.add(new Color(name,code));
        }

        cursor.close();

        return colors;
    }

    public ArrayList<Setting> getAllSettings() {
        ArrayList<Setting> settings = new ArrayList<>();
        SQLiteDatabase db = SettingsDb.this.getReadableDatabase();

        String[] projection = {
                SettingsContract.Setting.COLUMN_NAME_ID,
                SettingsContract.Setting.COLUMN_NAME_VALUE,
        };

        Cursor cursor = db.query(
                SettingsContract.Setting.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SettingsContract.Setting.COLUMN_NAME_ID));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(SettingsContract.Setting.COLUMN_NAME_VALUE));

            settings.add(new Setting(name,value));
        }

        cursor.close();

        return settings;
    }
}

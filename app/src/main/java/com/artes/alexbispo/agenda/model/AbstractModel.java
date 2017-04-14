package com.artes.alexbispo.agenda.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 31/12/16.
 */

public abstract class AbstractModel extends SQLiteOpenHelper {

    public AbstractModel(Context context, int dataBaseVersion) {
        super(context, "Agenda", null, dataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + toSql() + ";";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + getTableName() + ";";
        db.execSQL(sql);
    }

    protected abstract String toSql();

    protected abstract String getTableName();
}

package com.artes.alexbispo.agenda.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 31/12/16.
 */

public class SQLObject extends SQLiteOpenHelper {

    private final Accessible accessible;

    public SQLObject(Context context, Accessible accessible) {
        super(context, "Agenda", null, accessible.getDataBaseVersion());
        this.accessible = accessible;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + accessible.toSql() + ";";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + accessible.getTableName() + ";";
        db.execSQL(sql);
    }

    public interface Accessible {

        int getDataBaseVersion();

        String getTableName();

        String toSql();
    }
}

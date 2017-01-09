package net.galliliu.jigsawpuzzle.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by galliliu on 16/5/15.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper{
    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableColumnConstant.CREATE_TABLE_GAME);
        db.execSQL(TableColumnConstant.CREATE_TABLE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package net.galliliu.jigsawpuzzle.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.galliliu.jigsawpuzzle.presenter.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by galliliu on 16/5/28.
 */
public class RecordModel implements IRecordModel{
    public static final String TAG = "RecordModel";
    public static final int MAX_NUMBER_OF_RECORD = 10;
    private DatabaseOpenHelper doh;

    public RecordModel(Context context) {
        doh = new DatabaseOpenHelper(context,
                TableColumnConstant.DATABASE_NAME,
                null,
                TableColumnConstant.DATABASE_VERSION1);
    }

    @Override
    public void insert(String tag, String gameID, String photoID,
                       int type, long time, String endDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumnConstant.GAME_TAG, tag);
        values.put(TableColumnConstant.GAME_ID, gameID);
        values.put(TableColumnConstant.PHOTO_ID, photoID);
        values.put(TableColumnConstant.TYPE, type);
        values.put(TableColumnConstant.TIME, time);
        values.put(TableColumnConstant.END_DATE, endDate);

        SQLiteDatabase db = doh.getReadableDatabase();
        try {
            db.insert(TableColumnConstant.TABLE_RECORD, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Exception occured!" + e.getMessage());
        } finally {
            db.close();
        }

        // Delete those redundant records, we only need top ten records
//        String statement = "SELECT * FROM " + TableColumnConstant.TABLE_RECORD + " WHERE " +
//                TableColumnConstant.GAME_ID + "=?" + " ORDER BY TIME";
//        List<Map> recordList = get(statement, new String[] {gameID});
//        if (recordList.size() > MAX_NUMBER_OF_RECORD) {
//            for (int i = MAX_NUMBER_OF_RECORD; i < recordList.size(); i++) {
//                delete(TableColumnConstant.GAME_TAG + "=? and " + TableColumnConstant.GAME_ID + "=?",
//                        new String[]{
//                                (String) recordList.get(i).get(TableColumnConstant.GAME_TAG),
//                                (String) recordList.get(i).get(TableColumnConstant.GAME_ID)
//                        });
//            }
//        }
    }

    @Override
    public List<Map> get(String[] whereArgs) {
        String statement = "SELECT * FROM " + TableColumnConstant.TABLE_RECORD
                + " WHERE " + TableColumnConstant.GAME_ID + "=?"
                + " AND " + TableColumnConstant.TYPE + "=?"
                + " ORDER BY time"
                + " LIMIT " + MAX_NUMBER_OF_RECORD;
        List<Map> recordList = new ArrayList<>();
        SQLiteDatabase db = doh.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(statement, whereArgs);
            if (cursor.moveToFirst()) {
                do {
                    String tag = cursor.getString(cursor.getColumnIndex(TableColumnConstant.GAME_TAG));
                    String gameID = cursor.getString(cursor.getColumnIndex(TableColumnConstant.GAME_ID));
                    String photoID = cursor.getString(cursor.getColumnIndex(TableColumnConstant.PHOTO_ID));
                    int type = cursor.getInt(cursor.getColumnIndex(TableColumnConstant.TYPE));
                    long time = cursor.getInt(cursor.getColumnIndex(TableColumnConstant.TIME));
                    String endDate = cursor.getString(cursor.getColumnIndex(TableColumnConstant.END_DATE));
                    Map<String, Object> map = new HashMap<>();
                    map.put(TableColumnConstant.GAME_TAG, tag);
                    map.put(TableColumnConstant.GAME_ID, gameID);
                    map.put(TableColumnConstant.PHOTO_ID, photoID);
                    map.put(TableColumnConstant.TYPE, type);
                    map.put(TableColumnConstant.TIME, TimeUtil.formatTime(time));
                    map.put(TableColumnConstant.TIME_LONG, time);
                    map.put(TableColumnConstant.END_DATE, endDate);
                    recordList.add(map);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Exception occured!"  + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return recordList;
    }



    @Override
    public List<Map> getRank(String[] whereArgs) {
        String statement = "SELECT DISTINCT time FROM " + TableColumnConstant.TABLE_RECORD
                + " WHERE " + TableColumnConstant.GAME_ID + "=?" + " AND "
                + TableColumnConstant.TYPE + "=?" + " ORDER BY time";
        List<Map> rankList = new ArrayList<>();
        SQLiteDatabase db = doh.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(statement, whereArgs);
            if (cursor.moveToFirst()) {
                do {
                    long time = cursor.getInt(cursor.getColumnIndex(TableColumnConstant.TIME));
                    Map<String, Object> map = new HashMap<>();
                    map.put(TableColumnConstant.TIME_LONG, time);
                    rankList.add(map);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Exception occured!"  + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return rankList;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = doh.getReadableDatabase();
        // Delete all records
        try {
            db.delete(TableColumnConstant.TABLE_RECORD, whereClause, whereArgs);
        }catch(SQLException e){
            Log.e(TAG, "Exception occured! message: " + e.getMessage());
        }finally{
            db.close();
        }
    }

    @Override
    public void deleteRecords(String gameID) {
        String whereClause = TableColumnConstant.GAME_ID + "=?";
        delete(whereClause, new String[] {gameID});
    }

    @Override
    public void deleteAll(String gameID) {

    }
}

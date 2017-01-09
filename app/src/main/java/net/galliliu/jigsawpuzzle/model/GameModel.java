package net.galliliu.jigsawpuzzle.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.GameTag;
import net.galliliu.jigsawpuzzle.presenter.GameManager;
import net.galliliu.jigsawpuzzle.presenter.PhotoManager;
import net.galliliu.jigsawpuzzle.presenter.TimeUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by galliliu on 16/5/24.
 */
public class GameModel implements IGameModel{
    public static final String TAG = "GameModel";
    private DatabaseOpenHelper doh;

    public GameModel(Context context) {
        doh = new DatabaseOpenHelper(context,
                TableColumnConstant.DATABASE_NAME,
                null,
                TableColumnConstant.DATABASE_VERSION1);
        doh.getWritableDatabase();
    }

    @Override
    public void newGame(GameInfo gameInfo) {
        ContentValues values = new ContentValues();
        values.put(TableColumnConstant.GAME_TAG, gameInfo.getGameTag().getTag());
        values.put(TableColumnConstant.GAME_ID, gameInfo.getGameID());
        values.put(TableColumnConstant.PHOTO_ID, gameInfo.getPhotoID());
        values.put(TableColumnConstant.START_DATE, gameInfo.getStartDate());

        SQLiteDatabase db = doh.getReadableDatabase();
        try {
            db.insert(TableColumnConstant.TABLE_GAME, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Exception occured!");
        } finally {
          db.close();
        }
    }

    @Override
    public void updateGameList(LinkedList<Map> gameInfoList) {
        SQLiteDatabase db = doh.getReadableDatabase();
        Cursor cursor = null;
        gameInfoList.clear();

        try {
            cursor = db.rawQuery("SELECT * FROM " + TableColumnConstant.TABLE_GAME, null);
            if (cursor.moveToFirst()) {
                int number = 1;
                do {
                    String tag = cursor.getString(cursor.getColumnIndex(TableColumnConstant.GAME_TAG));
                    String game_id = cursor.getString(cursor.getColumnIndex(TableColumnConstant.GAME_ID));
                    String photo_id = cursor.getString(cursor.getColumnIndex(TableColumnConstant.PHOTO_ID));
                    long start_date = cursor.getLong(cursor.getColumnIndex(TableColumnConstant.START_DATE));
                    GameInfo gi = new GameInfo(game_id, photo_id, GameTag.valueOf(tag),
                            TimeUtil.formatDateTime(new Date(start_date)));

                    // For game label and lose game photo label
                    Map map = new HashMap();
                    map.put(GameManager.GAME_INFO_KEY, gi);
                    if (GameManager.isDefaultGame(gi)) {
                        map.put(GameManager.GAME_NUMBER_KEY, GameManager.parseGameID(gi));
                        // Game photo not found
                        if (PhotoManager.parseDefaultGamePhotoID(gi) == R.drawable.photolost) {
                            map.put(GameManager.GAME_PHOTO_LOST_KEY, true);
                        } else {
                            map.put(GameManager.GAME_PHOTO_LOST_KEY, false);
                        }
                    } else {
                        map.put(GameManager.GAME_NUMBER_KEY, number);
                        // Game photo not found
                        boolean isFound = PhotoManager.isPhotoLost(gi);
                        map.put(GameManager.GAME_PHOTO_LOST_KEY, isFound);
                        number++;
                    }

                    gameInfoList.add(map);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Exception occured!" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public int createPhotoID() {
        int id = 0;
        SQLiteDatabase db = doh.getReadableDatabase();
        Cursor cursor = null;
        try{
            String statement = "SELECT id FROM " + TableColumnConstant.TABLE_GAME + " ORDER BY id DESC";
            cursor = db.rawQuery(statement, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occured!");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        // ID which we need
        return id + 1;
    }

    @Override
    public void deleteGame(GameInfo gameInfo) {
        // Delete the gameInfo which stored in database
        SQLiteDatabase db = doh.getReadableDatabase();
        try {
            db.delete(TableColumnConstant.TABLE_GAME,
                    TableColumnConstant.GAME_TAG + "=? and " + TableColumnConstant.GAME_ID + "=?",
                    new String[]{gameInfo.getGameTag().getTag(), gameInfo.getGameID()});
        } catch (Exception e) {
            Log.e(TAG, "Exception occured!");
        } finally {
            db.close();
        }
    }
}

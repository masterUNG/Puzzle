package net.galliliu.jigsawpuzzle.model;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import net.galliliu.jigsawpuzzle.bean.GameInfo;

import java.io.File;

/**
 * Created by galliliu on 16/5/24.
 */
public class PhotoModel implements IPhotoModel{
    public static final String TAG = "PhotoModel";
//    private DatabaseOpenHelper doh;

    public PhotoModel(Context context) {
//        doh = new DatabaseOpenHelper(context,
//                TableColumnConstant.DATABASE_NAME,
//                null,
//                TableColumnConstant.DATABASE_VERSION1);
    }

//    @Override
//    public int createPhotoID() {
//        int id = 0;
//        SQLiteDatabase db = doh.getReadableDatabase();
//        Cursor cursor = null;
//        try{
//            String statement = "SELECT id FROM " + TableColumnConstant.TABLE_GAME + " ORDER BY id DESC";
//            cursor = db.rawQuery(statement, null);
//            if (cursor.moveToFirst()) {
//                id = cursor.getInt(cursor.getColumnIndex("id"));
//            } else {
//                id = 0;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Exception occured!");
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            db.close();
//        }
//
//        // The id which we need
//        return id + 1;
//    }

    @Override
    public Uri createPhotoFile(String photoID, String gameID) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory() + File.separator
                    + FileConstant.PHOTO_FILE_PATH + File.separator;
            File file = new File(path);

            if (!file.exists()) {
                file.mkdir();
            }
            Log.i("PhotoPath", file.getPath());
            return Uri.fromFile(new File(path + gameID + FileConstant.PHOTO_FILE_EXTENSION));
        } else {
            return null;
        }
    }

    @Override
    public boolean deletePhotoFile(GameInfo gameInfo) {
        String path = Environment.getExternalStorageDirectory() + File.separator
                + FileConstant.PHOTO_FILE_PATH + File.separator;
        File file = new File(path);

        if (!file.exists()) {
            Log.i("PhotoPath", "Not such directory!");
            return false;
        }

        File photoFile = new File(path + gameInfo.getGameID() + FileConstant.PHOTO_FILE_EXTENSION);
        Log.i("Photo", photoFile.getAbsolutePath());

        return !photoFile.exists() || photoFile.delete();
    }
}

package net.galliliu.jigsawpuzzle.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;
import android.util.Log;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.GameTag;
import net.galliliu.jigsawpuzzle.bean.PhotoSlice;
import net.galliliu.jigsawpuzzle.model.FileConstant;
import net.galliliu.jigsawpuzzle.model.IPhotoModel;
import net.galliliu.jigsawpuzzle.model.PhotoModel;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by galliliu on 16/5/12.
 */
public class PhotoManager {
    public static final String TAG = "PhotoManager";
    int ASPECTX;
    int ASPECTY;
    int OUTPUTX;
    int OUTPUTY;
    private IPhotoModel photoModel;

    {
        ASPECTX = 1;
        ASPECTY = 1;
        OUTPUTX = 1000;
        OUTPUTY = 1000;
    }
    Uri tempFileUri;

    // Using a filename identify a picture and a game
    String gameID = "";
    String photoID = "";

    public PhotoManager(Context context) {
        this.photoModel = new PhotoModel(context);
    }

    public Intent cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", ASPECTX);
        intent.putExtra("aspectY", ASPECTY);
        intent.putExtra("outputX", OUTPUTX);
        intent.putExtra("outputY", OUTPUTY);
        return intent;
    }

    public Uri getTempFileUri() {
        return tempFileUri;
    }

    /**
     * Save a *.png file to the specifically directory(/storage/emulated/0/net.galliliu.puzzle/)
     * @return boolean returns true if this photo file is created, false otherwise
     */
    public boolean createPhotoFile() {
        gameID = FileConstant.PHOTO_FILE_CUSTOM_PREFIX + photoID;
        tempFileUri = photoModel.createPhotoFile(photoID, gameID);
        return tempFileUri != null;
    }

    public GameInfo newGameInfo() {
        return new GameInfo(gameID, photoID, GameTag.Custom, TimeUtil.formatDateTime(new Date()));
    }

    public void setPhotoID(String id) {
        this.photoID = id;
    }

    public boolean deletePhoto(GameInfo gameInfo) {
        return photoModel.deletePhotoFile(gameInfo);
    }

    public static @DrawableRes int parseDefaultGamePhotoID(GameInfo gameInfo) {
        int id = Integer.parseInt(gameInfo.getPhotoID());
        switch (id) {
            case R.drawable.photo1:
                return R.drawable.photo1;
            case R.drawable.photo2:
                return R.drawable.photo2;
            case R.drawable.photo3:
                return R.drawable.photo3;
            case R.drawable.photo4:
                return R.drawable.photo4;
            case R.drawable.photo5:
                return R.drawable.photo5;
            case R.drawable.photo6:
                return R.drawable.photo6;
            case R.drawable.photo7:
                return R.drawable.photo7;
            case R.drawable.photo8:
                return R.drawable.photo8;
            case R.drawable.photo9:
                return R.drawable.photo9;
            case R.drawable.photo10:
                return R.drawable.photo10;
            default:
                return R.drawable.photolost;
        }
    }

    public static boolean isPhotoLost(GameInfo gameInfo) {
        String path = Environment.getExternalStorageDirectory() + File.separator
                + FileConstant.PHOTO_FILE_PATH + File.separator + gameInfo.getGameID()
                + FileConstant.PHOTO_FILE_EXTENSION;
        File file = new File(path);

        return !file.exists();
    }

    public static Uri parseCustomGamePhotoUri(GameInfo gameInfo){
        String uri = "file://" + Environment.getExternalStorageDirectory() + File.separator
                + FileConstant.PHOTO_FILE_PATH + File.separator + gameInfo.getGameID()
                + FileConstant.PHOTO_FILE_EXTENSION;
//        Uri photoUri = new Uri.Builder().scheme("file").appendPath(path).build();
//        Log.e(TAG, "Photo Uri path: " + photoUri.getPath());
//        Log.e(TAG, "Photo Uri path: " + photoUri.getPath());
//        Log.e(TAG, "Photo Uri Encoded path: " + photoUri.getEncodedPath());
        return Uri.parse(uri);
    }

    /**
     * Decode a photo
     * @param context application context
     * @param gameInfo Game information
     * @return Bitmap a decoded photo
     * */
    public static Bitmap decodePhoto(Context context, GameInfo gameInfo) {
        Bitmap bitmap;

        if (GameManager.isDefaultGame(gameInfo)) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), parseDefaultGamePhotoID(gameInfo));
        } else {
            String path = Environment.getExternalStorageDirectory() + File.separator
                    + FileConstant.PHOTO_FILE_PATH + File.separator + gameInfo.getGameID()
                    + FileConstant.PHOTO_FILE_EXTENSION;
            File file = new File(path);

            if (!file.exists()) {
                Log.i("PhotoPath", "Not such directory!");
                // if the file not exist, replace it with a default photo
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.photolost);
            }

            bitmap = BitmapFactory.decodeFile(path);
        }

        return bitmap;
    }

    /**
     * Resize a photo
     * @param bitmapSelected source bitmap
     * @param displayMetrics scale factor
     * @return Bitmap bitmap
     * */
    public static Bitmap resizePhoto(Bitmap bitmapSelected, DisplayMetrics displayMetrics) {
        int bitmapSelectedWidth = bitmapSelected.getWidth();
        int bitmapSelectedHeight = bitmapSelected.getHeight();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        Log.i("Layout", "width:" + screenWidth + " height:" + screenHeight);
        float scalex = (float) screenWidth / bitmapSelectedWidth;
        float scaley = (float) screenWidth / bitmapSelectedHeight;

        Matrix matrix = new Matrix();

        matrix.postScale(scalex, scaley);
        return Bitmap.createBitmap(bitmapSelected, 0, 0,
                bitmapSelectedWidth, bitmapSelectedHeight,
                matrix, true);
    }

    /**
     * Split this photo with the given game type
     * */
    public static void splitPhoto(LinkedList<PhotoSlice> bitmapLinkedList, Bitmap srcBitmap, int gameType) {
        Bitmap tempBitmap;
        // Calculate photo width and height
        int width = srcBitmap.getWidth() / gameType;
        int height = srcBitmap.getHeight() / gameType;

        // Do split
        for (int y = 0; y < gameType; y++) {
            for (int x = 0; x < gameType; x++) {
                tempBitmap = Bitmap.createBitmap(srcBitmap, x * width, y * height, width, height);
                PhotoSlice slice = new PhotoSlice(tempBitmap, y * gameType + x + 1);
                bitmapLinkedList.add(slice);
            }
        }
    }
}

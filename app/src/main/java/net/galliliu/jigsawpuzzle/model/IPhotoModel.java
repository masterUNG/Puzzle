package net.galliliu.jigsawpuzzle.model;

import android.net.Uri;

import net.galliliu.jigsawpuzzle.bean.GameInfo;

/**
 * Created by galliliu on 16/5/24.
 */
public interface IPhotoModel {
//    int createPhotoID();
    Uri createPhotoFile(String photoID, String gameID);
    boolean deletePhotoFile(GameInfo gameInfo);
}

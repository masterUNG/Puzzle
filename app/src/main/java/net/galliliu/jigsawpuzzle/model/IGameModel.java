package net.galliliu.jigsawpuzzle.model;

import net.galliliu.jigsawpuzzle.bean.GameInfo;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by galliliu on 16/5/24.
 */
public interface IGameModel {
    void newGame(GameInfo gameInfo);
    void updateGameList(LinkedList<Map> gameInfoList);
    int createPhotoID();
    void deleteGame(GameInfo gameInfo);
}

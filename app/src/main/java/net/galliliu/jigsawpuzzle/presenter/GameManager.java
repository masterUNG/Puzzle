package net.galliliu.jigsawpuzzle.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.GameTag;
import net.galliliu.jigsawpuzzle.bean.GameType;
import net.galliliu.jigsawpuzzle.model.FileConstant;
import net.galliliu.jigsawpuzzle.model.GameModel;
import net.galliliu.jigsawpuzzle.model.IGameModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import static net.galliliu.jigsawpuzzle.presenter.SharedPreferencesKey.IS_FIRST_START;


/**
 * Created by galliliu on 16/5/13.
 */
public class GameManager {
    public static final String TAG = "GameManager";
    public static final String GAME_INFO_KEY = "game_info_key";
    public static final String GAME_NUMBER_KEY = "game_number_key";
    public static final String GAME_PHOTO_LOST_KEY = "game_photo_lost_key";
    private SharedPreferences settings;
    private LinkedList<Map> gameInfoList;
    private IGameModel gameModel;

    public GameManager(Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.gameModel = new GameModel(context);
        gameInfoList = new LinkedList<>();

        // If this is the first time we start this app, preload some default games
        createDefaultGames(settings.getBoolean(IS_FIRST_START, true));
        updateGameList();
    }

    private void createDefaultGames(boolean isFirstStart) {
        if (isFirstStart) {
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "1",
                    String.valueOf(R.drawable.photo1),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "2",
                    String.valueOf(R.drawable.photo2),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "3",
                    String.valueOf(R.drawable.photo3),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "4",
                    String.valueOf(R.drawable.photo4),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "5",
                    String.valueOf(R.drawable.photo5),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "6",
                    String.valueOf(R.drawable.photo6),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "7",
                    String.valueOf(R.drawable.photo7),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "8",
                    String.valueOf(R.drawable.photo8),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "9",
                    String.valueOf(R.drawable.photo9),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            gameModel.newGame(new GameInfo(FileConstant.PHOTO_FILE_DEFAULT_PREFIX + "10",
                    String.valueOf(R.drawable.photo10),
                    GameTag.Default,
                    TimeUtil.formatDateTime(new Date())));
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(IS_FIRST_START, false);
            editor.apply();
        }
    }

    public void newGame(GameInfo gameInfo) {
        gameModel.newGame(gameInfo);
    }

    public void updateGameList() {
        gameModel.updateGameList(gameInfoList);
    }

    public void deleteGame(GameInfo gameInfo) {
        gameModel.deleteGame(gameInfo);
    }

    public LinkedList<Map> getGameInfoList() {
        return gameInfoList;
    }

    public int createGamePhotoID() {
        return gameModel.createPhotoID();
    }

    public boolean isExceedMaxGameAmount(int id) {
        return id >= Integer.MAX_VALUE;
    }

    public static boolean isDefaultGame(GameInfo gameInfo) {
        return gameInfo.getGameTag() == GameTag.Default;
    }

    public static int parseGameID(GameInfo gameInfo) {
        return Integer.parseInt(gameInfo.getGameID().split("_")[2]);
    }

    /**
     * Parse game type
     * @param gameInfo game information
     * @return game type
     * */
    public static int parseGameType(GameInfo gameInfo) {
        // Split photo according to game type, GameType1-3*3,GameType2-4*4,etc.
        switch (gameInfo.getGameType()) {
            case GameType3:
                return GameType.TYPE_NUMBER3;
            case GameType4:
                return GameType.TYPE_NUMBER4;
            case GameType5:
                return GameType.TYPE_NUMBER5;
            case GameType6:
                return GameType.TYPE_NUMBER6;
            case GameType7:
                return GameType.TYPE_NUMBER7;
            default:
                return GameType.TYPE_NUMBER3;
        }
    }
}

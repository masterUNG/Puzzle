package net.galliliu.jigsawpuzzle.model;

/**
 * Created by galliliu on 16/5/15.
 */
public class TableColumnConstant {
    public static final int DATABASE_VERSION1 = 1;
    public static final int DATABASE_VERSION2 = 2;
    public static final int DATABASE_VERSION3 = 3;
    public static final String TIME_LONG = "time_long";

    // Table column name
    public static final String DATABASE_NAME = "puzzle.db";
    public static final String TABLE_RECORD = "record";
    public static final String TABLE_GAME = "game";
    public static final String GAME_TAG = "game_tag";
    public static final String GAME_ID = "game_id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String PHOTO_ID = "photo_id";
    public static final String TIME = "time";
    public static final String TYPE = "type";

    public static final String CREATE_TABLE_GAME = "CREATE TABLE game ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "game_tag TEXT, "
            + "game_id TEXT, "
            + "photo_id TEXT, "
            + "start_date TEXT)";

    public static final String CREATE_TABLE_RECORD = "CREATE TABLE record ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "game_tag TEXT, "
            + "game_id TEXT, "
            + "photo_id TEXT, "
            + "type INTEGER, "
            + "time INTEGER, "
            + "end_date TEXT)";
}

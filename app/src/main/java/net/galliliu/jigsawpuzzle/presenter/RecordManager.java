package net.galliliu.jigsawpuzzle.presenter;

import android.content.Context;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.GameType;
import net.galliliu.jigsawpuzzle.model.IRecordModel;
import net.galliliu.jigsawpuzzle.model.RecordModel;
import net.galliliu.jigsawpuzzle.model.TableColumnConstant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by galliliu on 16/5/13.
 */
public class RecordManager {
    public static final String RECORD_RANK = "RANK";
    public static final long RECORD_NOT_FOUND = -1;
    public static final int FIRST = 1;
    public static final int SECOND = 2;
    public static final int THIRD = 3;
    Context context;
    IRecordModel record;

    public RecordManager(Context context) {
        this.context = context;
        record = new RecordModel(context);
    }

    public void insertRecord(GameInfo gameInfo, long time, String endDate) {
        String tag = gameInfo.getGameTag().getTag();
        String gameID = gameInfo.getGameID();
        String photoID = gameInfo.getPhotoID();
        int type = GameType.parserGameType(gameInfo.getGameType());
        record.insert(tag, gameID, photoID, type, time, endDate);
    }

    public Map getBestRecord(GameInfo gameInfo, long time) {
        List<Map> recordList = record.getRank(new String[] {gameInfo.getGameID(),
                String.valueOf(GameType.parserGameType(gameInfo.getGameType()))});
        Map<String, Object> data = new HashMap<>();

        if (recordList.isEmpty()) {
            data.put("title", R.string.dialog_title_new_record);
            data.put("imgID", R.drawable.trophy_gold);
            data.put("bestRecord", RECORD_NOT_FOUND);
        } else {
            // search current game rank
            int rank = 1;
            boolean isDraw = false;
            for (int i = 0; i < recordList.size(); i++) {
                if (time > (long)recordList.get(i).get(TableColumnConstant.TIME_LONG)) {
                    rank++;
                } else if (time == (long)recordList.get(i).get(TableColumnConstant.TIME_LONG)) {
                    isDraw = true;
                    break;
                } else {
                    break;
                }

                if (rank > 3) {
                    break;
                }
            }
            switch (rank) {
                case 1:
                    if (isDraw) {
                        data.put("title", R.string.dialog_title_draw);
                    } else {
                        data.put("title", R.string.dialog_title_first);
                    }
                    data.put("imgID", R.drawable.trophy_gold);
                    break;
                case 2:
                    data.put("title", R.string.dialog_title_second);
                    data.put("imgID", R.drawable.trophy_silver);
                    break;
                case 3:
                    data.put("title", R.string.dialog_title_third);
                    data.put("imgID", R.drawable.trophy_bronze);
                    break;
                default:
                    data.put("title", R.string.dialog_title_win);
                    data.put("imgID", R.drawable.trophy_black);
                    break;
            }
            data.put("bestRecord", recordList.get(0).get(TableColumnConstant.TIME_LONG));
        }
//        Log.i("TTTT", "Best Time: " + data.get("bestRecord"));
        return data;
    }

    public List<Map> getRecords(String gameID, String gameType) {
        String[] whereArgs = new String[] {gameID, gameType};
        List<Map> recordList = record.get(whereArgs);
        List<Map> rankList = record.getRank(whereArgs);
        HashMap mapRecord;
        HashMap mapRank;

//        for (int i = 0; i < rankList.size(); i++) {
//            HashMap m = (HashMap) rankList.get(i);
//            Log.i("Records Rank", "Records Rank:" + gameType + " " + TableColumnConstant.TIME_LONG + ":"
//                    + m.get(TableColumnConstant.TIME_LONG));
//        }

        // Label rank for all records
        for (int i = 0; i < recordList.size(); i++) {
            mapRecord = (HashMap) recordList.get(i);
            for (int j = 0; j < rankList.size(); j++) {
                mapRank = (HashMap) rankList.get(j);
                if ((long) mapRecord.get(TableColumnConstant.TIME_LONG)
                        == (long) mapRank.get(TableColumnConstant.TIME_LONG)) {
                    mapRecord.put(RECORD_RANK, j + 1);
                    break;
                }
            }
        }
        return recordList;
    }

    public String getAllBestRecords(LinkedList<Map> gameInfoList) {
        String content = context.getString(R.string.content_share1) + "\n";

        for (int i = GameType.TYPE_NUMBER3; i <=  GameType.TYPE_NUMBER7; i++) {
            long bestTime = Long.MAX_VALUE;
            for (Map map : gameInfoList) {
                GameInfo gameInfo = (GameInfo) map.get(GameManager.GAME_INFO_KEY);
                List<Map> list = getRecords(gameInfo.getGameID(), String.valueOf(i));
                if (!list.isEmpty()) {
                    long time = (long) list.get(0).get(TableColumnConstant.TIME_LONG);
                    if (time < bestTime) {
                        bestTime = time;
                    }
                }
            }

            String type = context.getString(GameType.valueOfType(i).getResID());
            if (bestTime < Long.MAX_VALUE) {
                content = content + TimeUtil.formatTime(bestTime) + "(" + type + "),\n";
            } else {
                content = content + TimeUtil.formatTime(0) + "(" + type + "),\n";
            }
        }
        content = content.substring(0, content.length()-2) + ".\n";
        content += context.getString(R.string.content_share2);
        content += "http://www.wandoujia.com/apps/net.galliliu.jigsawpuzzle";

        return content;
    }

    public void deleteRecords(GameInfo gameInfo) {
        record.deleteRecords(gameInfo.getGameID());
    }

    public void resetAll(GameInfo gameInfo) {
        record.deleteRecords(gameInfo.getGameID());
    }
}

package net.galliliu.jigsawpuzzle.model;

import java.util.List;
import java.util.Map;

/**
 * Created by galliliu on 16/5/28.
 */
public interface IRecordModel {
    void insert(String tag, String gameID, String photoID,
                int type, long time, String endDate);
    List<Map> get(String[] whereArgs);
    void update();
    List<Map> getRank(String[] whereArgs);
    void delete(String whereClause, String[] whereArgs);
    void deleteRecords(String gameID);
    void deleteAll(String gameID);
}

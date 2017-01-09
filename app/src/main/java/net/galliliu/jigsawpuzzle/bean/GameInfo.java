package net.galliliu.jigsawpuzzle.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by galliliu on 16/5/13.
 */
public class GameInfo implements Parcelable{
    private GameTag gameTag;
    private GameType gameType;
    private String startDate;
    private String gameID;
    private String photoID;
    private long timePlayed;

    public GameInfo() {

    }

    public GameInfo(String gid, String pid, GameTag gt, String d){
        this(gid, pid, 0, gt, GameType.GameTypeDefault, d);
    }

    public GameInfo(String gid, String pid, long t, GameTag gt, GameType type, String d) {
        this.gameID = gid;
        this.photoID = pid;
        this.timePlayed = t;
        this.gameTag = gt;
        this.gameType = type;
        this.startDate = d;
    }

    private GameInfo(Parcel parcel) {
        gameTag = parcel.readParcelable(GameTag.class.getClassLoader());
        gameType = parcel.readParcelable(GameType.class.getClassLoader());
        startDate = parcel.readString();
        gameID = parcel.readString();
        photoID = parcel.readString();
        timePlayed = parcel.readInt();
    }

    public GameTag getGameTag() {
        return gameTag;
    }

    public String getGameID(){
        return gameID;
    }

    public String getPhotoID(){
        return photoID;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setGameTag(GameTag gt) {
        this.gameTag = gt;
    }

    public void setGameID(String id) {
        this.gameID = id;
    }

    public void setPhotoID(String id) {
        this.photoID = id;
    }

    public void setTimePlayed(long t) {
        this.timePlayed = t;
    }

    public void setGameType(GameType l) {
        this.gameType = l;
    }

    public void setStartDate(String d) {
        this.startDate = d;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(gameTag, flags);
        dest.writeParcelable(gameType, flags);
        dest.writeString(startDate);
        dest.writeString(gameID);
        dest.writeString(photoID);
        dest.writeLong(timePlayed);
    }

    public static final Parcelable.Creator<GameInfo> CREATOR
            = new Parcelable.Creator<GameInfo>() {

        @Override
        public GameInfo createFromParcel(Parcel source) {
            return new GameInfo(source);
        }

        @Override
        public GameInfo[] newArray(int size) {
            return new GameInfo[size];
        }
    };
}

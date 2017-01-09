package net.galliliu.jigsawpuzzle.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by galliliu on 16/5/15.
 */
public enum GameTag implements Parcelable {
    Default("Default"),
    Custom("Custom");

    private String tag;

    GameTag(String s) {
        this.tag = s;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeString(tag);
    }

    public static final Parcelable.Creator<GameTag> CREATOR
            = new Parcelable.Creator<GameTag>() {

        @Override
        public GameTag createFromParcel(Parcel source) {
            GameTag gt = GameTag.values()[source.readInt()];
            gt.setTag(source.readString());
            return gt;
        }

        @Override
        public GameTag[] newArray(int size) {
            return new GameTag[size];
        }
    };
}

package net.galliliu.jigsawpuzzle.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import net.galliliu.jigsawpuzzle.R;

/**
 * Created by galliliu on 16/5/13.
 */
public enum GameType implements Parcelable{
    GameTypeDefault(R.string.game_type_default),
    GameType3(R.string.game_type_3),
    GameType4(R.string.game_type_4),
    GameType5(R.string.game_type_5),
    GameType6(R.string.game_type_6),
    GameType7(R.string.game_type_7);

    public final static int TYPE_NUMBER3 = 3;
    public final static int TYPE_NUMBER4 = 4;
    public final static int TYPE_NUMBER5 = 5;
    public final static int TYPE_NUMBER6 = 6;
    public final static int TYPE_NUMBER7 = 7;
    private int resID;

    GameType(@StringRes int id) {
        this.resID = id;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public static GameType valueOfType(int type) {
        switch (type) {
            case TYPE_NUMBER3:
                return GameType3;
            case TYPE_NUMBER4:
                return GameType4;
            case TYPE_NUMBER5:
                return GameType5;
            case TYPE_NUMBER6:
                return GameType6;
            case TYPE_NUMBER7:
                return GameType7;
            default:
                return GameType3;
        }
    }

    public static int parserGameType(GameType type) {
        // Split photo according to game type, GameType1-3*3,GameType2-4*4,etc.
        switch (type.getResID()) {
            case R.string.game_type_3:
                return 3;
            case R.string.game_type_4:
                return 4;
            case R.string.game_type_5:
                return 5;
            case R.string.game_type_6:
                return 6;
            case R.string.game_type_7:
                return 7;
            default:
                return 3;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeInt(resID);
    }

    public static final Parcelable.Creator<GameType> CREATOR
            = new Parcelable.Creator<GameType>() {

        @Override
        public GameType createFromParcel(Parcel source) {
            GameType gl = GameType.values()[source.readInt()];
            gl.setResID(source.readInt());
            return gl;
        }

        @Override
        public GameType[] newArray(int size) {
            return new GameType[size];
        }
    };
}

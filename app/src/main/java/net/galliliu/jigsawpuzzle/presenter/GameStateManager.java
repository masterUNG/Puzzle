package net.galliliu.jigsawpuzzle.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.GameType;
import net.galliliu.jigsawpuzzle.bean.PhotoSlice;
import net.galliliu.jigsawpuzzle.ui.activity.GameActivity;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by galliliu on 16/5/23.
 */
public class GameStateManager implements Parcelable{
    // Indicate the blank puzzle element.
    public static final int BLANK_NUMBER = 0;
    // One second
    public static final int MILLISECOND = 1000;
    // Time out flag
    public static final long TIME_OUT = 100 * 60 * 60;

    private Context context;
    public GameInfo gameInfo;
    public LinkedList<PhotoSlice> bitmapLinkedList;
//    private LinkedList<PhotoSlice> bitmapLinkedListCache;
//    private LinkedList<PhotoSlice> bitmapLinkedListPuzzleCopy;
    public Bitmap photoSelected;
    public PhotoSlice photoLast;
    public boolean gameState;
    public boolean originalVisible;

    // Timer
    private Handler handler;
    private long timeCount;
    private Timer timer;

    // Give up dialog
    private boolean giveUpDialogDisplay;

    public GameStateManager(Context context, Handler handler, GameInfo gameInfo) {
        this.context = context;
        this.handler = handler;
        this.gameInfo = gameInfo;

        // Set default type
        setType(GameType.GameType3);

        // Resize photo to fit our screen
        photoSelected = PhotoManager.decodePhoto(context, gameInfo);
        DisplayMetrics metrics = SystemScreenUtil.getSystemScreenSize(context);
        photoSelected = PhotoManager.resizePhoto(photoSelected, metrics);

        bitmapLinkedList = new LinkedList<>();
        setGameState(false);
        setOriginalVisible(true);
        resetTime();
//        timer = new Timer();
//        timeCount = TIME_OUT - 10;

        setGiveUpDialogDisplay(false);
    }

    protected GameStateManager(Parcel in) {
        bitmapLinkedList = new LinkedList<>();
        gameInfo = in.readParcelable(GameInfo.class.getClassLoader());
        in.readTypedList(bitmapLinkedList, PhotoSlice.CREATOR);
        photoSelected = in.readParcelable(Bitmap.class.getClassLoader());
        photoLast = in.readParcelable(PhotoSlice.class.getClassLoader());
        gameState = in.readByte() != 0;
        originalVisible = in.readByte() != 0;
        timeCount = in.readLong();
    }

    public static final Creator<GameStateManager> CREATOR = new Creator<GameStateManager>() {
        @Override
        public GameStateManager createFromParcel(Parcel in) {
            return new GameStateManager(in);
        }

        @Override
        public GameStateManager[] newArray(int size) {
            return new GameStateManager[size];
        }
    };

    public void setContext(Context c) {
        this.context = c;
    }

    public void setHandler(Handler h) {
        this.handler = h;
    }

    public void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                timeCount++;
                bundle.putString(GameActivity.NEW_TIME,
                        context.getString(R.string.game_time) + " " + TimeUtil.formatTime(timeCount));
                message.setData(bundle);

                // handle time out
                if (timeCount > TIME_OUT) {
                    message.what = GameActivity.TIME_OUT_MESSAGE;
                    handler.sendMessage(message);
                    stopTimer();
                } else {
                    message.what = GameActivity.TIME_UPDATE_MESSAGE;
                    handler.sendMessage(message);
                }

            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, MILLISECOND);
    }

    public void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    public void resetTime() {
        timeCount = 0;
    }

    public long getTime() {
        return timeCount;
    }

    public void setGameState(boolean state) {
        gameState = state;
    }

    public void setOriginalVisible(boolean visible) {
        originalVisible = visible;
    }

    /**
     * Split this photo
     * */
    public void splitPhotoSelected(){
        bitmapLinkedList.clear();
        PhotoManager.splitPhoto(bitmapLinkedList, photoSelected, GameManager.parseGameType(gameInfo));
        photoLast = bitmapLinkedList.getLast();
        bitmapLinkedList.removeLast();
//        bitmapLinkedListCache = new LinkedList<>(bitmapLinkedList);
    }

    public void setType(GameType type) {
        this.gameInfo.setGameType(type);
    }

    public int getPhotoSliceHeight() {
        return photoLast.getPhoto().getHeight();
    }

    public int getGameType() {
        return GameType.parserGameType(gameInfo.getGameType());
    }

    /**
     * Generate a new puzzle
     */
    public void generatePuzzle() {
        int[] puzzle = Puzzle.randomGeneratePuzzle(GameManager.parseGameType(gameInfo));
        LinkedList<PhotoSlice> copy = new LinkedList<>(bitmapLinkedList);
        bitmapLinkedList.clear();

        // Transform
        for (int num : puzzle) {
            Log.i("Puzzle", "puzzle: " + num);
            if (num == BLANK_NUMBER) {
                bitmapLinkedList.add(new PhotoSlice(null, BLANK_NUMBER));
            } else {
                for (PhotoSlice slice : copy) {
                    if (slice.getNumber() == num) {
                        bitmapLinkedList.add(slice);
                        break;
                    }
                }
            }
        }

        // Copy the new puzzle
//        bitmapLinkedListPuzzleCopy = new LinkedList<>(bitmapLinkedList);
    }

    public boolean changeType(int type) {
        GameType gl = GameType.valueOfType(type);
        // type changed
        if (gl != gameInfo.getGameType()) {
            setType(gl);
            return true;
        }
        return false;
    }

    public boolean isPhotoSliceChangeable(PhotoSlice psSelected) {
        if (psSelected.getNumber() == BLANK_NUMBER) {
            return false;
        }

//        Log.i("Puzzle", "PhotoSliceSelected: " + psSelected.getNumber());

        PhotoSlice ps;
        int length = getGameType();
        int[][] puzzle = new int[length][];
        for (int i = 0; i < length; i++) {
            puzzle[i] = new int[length];
        }

        int row = 0, line = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                puzzle[i][j] = bitmapLinkedList.get(i * length + j).getNumber();
                if (puzzle[i][j] == psSelected.getNumber()) {
                    row = i;
                    line = j;
                }
            }
        }

//        Log.i("Puzzle", "row: " + row + " line:" + line);

        if (line-1 >= 0 && puzzle[row][line-1] == BLANK_NUMBER) {
            ps = bitmapLinkedList.get(row * length + line - 1);
            ps.setPhoto(psSelected.getPhoto());
            ps.setNumber(psSelected.getNumber());
            psSelected.setPhoto(null);
            psSelected.setNumber(BLANK_NUMBER);
//            Log.i("Puzzle", "row, line-1: " + ps.getNumber());
            return true;
        }

        if (line+1 < length && puzzle[row][line+1] == BLANK_NUMBER) {
            ps = bitmapLinkedList.get(row * length + line + 1);
            ps.setPhoto(psSelected.getPhoto());
            ps.setNumber(psSelected.getNumber());
            psSelected.setPhoto(null);
            psSelected.setNumber(BLANK_NUMBER);
//            Log.i("Puzzle", "row, line+1: " + ps.getNumber());
            return true;
        }

        if (row-1 >= 0 && puzzle[row-1][line] == BLANK_NUMBER) {
            ps = bitmapLinkedList.get((row - 1) * length + line);
            ps.setPhoto(psSelected.getPhoto());
            ps.setNumber(psSelected.getNumber());
            psSelected.setPhoto(null);
            psSelected.setNumber(BLANK_NUMBER);
//            Log.i("Puzzle", "row-1, line: " + ps.getNumber());
            return true;
        }

        if (row+1 < length && puzzle[row+1][line] == BLANK_NUMBER) {
            ps = bitmapLinkedList.get((row + 1) * length + line);
            ps.setPhoto(psSelected.getPhoto());
            ps.setNumber(psSelected.getNumber());
            psSelected.setPhoto(null);
            psSelected.setNumber(BLANK_NUMBER);
//            Log.i("Puzzle", "row+1, line: " + ps.getNumber());
            return true;
        }

        return false;
    }

    public boolean isGameOver() {
        for (int i = 0; i < bitmapLinkedList.size() - 1; i++) {
            if (bitmapLinkedList.get(i).getNumber() != i + 1) {
                return false;
            }
        }

        return true;
    }

    public void setGiveUpDialogDisplay(boolean isDisplay) {
        giveUpDialogDisplay = isDisplay;
    }

    public boolean isGiveUpDialogDisplay() {
        return giveUpDialogDisplay;
    }

    public void addLastPhotoSliceToList() {
        bitmapLinkedList.getLast().setPhoto(photoLast.getPhoto());
        bitmapLinkedList.getLast().setNumber(photoLast.getNumber());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(gameInfo, flags);
        dest.writeTypedList(bitmapLinkedList);
        dest.writeParcelable(photoSelected, flags);
        dest.writeParcelable(photoLast, flags);
        dest.writeByte((byte) (gameState ? 1 : 0));
        dest.writeByte((byte) (originalVisible ? 1 : 0));
        dest.writeLong(timeCount);
    }
}

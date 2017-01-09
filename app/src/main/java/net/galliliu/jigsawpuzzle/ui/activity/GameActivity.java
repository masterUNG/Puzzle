package net.galliliu.jigsawpuzzle.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.PhotoSlice;
import net.galliliu.jigsawpuzzle.presenter.GameStateManager;
import net.galliliu.jigsawpuzzle.presenter.RecordManager;
import net.galliliu.jigsawpuzzle.presenter.TimeUtil;
import net.galliliu.jigsawpuzzle.ui.adapter.GameItemGridAdapter;
import net.galliliu.jigsawpuzzle.ui.fragment.GameTypeChangeDialog;
import net.galliliu.jigsawpuzzle.ui.fragment.GiveUpDialogFragment;
import net.galliliu.jigsawpuzzle.ui.fragment.TimeOutDialogFragment;
import net.galliliu.jigsawpuzzle.ui.fragment.WinDialogFragment;
import net.galliliu.jigsawpuzzle.ui.listener.IGameTimeUpdateListener;
import net.galliliu.jigsawpuzzle.ui.listener.IGameTypeChangeListener;
import net.galliliu.jigsawpuzzle.ui.listener.IGiveUpGameDialogListener;
import net.galliliu.jigsawpuzzle.ui.listener.ITimeOutDialogButtonClickListener;
import net.galliliu.jigsawpuzzle.ui.listener.IWinDialogButtonClickListener;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener,
        IGameTypeChangeListener, IGameTimeUpdateListener, View.OnClickListener,
        IWinDialogButtonClickListener, ITimeOutDialogButtonClickListener,
        IGiveUpGameDialogListener {
    public static final int TIME_UPDATE_MESSAGE = 100;
    public static final int TIME_OUT_MESSAGE = 101;
    public static final String NEW_TIME = "new_time";
    public static final String GAME_STATE_MANAGER = "GAME_STATE_MANAGER";
    public static final String TITLE = "title";
    GameStateManager gameStateManager;
    RecordManager recordManager;
    GameItemGridAdapter gameItemGridAdapter;
    GridView gridView;
    ImageView imageView;
    TextView textViewType;
    TextView textViewTime;
    Button buttonOriginal;
    Button buttonStart;
    TimerMessageHandler handler;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        handler = new TimerMessageHandler(this);
        recordManager = new RecordManager(this);
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            GameInfo gameInfo = bundle.getParcelable(MainActivity.GAME_INFO);
            title = bundle.getString(MainActivity.GAME_TITLE);
            gameStateManager = new GameStateManager(this, handler, gameInfo);
        } else {
            gameStateManager = savedInstanceState.getParcelable(GAME_STATE_MANAGER);
            title = savedInstanceState.getString(TITLE);
            assert gameStateManager != null;
            gameStateManager.setHandler(handler);
            gameStateManager.setContext(this);
        }

        // Set activity title
        setTitle(title);

        textViewType = (TextView) findViewById(R.id.textViewType);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageView = (ImageView) findViewById(R.id.photo_selected);
        imageView.setImageBitmap(gameStateManager.photoSelected);
        gridView = (GridView) findViewById(R.id.game_item_grid_view);
        gameItemGridAdapter = new GameItemGridAdapter(this, gameStateManager.bitmapLinkedList);
        gridView.setAdapter(gameItemGridAdapter);
        gridView.setOnTouchListener(this);
        buttonOriginal = (Button) findViewById(R.id.button_original);
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonOriginal.setOnClickListener(this);
        buttonStart.setOnClickListener(this);

        // Restore game
        if (gameStateManager.gameState) {
            textViewType.setText(getString(gameStateManager.gameInfo.getGameType().getResID()));
            textViewTime.setText(getString(R.string.game_time) + " " + TimeUtil.formatTime(gameStateManager.getTime()));
            imageView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            gridView.setNumColumns(gameStateManager.getGameType());
            gameItemGridAdapter.notifyDataSetChanged();
            gameStateManager.setOriginalVisible(false);
            gameStateManager.startTimer();
            buttonStart.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_type_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_game_type:
                if (gameStateManager.gameState) {
                    Toast.makeText(this, R.string.game_type_change_warning, Toast.LENGTH_SHORT).show();
                    break;
                }
                GameTypeChangeDialog dialog = new GameTypeChangeDialog();
                dialog.show(getSupportFragmentManager(), "GameTypeChangeDialog");
                break;
            case android.R.id.home:
                if (gameStateManager.gameState) {
                    gameStateManager.stopTimer();
                    gameStateManager.setGiveUpDialogDisplay(true);
                    GiveUpDialogFragment giveUpDialog = new GiveUpDialogFragment();
                    giveUpDialog.show(getSupportFragmentManager(), "GiveUpDialogFragment");
                    return true;
                }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (gameStateManager.gameState) {
            gameStateManager.stopTimer();
            gameStateManager.setGiveUpDialogDisplay(true);
            GiveUpDialogFragment dialog = new GiveUpDialogFragment();
            dialog.show(getSupportFragmentManager(), "GiveUpDialogFragment");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameStateManager.gameState && !gameStateManager.isGiveUpDialogDisplay()) {
            gameStateManager.startTimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameStateManager.gameState) {
            gameStateManager.stopTimer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the current game state
        outState.putParcelable(GAME_STATE_MANAGER, gameStateManager);
        outState.putString(TITLE, title);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PhotoSlice ps = (PhotoSlice) gameItemGridAdapter.getItem(
                gridView.pointToPosition((int)event.getX(), (int)event.getY()));
        if (gameStateManager.isPhotoSliceChangeable(ps)) {
            gameItemGridAdapter.notifyDataSetChanged();
            if (gameStateManager.isGameOver()) {
                gameStateManager.stopTimer();
                gameStateManager.addLastPhotoSliceToList();
                gameItemGridAdapter.notifyDataSetChanged();
                gameStateManager.setOriginalVisible(true);
                gameStateManager.setGameState(false);
                Map best = recordManager.getBestRecord(gameStateManager.gameInfo,
                        gameStateManager.getTime());
                recordManager.insertRecord(gameStateManager.gameInfo,
                        gameStateManager.getTime(), TimeUtil.formatDateTime(new Date()));
                WinDialogFragment winDialog = WinDialogFragment.newInstance((int) best.get("title"),
                        (int) best.get("imgID"), gameStateManager.gameInfo.getGameType().getResID(),
                        gameStateManager.getTime(), (long) best.get("bestRecord"));
                winDialog.show(getSupportFragmentManager(), "WinDialogFragment");
            }
        }
        return true;
    }

    @Override
    public void onGameTypeItemClick(int type) {
        if (gameStateManager.changeType(type)) {
            textViewType.setText(getString(gameStateManager.gameInfo.getGameType().getResID()));
            Toast.makeText(this, R.string.game_type_change, Toast.LENGTH_SHORT).show();
        }
        gridView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        textViewTime.setText(R.string.game_time_text);
        buttonOriginal.setText(R.string.view_original);
    }

    @Override
    public void onGameTimeUpdate(String newTime) {
        textViewTime.setText(newTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_original:
                if (gameStateManager.gameState) {
                    if (gameStateManager.originalVisible) {
                        gridView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        buttonOriginal.setText(R.string.view_original);
                    } else {
                        gridView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        buttonOriginal.setText(R.string.back);
                    }
                    gameStateManager.originalVisible = !gameStateManager.originalVisible;
                } else {
                    gridView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    textViewTime.setText(R.string.game_time_text);
                    gameStateManager.originalVisible = true;
                }
                break;
            case R.id.button_start:
                imageView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                gridView.setNumColumns(gameStateManager.getGameType());
                gameStateManager.splitPhotoSelected();
                gameStateManager.generatePuzzle();
                gameItemGridAdapter.notifyDataSetChanged();
                gameStateManager.setOriginalVisible(false);
                gameStateManager.setGameState(true);
                gameStateManager.resetTime();
                gameStateManager.startTimer();
                buttonStart.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onWinDialogBackButtonClick() {
        buttonStart.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWinDialogTryAgainButtonClick() {
        gameStateManager.generatePuzzle();
        gameItemGridAdapter.notifyDataSetChanged();
        gameStateManager.setOriginalVisible(false);
        gameStateManager.setGameState(true);
        gameStateManager.resetTime();
        gameStateManager.startTimer();
        buttonStart.setVisibility(View.GONE);
    }

    @Override
    public void onTimeOutDialogPositiveClick() {
        textViewTime.setText(R.string.game_time_text);
        buttonOriginal.setText(R.string.view_original);
        gridView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        gameStateManager.setOriginalVisible(true);
        gameStateManager.resetTime();
        gameStateManager.setGameState(false);
        buttonStart.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGiveUpGameDialogPositiveClick() {
        gameStateManager.stopTimer();
        this.finish();
    }

    @Override
    public void onGiveUpGameDialogNegativeClick() {
        gameStateManager.setGiveUpDialogDisplay(false);
        if (gameStateManager.gameState) {
            gameStateManager.startTimer();
        }
    }

    private static class TimerMessageHandler extends Handler {
        WeakReference<GameActivity> weakReference;

        TimerMessageHandler(GameActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GameActivity activity = weakReference.get();
            switch (msg.what) {
                case TIME_UPDATE_MESSAGE:
                    activity.onGameTimeUpdate(msg.getData().getString(NEW_TIME));
                    break;
                case TIME_OUT_MESSAGE:
                    TimeOutDialogFragment dialog = new TimeOutDialogFragment();
                    dialog.show(activity.getSupportFragmentManager(), "TimeOutDialogFragment");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}

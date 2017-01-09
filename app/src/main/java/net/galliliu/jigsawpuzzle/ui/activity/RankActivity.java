package net.galliliu.jigsawpuzzle.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.bean.GameType;
import net.galliliu.jigsawpuzzle.presenter.RecordManager;
import net.galliliu.jigsawpuzzle.ui.adapter.RecordExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankActivity extends AppCompatActivity {
    public static final String GAME_INFO = "info";
    public static final String GAME_TITLE = "title";
    private RecordManager recordManager;
    private GameInfo gameInfo;
    private RecordExpandableListAdapter adapter;
    private List<String> groupList;
    private List<List<Map>> childList;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        if (savedInstanceState == null) {
            gameInfo = getIntent().getParcelableExtra(MainActivity.GAME_INFO);
            title = getIntent().getStringExtra(MainActivity.GAME_TITLE);
        } else {
            gameInfo = savedInstanceState.getParcelable(GAME_INFO);
            title = savedInstanceState.getString(GAME_TITLE);
        }
        setTitle(title);

        recordManager = new RecordManager(this);
        groupList = new ArrayList<>();
        groupList.add(getString(R.string.game_type_3));
        groupList.add(getString(R.string.game_type_4));
        groupList.add(getString(R.string.game_type_5));
        groupList.add(getString(R.string.game_type_6));
        groupList.add(getString(R.string.game_type_7));

        childList = new ArrayList<>();
        initData();

        adapter = new RecordExpandableListAdapter(this, groupList, childList);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListView);
        listView.setAdapter(adapter);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        listView.setIndicatorBounds(width - 90, width - 10);

        // Expand all records
        for (int i = 0; i < groupList.size(); i++) {
            listView.expandGroup(i);
        }
    }

    void initData() {
        childList.clear();
        for (int i = GameType.TYPE_NUMBER3; i <=  GameType.TYPE_NUMBER7; i++) {
            childList.add(recordManager.getRecords(gameInfo.getGameID(), String.valueOf(i)));
//            for (int j = 0; j < childList.size(); j++) {
//                for (int k = 0; k < childList.get(j).size(); k++) {
//                    HashMap m = (HashMap) childList.get(j).get(k);
//                    Log.i("Records", TableColumnConstant.GAME_TAG + ":" + m.get(TableColumnConstant.GAME_TAG) + " "
//                            + TableColumnConstant.GAME_ID + ":" + m.get(TableColumnConstant.GAME_ID) + " "
//                            + TableColumnConstant.PHOTO_ID + ":" + m.get(TableColumnConstant.PHOTO_ID) + " "
//                            + TableColumnConstant.TYPE + ":" + m.get(TableColumnConstant.TYPE) + " "
//                            + TableColumnConstant.TIME + ":" + m.get(TableColumnConstant.TIME) + " "
//                            + TableColumnConstant.TIME_LONG + ":" + m.get(TableColumnConstant.TIME_LONG) + " "
//                            + TableColumnConstant.END_DATE + ":" + m.get(TableColumnConstant.END_DATE) + " "
//                            + RecordManager.RECORD_RANK + ":" + m.get(RecordManager.RECORD_RANK)
//                    );
//                }
//            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(GAME_INFO, gameInfo);
        outState.putString(GAME_TITLE, title);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset_all:
                recordManager.resetAll(gameInfo);
                initData();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

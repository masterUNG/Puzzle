package net.galliliu.jigsawpuzzle.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.presenter.GameManager;
import net.galliliu.jigsawpuzzle.presenter.PhotoManager;
import net.galliliu.jigsawpuzzle.presenter.RecordManager;
import net.galliliu.jigsawpuzzle.ui.adapter.MainGameGridAdapter;
import net.galliliu.jigsawpuzzle.ui.fragment.DeleteGameDialogFragment;
import net.galliliu.jigsawpuzzle.ui.fragment.GameItemLongClickDialog;
import net.galliliu.jigsawpuzzle.ui.fragment.GamePhotoLostDialogFragment;
import net.galliliu.jigsawpuzzle.ui.fragment.NewGameDialogFragment;
import net.galliliu.jigsawpuzzle.ui.listener.IDeleteGameDialogFragmentListener;
import net.galliliu.jigsawpuzzle.ui.listener.IGameItemLongClickListener;
import net.galliliu.jigsawpuzzle.ui.listener.INewGameDialogFragmentListener;

import java.util.Map;

//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, IDeleteGameDialogFragmentListener,
        INewGameDialogFragmentListener, IGameItemLongClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "MainActivity";
    public static final String GAME_INFO = "game_info";
    public static final String GAME_TITLE = "game_title";
    public static final int OPEN_CAMERA_REQUEST_CODE = 100;
    public static final int OPEN_GALLERY_REQUEST_CODE = 101;
    public static final int CROP_REQUEST_CODE = 102;

    GameManager gameManager;
    PhotoManager photoManager;
    RecordManager recordManager;
    MainGameGridAdapter mainGridAdapter;
//    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initImageLoader(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        gameManager = new GameManager(this);
        photoManager = new PhotoManager(this);
        recordManager = new RecordManager(this);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        mainGridAdapter = new MainGameGridAdapter(this, gameManager.getGameInfoList());
        gridView.setAdapter(mainGridAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
//        gridView.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));
    }

    private void initImageLoader(Context context) {
//        // Release memory
//        if (ImageLoader.getInstance().isInited()) {
//            ImageLoader.getInstance().clearMemoryCache();
//            ImageLoader.getInstance().destroy();
//        }
//
//        // UIL framwork config
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.memoryCache(new WeakMemoryCache());
//        config.memoryCacheSize(2 * 1024 * 1024);
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        //        config.writeDebugLogs(); // Remove for release app
//
//        // Initialize ImageLoader with configuration.
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                DialogFragment dialogFragment = new NewGameDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "NewGameDialogFragment");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = null;
        String content;

        switch (item.getItemId()) {
//            case R.id.nav_history_record_main:
//                intent = new Intent(this, RankActivity.class);
//                break;
//            case R.id.nav_setting_main:
//                break;
            case R.id.nav_help_main:
                intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.EXTRA_SHOW_FRAGMENT,
                        HelpActivity.HelpPreferenceFragment.class.getName());
                intent.putExtra(HelpActivity.EXTRA_NO_HEADERS, true);
                break;
            case R.id.nav_about_main:
                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.nav_share_main:
                intent = new Intent();
                content = recordManager.getAllBestRecords(gameManager.getGameInfoList());
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, content);
                intent.setType("text/plain");
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        imageLoader.clearDiskCache();
//        imageLoader.clearMemoryCache();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GameActivity.class);
        Map map = (Map) parent.getItemAtPosition(position);
        boolean gamePhotoLost = (boolean) map.get(GameManager.GAME_PHOTO_LOST_KEY);

        if (gamePhotoLost) {
            mainGridAdapter.notifyDataSetChanged();
            GamePhotoLostDialogFragment dialog = new GamePhotoLostDialogFragment();
            dialog.show(getSupportFragmentManager(), "GamePhotoLostDialogFragment");
            return;
        }

        GameInfo gameInfo = (GameInfo) map.get(GameManager.GAME_INFO_KEY);
        intent.putExtra(GAME_INFO, gameInfo);
        String title;
        title = formatTitle((int) map.get(GameManager.GAME_NUMBER_KEY), gameInfo);
        intent.putExtra(GAME_TITLE, title);
        startActivity(intent);
    }

    public String formatTitle(int number, GameInfo gameInfo) {
        String defaultGameTag = getString(R.string.game_default);
        String customGameTag = getString(R.string.game_custom);
        String hint = GameManager.isDefaultGame(gameInfo) ? defaultGameTag : customGameTag;
        hint += " " + number;

        return hint;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Map map = (Map) parent.getItemAtPosition(position);
        GameInfo gameInfo = (GameInfo) map.get(GameManager.GAME_INFO_KEY);
        String title;
        title = formatTitle((int) map.get(GameManager.GAME_NUMBER_KEY), gameInfo);

        if (GameManager.isDefaultGame(gameInfo)) {
//            Toast.makeText(this, getString(R.string.could_not_delete_default_game), Toast.LENGTH_SHORT).show();
            onRecordButtonClick(gameInfo, title);
            return true;
        }

        // Game photo lost handle
        boolean gamePhotoLost = (boolean) map.get(GameManager.GAME_PHOTO_LOST_KEY);
        if (gamePhotoLost) {
            mainGridAdapter.notifyDataSetChanged();
            onDeleteButtonClick(gameInfo);
            return true;
        }

        // Display dialog
        GameItemLongClickDialog dialog = GameItemLongClickDialog.newInstance(gameInfo, title);
        dialog.show(getSupportFragmentManager(), "GameItemLongClickDialog");
        return true;
    }

    @Override
    public void onDeleteGameDialogPositiveClick(GameInfo gameInfo) {
        // delete that game and update gameInfolist
        Picasso.with(this).invalidate(PhotoManager.parseCustomGamePhotoUri(gameInfo));
        gameManager.deleteGame(gameInfo);
        recordManager.deleteRecords(gameInfo);
        photoManager.deletePhoto(gameInfo);
        gameManager.updateGameList();
        gameManager.updateGameList();
        mainGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteGameDialogNegativeClick(GameInfo gameInfo) {
    }

    @Override
    public void onFromNewGameDialogCameraItemClick() {
        int photoID = gameManager.createGamePhotoID();

        if (gameManager.isExceedMaxGameAmount(photoID)) {
            Toast.makeText(this, getString(R.string.exceed_max_game_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        photoManager.setPhotoID(String.valueOf(photoID));
        if (!photoManager.createPhotoFile()){
            Toast.makeText(this, getString(R.string.could_not_take_photo), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoManager.getTempFileUri());
        startActivityForResult(openCameraIntent, OPEN_CAMERA_REQUEST_CODE);
    }

    @Override
    public void onFromNewGameDialogGalleryItemClick() {
        int photoID = gameManager.createGamePhotoID();

        if (gameManager.isExceedMaxGameAmount(photoID)) {
            Toast.makeText(this, getString(R.string.exceed_max_game_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        photoManager.setPhotoID(String.valueOf(photoID));
        if (!photoManager.createPhotoFile()){
            Toast.makeText(this, "没有足够的可用空间!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openGalleryIntent.setType("image/*");
        openGalleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoManager.getTempFileUri());
        startActivityForResult(openGalleryIntent, OPEN_GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent;
            Uri uri;

            switch (requestCode) {
                case OPEN_GALLERY_REQUEST_CODE:
//                    intent = photoManager.cropPhoto(data.getData());
//                    startActivityForResult(intent, CROP_REQUEST_CODE);
                    uri = photoManager.getTempFileUri();
                    Crop.of(data.getData(), uri).asSquare().start(this);
                    break;
                case OPEN_CAMERA_REQUEST_CODE:
//                    intent = photoManager.cropPhoto(photoManager.getTempFileUri());
//                    startActivityForResult(intent, CROP_REQUEST_CODE);
                    uri = photoManager.getTempFileUri();
                    Crop.of(uri, uri).asSquare().start(this);
                    break;
                case Crop.REQUEST_CROP:
                    gameManager.newGame(photoManager.newGameInfo());
                    gameManager.updateGameList();
                    mainGridAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        } else if (requestCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDeleteButtonClick(GameInfo gameInfo) {
        DialogFragment dialogFragment = DeleteGameDialogFragment.newInstance(gameInfo);
        dialogFragment.show(getSupportFragmentManager(), "DeleteGameDialogFragment");
    }

    @Override
    public void onRecordButtonClick(GameInfo gameInfo, String title) {
        Intent intent = new Intent(this, RankActivity.class);
        intent.putExtra(MainActivity.GAME_INFO, gameInfo);
        intent.putExtra(MainActivity.GAME_TITLE, title);
        startActivity(intent);
    }
}

package net.galliliu.jigsawpuzzle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.presenter.GameManager;
import net.galliliu.jigsawpuzzle.presenter.PhotoManager;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by galliliu on 16/5/23.
 */
public class MainGameGridAdapter extends BaseAdapter {
    private Context context;
    private LinkedList<Map> gameInfoLinkedList;
//    private DisplayImageOptions options;
//    private AnimateFirstDisplayListener animateListener;

    public MainGameGridAdapter(Context c, LinkedList<Map> list){
        this.context = c;
        this.gameInfoLinkedList = list;
//        Resources resources = context.getResources();
//        this.options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.photolost)
//                .showImageOnFail(R.drawable.photolost)
//                .showImageForEmptyUri(R.drawable.photolost)
//                .cacheInMemory(true)
//                .considerExifParams(true)
//                .cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();
//        animateListener = new AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        return gameInfoLinkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameInfoLinkedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameHolderView gameHolderView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_games_layout, null);
            gameHolderView = new GameHolderView();
            gameHolderView.imageView = (ImageView) convertView.findViewById(R.id.game_image_view);
            gameHolderView.textView = (TextView) convertView.findViewById(R.id.game_textview);
            convertView.setTag(gameHolderView);
        } else {
            gameHolderView = (GameHolderView) convertView.getTag();
        }

        Map map = gameInfoLinkedList.get(position);
        GameInfo gameInfo = (GameInfo) map.get(GameManager.GAME_INFO_KEY);
        boolean isLost = (boolean) map.get(GameManager.GAME_PHOTO_LOST_KEY);

        if (GameManager.isDefaultGame(gameInfo)) {
//            gameHolderView.imageView.setImageResource(PhotoManager.parseDefaultGamePhotoID(gameInfo));
            if (isLost) {
                Picasso.with(context)
                        .load(R.drawable.photolost)
                        .resizeDimen(R.dimen.game_list_item_column_width, R.dimen.game_list_item_column_height)
                        .into(gameHolderView.imageView);
            } else {
                Picasso.with(context)
                        .load(PhotoManager.parseDefaultGamePhotoID(gameInfo))
                        .resizeDimen(R.dimen.game_list_item_column_width, R.dimen.game_list_item_column_height)
                        .into(gameHolderView.imageView);
            }
        } else {
//            String path = PhotoManager.parseCustomGamePhotoPath(gameInfo);
//            ImageLoader.getInstance().displayImage(
//                    "file://" + path,
//                    gameHolderView.imageView,
//                    options);
            if (isLost) {
                Picasso.with(context)
                        .load(R.drawable.photolost)
                        .resizeDimen(R.dimen.game_list_item_column_width, R.dimen.game_list_item_column_height)
                        .into(gameHolderView.imageView);
            } else {
                Picasso.with(context)
                        .load(PhotoManager.parseCustomGamePhotoUri(gameInfo))
                        .resizeDimen(R.dimen.game_list_item_column_width, R.dimen.game_list_item_column_height)
                        .into(gameHolderView.imageView);
            }
        }

        int game_number = (int) map.get(GameManager.GAME_NUMBER_KEY);
        String defaultGameTag = context.getString(R.string.game_default);
        String customGameTag = context.getString(R.string.game_custom);
        String hint = GameManager.isDefaultGame(gameInfo) ? defaultGameTag : customGameTag;
        hint += " " + game_number;

        gameHolderView.textView.setText(hint);
        return convertView;
    }

    private static class GameHolderView {
        private ImageView imageView;
        private TextView textView;
    }

//    public static DisplayImageOptions getDisplayOptions(
//            BitmapFactory.Options options) {
//        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.photolost)
//                .showImageOnFail(R.drawable.photolost)
//                .showImageForEmptyUri(R.drawable.photolost)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .resetViewBeforeLoading(true)
//                .cacheInMemory(true)
//                .considerExifParams(true);
//        if (options != null) {
//            builder = builder.decodingOptions(options);
//        }
//        return builder.build();
//    }
//
//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//        return inSampleSize;
//    }
//
//    public static BitmapFactory.Options getDecodingOptions(String path, int reqWidth, int reqHeight) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//        options.inJustDecodeBounds = false;
//        return options;
//    }

//    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    FadeInBitmapDisplayer.animate(imageView, 100);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//    }
}

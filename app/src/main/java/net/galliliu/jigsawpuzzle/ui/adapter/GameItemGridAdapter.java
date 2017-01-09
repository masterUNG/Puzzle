package net.galliliu.jigsawpuzzle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.PhotoSlice;
import net.galliliu.jigsawpuzzle.presenter.Puzzle;

import java.util.LinkedList;

/**
 * Created by galliliu on 16/5/23.
 */
public class GameItemGridAdapter extends BaseAdapter{
    private Context context;
    private LinkedList<PhotoSlice> photoSliceLinkedList;

    public GameItemGridAdapter(Context context, LinkedList<PhotoSlice> list) {
        this.context = context;
        this.photoSliceLinkedList = list;
    }

    @Override
    public int getCount() {
        return photoSliceLinkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoSliceLinkedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameItemHolderView gameItemHolderView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_game_item_layout, null);
            gameItemHolderView = new GameItemHolderView();
            gameItemHolderView.imageView = (ImageView) convertView.findViewById(R.id.game_item_imageView);
            convertView.setTag(gameItemHolderView);
        } else {
            gameItemHolderView = (GameItemHolderView) convertView.getTag();
        }

        PhotoSlice slice = photoSliceLinkedList.get(position);
        gameItemHolderView.imageView.setImageBitmap(slice.getPhoto());
        if (slice.getNumber() == Puzzle.BLANK_NUMBER) {
            gameItemHolderView.imageView.setVisibility(View.GONE);
        } else {
            gameItemHolderView.imageView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private static class GameItemHolderView {
        private ImageView imageView;
    }
}

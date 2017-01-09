package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.ui.activity.MainActivity;
import net.galliliu.jigsawpuzzle.ui.listener.IGameItemLongClickListener;

/**
 * Created by galliliu on 16/6/2.
 */
public class GameItemLongClickDialog extends DialogFragment{
    private IGameItemLongClickListener listener;

    public static GameItemLongClickDialog newInstance(GameInfo gameInfo, String title) {
        GameItemLongClickDialog fragment =  new GameItemLongClickDialog();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.GAME_INFO, gameInfo);
        args.putString(MainActivity.GAME_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the MainActivity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (IGameItemLongClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGameItemLongClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final GameInfo gameInfo = getArguments().getParcelable(MainActivity.GAME_INFO);
        final String title = getArguments().getString(MainActivity.GAME_TITLE);
        return new AlertDialog.Builder(getActivity())
                .setItems(getResources().getStringArray(R.array.game_item_long_click),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        listener.onDeleteButtonClick(gameInfo);
                                        break;
                                    case 1:
                                        listener.onRecordButtonClick(gameInfo, title);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                .create();
    }
}

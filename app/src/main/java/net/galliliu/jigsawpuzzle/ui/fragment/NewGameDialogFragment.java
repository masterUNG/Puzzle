package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.ui.listener.INewGameDialogFragmentListener;

/**
 * Created by galliliu on 16/5/23.
 */
public class NewGameDialogFragment extends DialogFragment {
    private INewGameDialogFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the MainActivity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (INewGameDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement INewGameDialogFragmentListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setItems(getResources().getStringArray(R.array.new_game),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        listener.onFromNewGameDialogGalleryItemClick();
                                        break;
                                    case 1:
                                        listener.onFromNewGameDialogCameraItemClick();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                .create();
    }
}

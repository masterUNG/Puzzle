package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import net.galliliu.jigsawpuzzle.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by galliliu on 16/6/13.
 */
public class GamePhotoLostDialogFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.game_photo_lost))
                .setContentText(getString(R.string.game_photo_lost_text))
                .setConfirmText(getString(R.string.button_yes));
    }
}

package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.ui.listener.IGiveUpGameDialogListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by galliliu on 16/5/29.
 */
public class GiveUpDialogFragment extends DialogFragment{
    private IGiveUpGameDialogListener listener;
    private boolean isCancelClick = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (IGiveUpGameDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGiveUpGameDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                .setTitle(R.string.give_up_game)
//                .setPositiveButton(R.string.button_yes,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                listener.onGiveUpGameDialogPositiveClick();
//                            }
//                        })
//                .setNegativeButton(R.string.button_no,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                listener.onGiveUpGameDialogNegativeClick();
//                            }
//                        })
//                .create();
        return new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.give_up_game))
                .setCancelText(getString(R.string.button_no))
                .setConfirmText(getString(R.string.button_yes))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        isCancelClick = false;
                        sDialog.cancel();
                        listener.onGiveUpGameDialogPositiveClick();
                    }
                });

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (isCancelClick) {
            listener.onGiveUpGameDialogNegativeClick();
        }
        super.onCancel(dialog);
    }
}

package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.bean.GameInfo;
import net.galliliu.jigsawpuzzle.ui.activity.MainActivity;
import net.galliliu.jigsawpuzzle.ui.listener.IDeleteGameDialogFragmentListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by galliliu on 16/5/23.
 */
public class DeleteGameDialogFragment extends DialogFragment {
    private IDeleteGameDialogFragmentListener listener;

    public static DeleteGameDialogFragment newInstance(GameInfo gameInfo) {
        DeleteGameDialogFragment fragment =  new DeleteGameDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.GAME_INFO, gameInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the MainActivity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (IDeleteGameDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDeleteGameDialogFragmentListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final GameInfo gameInfo = getArguments().getParcelable(MainActivity.GAME_INFO);

//        return new AlertDialog.Builder(getActivity())
//                .setMessage(R.string.delete_game_dialog_message)
//                .setPositiveButton(R.string.delete_game_dialog_delete,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                listener.onDeleteGameDialogPositiveClick(gameInfo);
//                            }
//                        })
//                .setNegativeButton(R.string.delete_game_dialog_cancel,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                listener.onDeleteGameDialogNegativeClick(gameInfo);
//                            }
//                        })
//                .create();
        return new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.delete_game_dialog_message))
                .setCancelText(getString(R.string.delete_game_dialog_cancel))
                .setConfirmText(getString(R.string.delete_game_dialog_delete))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText(getString(R.string.delete_game_dialog_canceled))
                                .setConfirmText(getString(R.string.delete_game_dialog_ok))
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        listener.onDeleteGameDialogNegativeClick(gameInfo);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText(getString(R.string.delete_game_dialog_deleted))
                                .setConfirmText(getString(R.string.delete_game_dialog_ok))
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        listener.onDeleteGameDialogPositiveClick(gameInfo);
                    }
                });
    }
}

package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.ui.listener.ITimeOutDialogButtonClickListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by galliliu on 16/5/29.
 */
public class TimeOutDialogFragment extends DialogFragment{
    private ITimeOutDialogButtonClickListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the GameActivity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (ITimeOutDialogButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ITimeOutDialogButtonClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("Warning")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setMessage(R.string.time_out)
//                .setPositiveButton(R.string.button_yes,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                listener.onTimeOutDialogPositiveClick();
//                            }
//                        })
//                .create();
        return new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(getString(R.string.time_out))
                .setCustomImage(android.R.drawable.ic_dialog_alert)
                .setConfirmText(getString(R.string.delete_game_dialog_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        listener.onTimeOutDialogPositiveClick();
                        sweetAlertDialog.cancel();
                    }
                });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onTimeOutDialogPositiveClick();
        super.onCancel(dialog);
    }
}

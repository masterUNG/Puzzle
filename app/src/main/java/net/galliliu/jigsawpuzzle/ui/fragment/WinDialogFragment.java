package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.presenter.RecordManager;
import net.galliliu.jigsawpuzzle.presenter.TimeUtil;
import net.galliliu.jigsawpuzzle.ui.listener.IWinDialogButtonClickListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by galliliu on 16/5/26.
 */
public class WinDialogFragment extends DialogFragment {
    private IWinDialogButtonClickListener listener;
    private @StringRes int title;
    private @DrawableRes int imgID;
    private @StringRes int type;
    private long time;
    private long bestRecord;
    private boolean isBackButtonClick = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the GameActivity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (IWinDialogButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IWinDialogButtonClickListener");
        }
    }

    /**
     * Create a new instance of WinDialogFragment, providing "title, type, time, imgID, bestRecord"
     * as an argument.
     */
    public static WinDialogFragment newInstance(@StringRes int title, @DrawableRes int imgID,
                                                @StringRes int type, long time, long record) {
        WinDialogFragment dialog = new WinDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("imgID", imgID);
        args.putInt("type", type);
        args.putLong("time", time);
        args.putLong("bestRecord", record);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        title = args.getInt("title");
        imgID = args.getInt("imgID");
        type = args.getInt("type");
        time = args.getLong("time");
        bestRecord = args.getLong("bestRecord");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle(R.string.dialog_title_first);
//        dialog.setContentView(R.layout.win_dialog_layout);
//
//        dialog.setTitle(title);
//        ImageView trophyImg = (ImageView) dialog.findViewById(R.id.imageview_win_dialog);
//        trophyImg.setImageResource(imgID);
//        TextView textViewType = (TextView) dialog.findViewById(R.id.textview_type_dialog);
//        textViewType.setText(type);
//        TextView textViewThisRecord = (TextView) dialog.findViewById(R.id.textview_this_record_dialog);
//        textViewThisRecord.setText(TimeUtil.formatTime(time));
//        TextView textViewHistoryRecord = (TextView) dialog.findViewById(R.id.textview_history_time_dialog);
//        if (bestRecord == RecordManager.RECORD_NOT_FOUND) {
//            textViewHistoryRecord.setText(R.string.no_history_record);
//        } else {
//            textViewHistoryRecord.setText(TimeUtil.formatTime(bestRecord));
//        }
//
//        Button buttonBack = (Button) dialog.findViewById(R.id.button_win_dialog_back);
//        Button buttonAgain = (Button) dialog.findViewById(R.id.button_win_dialog_again);
//        buttonBack.setOnClickListener(this);
//        buttonAgain.setOnClickListener(this)
//        return dialog;

        String bestTime = bestRecord == RecordManager.RECORD_NOT_FOUND ?
                getString(R.string.no_history_record) : String.valueOf(TimeUtil.formatTime(bestRecord));
        String content = getString(type) + "\n"
                + getString(R.string.this_record) + " " + TimeUtil.formatTime(time) + "\n"
                + getString(R.string.history_best_record) + " " + bestTime;

        return new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(getString(title))
                .setCustomImage(imgID)
                .showCancelButton(true)
                .setCancelText(getString(R.string.dialog_button_back))
                .setConfirmText(getString(R.string.dialog_button_try_again))
                .setContentText(content)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        isBackButtonClick = false;
                        sweetAlertDialog.cancel();
                        listener.onWinDialogTryAgainButtonClick();
                    }
                });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button_win_dialog_back:
//                isBackButtonClick = false;
//                listener.onWinDialogBackButtonClick();
//                break;
//            case R.id.button_win_dialog_again:
//                isBackButtonClick = false;
//                listener.onWinDialogTryAgainButtonClick();
//                break;
//            default:
//                break;
//        }
//        this.dismiss();
//    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (isBackButtonClick) {
            listener.onWinDialogBackButtonClick();
        }
        super.onCancel(dialog);
    }
}


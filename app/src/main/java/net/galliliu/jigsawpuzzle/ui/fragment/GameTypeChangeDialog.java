package net.galliliu.jigsawpuzzle.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.ui.listener.IGameTypeChangeListener;

/**
 * Created by galliliu on 16/5/25.
 */
public class GameTypeChangeDialog extends DialogFragment {
    private IGameTypeChangeListener listener;
    private String[] type;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the MainActivity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (IGameTypeChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGameTypeChangeListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        type = new String[] {
            getString(R.string.game_type_3),
            getString(R.string.game_type_4),
            getString(R.string.game_type_5),
            getString(R.string.game_type_6),
            getString(R.string.game_type_7)
        };
        final int[] l = {3, 4, 5, 6, 7};

        return new AlertDialog.Builder(getActivity())
                .setItems(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onGameTypeItemClick(l[which]);
                    }
                })
                .create();
    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.game_type_dialog_layout, container, true);
//        ListView listView = (ListView) view.findViewById(R.id.game_type_dialog_listview);
//        type = new String[] {
//                getString(R.string.game_type_3),
//                getString(R.string.game_type_4),
//                getString(R.string.game_type_5),
//                getString(R.string.game_type_6),
//                getString(R.string.game_type_7)
//        };
//        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
//                R.layout.game_type_dialog_item_layout,
//                R.id.game_type_textview,
//                type);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
//
//        Dialog dialog = getDialog();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        return view;
//    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        listener.onGameTypeItemClick(type[position]);
//    }
}

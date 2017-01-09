package net.galliliu.jigsawpuzzle.ui.listener;

import net.galliliu.jigsawpuzzle.bean.GameInfo;

/**
 * Created by galliliu on 16/5/18.
 */
public interface IDeleteGameDialogFragmentListener {
    void onDeleteGameDialogPositiveClick(GameInfo gameInfo);
    void onDeleteGameDialogNegativeClick(GameInfo gameInfo);
}

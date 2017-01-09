package net.galliliu.jigsawpuzzle.ui.listener;

import net.galliliu.jigsawpuzzle.bean.GameInfo;

/**
 * Created by galliliu on 16/6/2.
 */
public interface IGameItemLongClickListener {
    void onDeleteButtonClick(GameInfo gameInfo);
    void onRecordButtonClick(GameInfo gameInfo, String title);
}

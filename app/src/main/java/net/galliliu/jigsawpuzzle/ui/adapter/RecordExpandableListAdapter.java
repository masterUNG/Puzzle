package net.galliliu.jigsawpuzzle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.galliliu.jigsawpuzzle.R;
import net.galliliu.jigsawpuzzle.model.TableColumnConstant;
import net.galliliu.jigsawpuzzle.presenter.RecordManager;

import java.util.List;
import java.util.Map;

/**
 * Created by galliliu on 16/6/2.
 */
public class RecordExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> groupList;
    private List<List<Map>> childList;

    public RecordExpandableListAdapter(Context context, List<String> groupList, List<List<Map>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolderView group;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.expandlist_group_layout, parent, false);
            group = new GroupHolderView();
            group.typeTextView = (TextView) convertView.findViewById(R.id.textView_type_rank);
            convertView.setTag(group);
        } else {
            group = (GroupHolderView) convertView.getTag();
        }
        group.typeTextView.setText(groupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolderView child;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.expandlist_child_layout, parent, false);
            child = new ChildHolderView();
            child.rankImageView = (ImageView) convertView.findViewById(R.id.img_rank);
            child.rankTextView = (TextView) convertView.findViewById(R.id.textView_rank);
            child.timeTextView = (TextView) convertView.findViewById(R.id.textView_time);
            child.dateTextView = (TextView) convertView.findViewById(R.id.textView_date);
            convertView.setTag(child);
        } else {
            child = (ChildHolderView) convertView.getTag();
        }

        int rank = (int) childList.get(groupPosition).get(childPosition).get(RecordManager.RECORD_RANK);
        switch (rank) {
            case RecordManager.FIRST:
                child.rankImageView.setImageResource(R.drawable.trophy_gold);
                break;
            case RecordManager.SECOND:
                child.rankImageView.setImageResource(R.drawable.trophy_silver);
                break;
            case RecordManager.THIRD:
                child.rankImageView.setImageResource(R.drawable.trophy_bronze);
                break;
            default:
                child.rankImageView.setImageResource(R.drawable.trophy_black);
                break;
        }
        child.rankTextView.setText(String.valueOf(rank));
        child.timeTextView.setText(String.valueOf(childList.get(groupPosition).get(childPosition)
                .get(TableColumnConstant.TIME)));
        child.dateTextView.setText(String.valueOf(childList.get(groupPosition).get(childPosition)
                .get(TableColumnConstant.END_DATE)));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class GroupHolderView {
        private TextView typeTextView;
    }

    private static class ChildHolderView {
        private ImageView rankImageView;
        private TextView rankTextView;
        private TextView timeTextView;
        private TextView dateTextView;
    }
}

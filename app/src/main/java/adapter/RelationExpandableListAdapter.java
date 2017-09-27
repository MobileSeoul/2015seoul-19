package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hany.houseofcartoon.R;

import java.util.ArrayList;

import db.entry.Book;

/**
 * Created by HanyLuv on 2015-10-28.
 */
public class RelationExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> groupList;
    private ArrayList<ArrayList<Book>> childList;
    private LayoutInflater inflater;
    private Context context;
    private Typeface typeface;

    public RelationExpandableListAdapter(Context context, ArrayList<String> groupList, ArrayList<ArrayList<Book>> childList) {

        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        inflater = LayoutInflater.from(context);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
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
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Book getChild(int groupPosition, int childPosition) {
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.relation_group_itme, null);
            viewHolder.tv_groupName = (TextView) convertView.findViewById(R.id.parent_text);
            viewHolder.total_serch_text = (TextView) convertView.findViewById(R.id.total_serch_text);
            viewHolder.parent_top_divider = (View) convertView.findViewById(R.id.parent_top_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder == null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_groupName.setText(Html.fromHtml(getGroup(groupPosition)), TextView.BufferType.SPANNABLE);
        if (groupPosition == 0) {
            viewHolder.parent_top_divider.setVisibility(View.GONE);
        }
        viewHolder.tv_groupName.setTypeface(typeface);
        viewHolder.total_serch_text.setText(String.valueOf(getChildrenCount(groupPosition))); //총검색 결과 보여줌
        viewHolder.total_serch_text.setTypeface(typeface);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.relation_child_itme, null);
            viewHolder.tv_childName = (TextView) convertView.findViewById(R.id.child_text);
            viewHolder.chile_button_divider = (View) convertView.findViewById(R.id.chile_button_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder == null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_childName.setText(getChild(groupPosition, childPosition).getTitle());
        viewHolder.tv_childName.setTypeface(typeface);
        if (isLastChild) {
            viewHolder.chile_button_divider.setVisibility(View.GONE);
        } else {
            viewHolder.chile_button_divider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewHolder {
        public TextView tv_groupName;
        public TextView total_serch_text;
        public View parent_top_divider;
        public View chile_button_divider;
        public TextView tv_childName;
    }
}

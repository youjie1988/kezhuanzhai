package com.huaheng.mobilewms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.MenuBean;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<DetailBean> mList = new ArrayList<>();

    public DetailAdapter(Context context) {
        this.mContext = context;
    }

    public List<DetailBean> getList() {
        return mList;
    }

    public void setList(List<DetailBean> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_list_item, null);
            ImageView detailView = (ImageView) convertView.findViewById(R.id.detailView);
            TextView detailType = (TextView) convertView.findViewById(R.id.detailType);
            TextView detailName = (TextView) convertView.findViewById(R.id.detailName);
            TextView detailCode = (TextView) convertView.findViewById(R.id.detailCode);
            ImageView completeView = (ImageView) convertView.findViewById(R.id.completeView);
            viewHolder.icon = detailView;
            viewHolder.type = detailType;
            viewHolder.name = detailName;
            viewHolder.code = detailCode;
            viewHolder.complete = completeView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DetailBean bean = (DetailBean) mList.get(position);
        viewHolder.icon.setImageDrawable(bean.getDrawable());
        viewHolder.type.setText(bean.getType());
        viewHolder.name.setText(bean.getName());
        viewHolder.code.setText(bean.getCode());
        if(bean.isComplete()) {
            viewHolder.complete.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.complete_2));
            viewHolder.complete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.complete.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView type;
        TextView name;
        TextView code;
        ImageView complete;
    }
}

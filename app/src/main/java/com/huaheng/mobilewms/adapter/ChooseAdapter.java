package com.huaheng.mobilewms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.bean.ChooseBean;

import java.util.ArrayList;
import java.util.List;

public class ChooseAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChooseBean> mList = new ArrayList<>();

    public ChooseAdapter(Context context) {
        this.mContext = context;
    }

    public List<ChooseBean> getList() {
        return mList;
    }

    public void setList(List<ChooseBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_list_item, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ChooseBean bean = (ChooseBean) mList.get(position);
        viewHolder.icon.setImageDrawable(bean.getDrawable());
        viewHolder.name.setText(bean.getName());
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        ImageView icon;
    }
}

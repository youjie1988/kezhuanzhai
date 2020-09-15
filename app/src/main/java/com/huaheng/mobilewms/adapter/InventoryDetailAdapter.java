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
import com.huaheng.mobilewms.bean.InventoryDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjie on 2020/6/3
 */
public class InventoryDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<InventoryDetailBean> mList = new ArrayList<>();

    public InventoryDetailAdapter(Context context) {
        this.mContext = context;
    }

    public List<InventoryDetailBean> getList() {
        return mList;
    }

    public void setList(List<InventoryDetailBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.inventory_detail_list_item, null);
            ImageView detailView = (ImageView) convertView.findViewById(R.id.detailView);
            TextView detailContainer = (TextView) convertView.findViewById(R.id.detailContainer);
            TextView detailCode = (TextView) convertView.findViewById(R.id.detailCode);
            TextView detailName = (TextView) convertView.findViewById(R.id.detailName);
            TextView detailNumber = (TextView) convertView.findViewById(R.id.detailNumber);
            viewHolder.icon = detailView;
            viewHolder.containerCode = detailContainer;
            viewHolder.name = detailName;
            viewHolder.code = detailCode;
            viewHolder.number = detailNumber;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InventoryDetailBean bean = (InventoryDetailBean) mList.get(position);
        viewHolder.icon.setImageDrawable(bean.getDrawable());
        viewHolder.containerCode.setText(bean.getContainerCode());
        viewHolder.code.setText(bean.getCode());
        viewHolder.name.setText(bean.getName());
        viewHolder.number.setText(bean.getAmount());
        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView containerCode;
        TextView code;
        TextView name;
        TextView number;
    }

}

package com.huaheng.mobilewms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.bean.MenuBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<MenuBean> mList = new ArrayList<>();

    public MenuAdapter(Context context) {
        this.mContext = context;
    }

    public List<MenuBean> getList() {
        return mList;
    }

    public void setList(List<MenuBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_list_item, null);
            ImageView menuView = (ImageView) convertView.findViewById(R.id.menuView);
            TextView menuName = (TextView) convertView.findViewById(R.id.menuName);
            viewHolder.icon = menuView;
            viewHolder.name = menuName;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MenuBean bean = (MenuBean) mList.get(position);
        viewHolder.icon.setImageDrawable(bean.getMenu());
        viewHolder.name.setText(bean.getName());
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        ImageView icon;
    }
}

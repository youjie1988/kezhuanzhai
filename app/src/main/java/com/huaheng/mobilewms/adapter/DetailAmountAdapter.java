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
import com.huaheng.mobilewms.view.AmountView;
import com.huaheng.mobilewms.view.DetailAmountView;

import java.util.ArrayList;
import java.util.List;

public class DetailAmountAdapter extends BaseAdapter {

    private Context mContext;
    private List<DetailBean> mList = new ArrayList<>();
    private AmountChangeListener amountChangeListener;

    public DetailAmountAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List <DetailBean> getmList() {
        return mList;
    }

    public void setmList(List <DetailBean> mList) {
        this.mList = mList;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_amount_list_item, null);
            ImageView detailView = (ImageView) convertView.findViewById(R.id.detailView);
            TextView detailType = (TextView) convertView.findViewById(R.id.detailType);
            TextView detailName = (TextView) convertView.findViewById(R.id.detailName);
            TextView detailCode = (TextView) convertView.findViewById(R.id.detailCode);
            DetailAmountView amountView = (DetailAmountView) convertView.findViewById(R.id.amountView);
            viewHolder.icon = detailView;
            viewHolder.type = detailType;
            viewHolder.name = detailName;
            viewHolder.code = detailCode;
            viewHolder.amountView = amountView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DetailBean bean = (DetailBean) mList.get(position);
        viewHolder.icon.setImageDrawable(bean.getDrawable());
        viewHolder.type.setText(bean.getType());
        viewHolder.name.setText(bean.getName());
        viewHolder.code.setText(bean.getCode());
        viewHolder.amountView.setAmount(bean.getAmount());
        viewHolder.amountView.setMaxValue(bean.getMaxAmount());
        if(amountChangeListener != null) {
            viewHolder.amountView.setOnAmountChangeListener(new DetailAmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    amountChangeListener.onAmountChange(position, amount);
                }
            });
        }
        return convertView;
    }

    public void setOnAmountListener(AmountChangeListener onAmountChangeListener) {
        amountChangeListener = onAmountChangeListener;
    }

    public interface AmountChangeListener{
         void onAmountChange(int id, int amount);
    }

    private class ViewHolder {
        ImageView icon;
        TextView type;
        TextView name;
        TextView code;
        DetailAmountView amountView;
    }
}

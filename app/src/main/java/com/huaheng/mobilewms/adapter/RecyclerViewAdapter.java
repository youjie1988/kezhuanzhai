package com.huaheng.mobilewms.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.bean.MenuBean;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    private List<MenuBean> mList;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public RecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public List<MenuBean> getList() {
        return mList;
    }

    public void setList(List<MenuBean> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        int parentHeight = parent.getHeight();
        int parentWidth = parent.getWidth();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_content,parent,false);
        RecyclerHolder holder = new RecyclerHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = parentHeight / 3;
        layoutParams.width = parentWidth / 3;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder recyclerHolder, final int position) {
        MenuBean menuBean =  mList.get(position);
        recyclerHolder.getIcon().setBackground(menuBean.getMenu());
        recyclerHolder.getTextView().setText(menuBean.getName());
        if (mOnItemClickListener != null) {
            //绑定点击事件
            recyclerHolder.getLinelayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //把条目的位置回调回去
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }
}

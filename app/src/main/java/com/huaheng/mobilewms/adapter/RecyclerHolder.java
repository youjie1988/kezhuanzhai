package com.huaheng.mobilewms.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaheng.mobilewms.R;


public class RecyclerHolder extends RecyclerView.ViewHolder{

    private ImageView icon;
    private TextView textView;
    private LinearLayout linelayout;

    public RecyclerHolder(@NonNull View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.contentIcon);
        textView = (TextView) itemView.findViewById(R.id.contentName);
        linelayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public LinearLayout getLinelayout() {
        return linelayout;
    }

    public void setLinelayout(LinearLayout linelayout) {
        this.linelayout = linelayout;
    }
}

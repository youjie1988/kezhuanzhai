package com.huaheng.mobilewms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huaheng.mobilewms.R;

public class LineLayout extends FrameLayout {

    private TextView lineName;
    private TextView lineContent;
    private View view;

    public LineLayout(@NonNull Context context) {
        super(context);

        view = LayoutInflater.from(context).inflate(R.layout.layout_line,null,false);
        lineName = view.findViewById(R.id.lineName);
        lineContent = view.findViewById(R.id.lineContent);
        addView(view);
    }

    public LineLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        view = LayoutInflater.from(context).inflate(R.layout.layout_line,null,false);
        lineName = view.findViewById(R.id.lineName);
        lineContent = view.findViewById(R.id.lineContent);
        addView(view);
    }

    public void setSelect() {
        lineContent.setTextIsSelectable(true);
    }

    public void setLineName(String name) {
        lineName.setText(name);
    }

    public void setLineContent(String content) {
        lineContent.setText(content);
    }
}

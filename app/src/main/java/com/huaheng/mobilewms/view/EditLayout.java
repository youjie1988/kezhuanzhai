package com.huaheng.mobilewms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huaheng.mobilewms.R;

/**
 * Created by youjie on 2020/5/4
 */
public class EditLayout extends FrameLayout {

    private TextView editName;
    private EditText editContent;
    private View view;

    public EditLayout(@NonNull Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.layout_edit,null,false);
        editName = view.findViewById(R.id.editName);
        editContent = view.findViewById(R.id.editContent);
        addView(view);
    }

    public EditLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.layout_edit,null,false);
        editName = view.findViewById(R.id.editName);
        editContent = view.findViewById(R.id.editContent);
        addView(view);
    }

    public void setEditName(String name) {
        editName.setText(name);
    }

    public String getEditValue() {
        return editContent.getText().toString();
    }

    public EditText getEditContent() {
        return editContent;
    }

    public void setEditContent(EditText editContent) {
        this.editContent = editContent;
    }
}

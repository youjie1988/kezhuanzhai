package com.huaheng.mobilewms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.huaheng.mobilewms.R;

import java.util.List;

/**
 * Created by youjie on 2020/9/9
 */
public class TypeSpinnerLayout extends FrameLayout {

    private TextView spinnerName;
    private Spinner spinnerContent;
    private View view;

    public TypeSpinnerLayout(@NonNull Context context) {
        super(context);

        view = LayoutInflater.from(context).inflate(R.layout.layout_spinner_type,null,false);
        spinnerName = view.findViewById(R.id.spinnerName);
        spinnerContent = view.findViewById(R.id.spinnerContent);
        addView(view);
    }

    public TypeSpinnerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        view = LayoutInflater.from(context).inflate(R.layout.layout_spinner_type,null,false);
        spinnerName = view.findViewById(R.id.spinnerName);
        spinnerContent = view.findViewById(R.id.spinnerContent);
        addView(view);
    }

    public void setLineName(String name) {
        spinnerName.setText(name);
    }

    public void setLineArray(Context context, List<String> strs) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, strs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContent.setAdapter(adapter);
    }

    public Spinner getSpinner() {
        return spinnerContent;
    }
}

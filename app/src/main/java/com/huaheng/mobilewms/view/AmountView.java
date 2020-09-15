package com.huaheng.mobilewms.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.util.WMSUtils;


public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher{

    private int amount = 1; //购买数量

    private OnAmountChangeListener mListener;

    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;
    private TextView lineName;
    private boolean isBtnDecreaseLongClick = false, isBtnIncreaseLongClick = false;
    private int maxValue = Integer.MAX_VALUE;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = (EditText) findViewById(R.id.etAmount);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        lineName = (TextView) findViewById(R.id.lineName);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, 40);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 60);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 18);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 24);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }

        btnDecrease.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnDecreaseLongClick = true;
                return false;
            }
        });

        btnDecrease.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if(isBtnDecreaseLongClick) {
                            if (amount > 1) {
                                amount--;
                                etAmount.setText(String.valueOf(amount));
                                etAmount.setSelection(String.valueOf(amount).length());
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isBtnDecreaseLongClick = false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        btnIncrease.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnIncreaseLongClick = true;
                return false;
            }
        });

        btnIncrease.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if(isBtnIncreaseLongClick) {
                            if(amount < maxValue) {
                                amount++;
                                etAmount.setText(String.valueOf(amount));
                                etAmount.setSelection(String.valueOf(amount).length());
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isBtnIncreaseLongClick = false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setLineName(String name) {
        lineName.setText(name);
    }

    public void setAmount(int amount1) {
        amount = amount1;
        etAmount.setText(String.valueOf(amount));
        etAmount.setSelection(String.valueOf(amount).length());
    }

    public void setMaxValue(int value) {
        maxValue = value;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > 1) {
                amount--;
                etAmount.setText(String.valueOf(amount));
                etAmount.setSelection(String.valueOf(amount).length());
            }
        } else if (i == R.id.btnIncrease) {
            if(amount < maxValue) {
                amount++;
                etAmount.setText(String.valueOf(amount));
                etAmount.setSelection(String.valueOf(amount).length());
            }
        }
        etAmount.clearFocus();
        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

//    @Override
//    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//        String content = textView.getText().toString();
//        if (WMSUtils.isEmpty(content)) {
//            return false;
//        }
//        amount = Integer.valueOf(content);
//        if (mListener != null) {
//            mListener.onAmountChange(this, amount);
//        }
//        return false;
//    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
            return;
        }
        amount = Integer.valueOf(s.toString());
        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }

}

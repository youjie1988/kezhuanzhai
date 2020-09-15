package com.huaheng.mobilewms.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by youjie
 * on 2020/2/29
 */
public class InputFilterMinMax implements InputFilter {
    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    private String getInput (CharSequence source, int start, int end, Spanned dest, int dstart, int dend){

        CharSequence destBefore = dest.subSequence(0, dstart);
        CharSequence destAfter = dest.subSequence(dend, dest.length());

        String input = destBefore.toString() + source.subSequence(start, end) + destAfter.toString();
        return input;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String inputStr = getInput(source, start, end, dest, dstart, dend);

            if(inputStr.isEmpty())
                return null;

            int input = Integer.parseInt(inputStr);
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return dest.subSequence(dstart, dend);
    }


    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}

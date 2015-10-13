package com.sg.FFTinputMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class ShowTextView extends TextView implements OutPutListener {
    CharSequence charSequence;

    public ShowTextView(Context context) {
        super(context);
    }

    public ShowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private char[] mapTable = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            '5', '1', 'a', '9', ' ', '+', '-', 0,
            0, 0, 'A', 0, '\n', '*', '%', 0,
            '6', '2', 'e', '0', ',', '_', '=', 0,
            0, 0, 'E', '0', '.', '^', '#', 0,
            'b', 'c', 'd', 'f', 0, 0, 0, 0,
            'B', 'C', 'D', 'F', 0, 0, 0, 0,
            '7', '3', 'i', 'j', '?', '/', '\\', 0,
            0, 0, 'I', 'J', '!', '$', '@', 0,
            'g', 'h', 'm', 'n', 0, 0, 0, 0,
            'G', 'H', 'M', 'N', 0, 0, 0, 0,
            '(', ')', '<', '>', 0, 0, 0, 0,
            '[', ']', '{', '}', 0, 0, 0, 0,
            '8', '4', 'o', 'k', 8, '|', '&', 0,
            0, 0, 'O', 'K', 127, '`', '~', 0,
    };

    @Override
    public void onOutPut(int a) {
        charSequence = this.getText();
        if (mapTable[a] != 0) {
            if (mapTable[a] == 8 || mapTable[a] == 127)
                this.setText(charSequence.subSequence(0, charSequence.length() - 1));
            else this.setText(charSequence + String.valueOf(mapTable[a]));
        }
    }
}

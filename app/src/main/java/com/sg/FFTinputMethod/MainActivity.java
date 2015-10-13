package com.sg.FFTinputMethod;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {
    private EscapeView escapeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        escapeView=new EscapeView(this);
        setContentView(escapeView);
    }
}
